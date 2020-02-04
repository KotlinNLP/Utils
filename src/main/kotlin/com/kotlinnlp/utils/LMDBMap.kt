/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.utils

import org.lmdbjava.Dbi
import org.lmdbjava.Env
import java.io.Closeable
import java.nio.ByteBuffer
import java.nio.ByteBuffer.allocateDirect
import org.lmdbjava.KeyRange
import org.lmdbjava.Env.create
import org.lmdbjava.DbiFlags.MDB_CREATE
import org.lmdbjava.EnvFlags.MDB_NOLOCK
import org.lmdbjava.EnvFlags.MDB_RDONLY_ENV
import java.io.File
import java.nio.file.Path

/**
 * Define a wrapper of the Lightning Memory Database (LMDB) for Java, implemented as extension a [MutableMap].
 *
 * @param path the path to the database directory
 * @param readOnly whether to open the database in read-only mode
 */
abstract class LMDBMap<K, V>(path: Path, readOnly: Boolean = false) : MutableMap<K, V>, Closeable {

  companion object {

    /**
     * Arbitrary DB name.
     */
    private const val DB_NAME = "__DB__"

    /**
     * @return a file object representing this path
     */
    private fun Path.toFileOrCreate(): File = this.toFile().also { if (!it.exists()) it.mkdir() }

    /**
     * Convert a [ByteBuffer] to a [ByteArray].
     *
     * @return an array of bytes
     */
    private fun ByteBuffer.asByteArray(): ByteArray {

      this.rewind()

      return ByteArray(this.remaining()).also { this.get(it) }
    }
  }

  /**
   * All the key/value pairs in this map.
   */
  override val entries: MutableSet<MutableMap.MutableEntry<K, V>> get() {

    val entries = mutableSetOf<MutableMap.MutableEntry<K, V>>()

    this.env.txnRead().use { txn ->
      this.db.iterate(txn, KeyRange.all<ByteBuffer>()).use {
        for (kv in it.iterable()) {
          val key = deserializeKey(kv.key().asByteArray())
          val value = deserializeValue(kv.`val`().asByteArray())
          entries.add(MutableEntry(key, value))
        }
      }
    }

    return entries
  }

  /**
   * The number of entries in this map.
   */
  override val size: Int get() = this.env.txnRead().use { txn ->
    this.db.stat(txn).entries.toInt()
  }

  /**
   * All the keys in this map.
   */
  override val keys: MutableSet<K> get() {

    val keys = mutableSetOf<K>()

    this.env.txnRead().use { txn ->
      this.db.iterate(txn, KeyRange.all<ByteBuffer>()).use {
        for (kv in it.iterable()) {
          keys.add(deserializeKey(kv.key().asByteArray()))
        }
      }
    }

    return keys
  }

  /**
   * All the values in this map.
   *
   * Note: this collection may contain duplicate values.
   */
  override val values: MutableCollection<V> get() {

    val values = mutableListOf<V>()

    this.env.txnRead().use { txn ->
      this.db.iterate(txn, KeyRange.all<ByteBuffer>()).use {
        for (kv in it.iterable()) {
          values.add(deserializeValue(kv.`val`().asByteArray()))
        }
      }
    }

    return values
  }

  /**
   * The environment for a single DB.
   */
  private val env: Env<ByteBuffer> = create()
    .setMapSize(1099511627776)
    .setMaxDbs(1)
    .open(path.toFileOrCreate(), *(if (readOnly) arrayOf(MDB_NOLOCK, MDB_RDONLY_ENV) else arrayOf()))

  /**
   * The database.
   */
  private val db: Dbi<ByteBuffer> = this.env.openDbi(DB_NAME, MDB_CREATE)

  /**
   * The mutable entry of this map.
   *
   * @property key the key
   * @property values the value
   */
  inner class MutableEntry(override val key: K, override val value: V) : MutableMap.MutableEntry<K, V> {

    /**
     * Change the value associated with the key of this entry.
     *
     * @return the previous value associated with the key
     */
    override fun setValue(newValue: V): V = this@LMDBMap.put(this.key, newValue)!!
  }

  /**
   * @param key a key
   *
   * @return the byte array representation of the [key]
   */
  abstract fun serializeKey(key: K): ByteArray

  /**
   * @param value a value
   *
   * @return the byte array representation of the [value]
   */
  abstract fun serializeValue(value: V): ByteArray

  /**
   * @param key the byte array representation of the key
   *
   * @return the deserialized key
   */
  abstract fun deserializeKey(key: ByteArray): K

  /**
   * @param value the byte array representation of the value
   *
   * @return the deserialized value
   */
  abstract fun deserializeValue(value: ByteArray): V

  /**
   * @param key a key
   *
   * @return true if the map contains the given [key]
   */
  override fun containsKey(key: K): Boolean = this[key] != null

  /**
   * @param value a value
   *
   * @return true if the map maps one or more keys to the specified [value]
   */
  override fun containsValue(value: V): Boolean {

    this.env.txnRead().use { txn ->
      this.db.iterate(txn, KeyRange.all<ByteBuffer>()).use {
        for (kv in it.iterable()) {

          if (deserializeValue(kv.`val`().asByteArray()) == value) {
            it.close()
            txn.close()
            return true
          }
        }
      }
    }

    return false
  }

  /**
   * @param key a key
   *
   * @return the value corresponding to the given [key] or null if such a key is not present in the map
   */
  override fun get(key: K): V? {

    val keyBuf = allocateDirect(this.env.maxKeySize)
    keyBuf.put(serializeKey(key)).flip()

    return this.env.txnRead().use { txn ->
      this.db.get(txn, keyBuf)?.let { this.deserializeValue(txn.`val`().asByteArray()) }
    }
  }

  /**
   * @return true if the map is empty (contains no elements) otherwise false
   */
  override fun isEmpty(): Boolean = this.size == 0

  /**
   * Remove all the elements from this map.
   */
  override fun clear() {

    this.env.txnRead().use { txn ->
      this.db.iterate(txn, KeyRange.all<ByteBuffer>()).use {
        for (kv in it.iterable()) {
          this.db.delete(kv.key())
        }
      }
    }
  }

  /**
   * Add a key/value pair to this map.
   *
   * @param key the key to add
   * @param value the value to associate to the [key]
   *
   * @return the previous value associated with the given [key] or null if it was not present
   */
  override fun put(key: K, value: V): V? {

    val prev: V? = this[key]

    val valueSer = serializeValue(value)
    val valueBuf = allocateDirect(valueSer.size)
    val keyBuf = allocateDirect(this.env.maxKeySize)

    keyBuf.put(serializeKey(key)).flip()
    valueBuf.put(valueSer).flip()

    this.db.put(keyBuf, valueBuf)

    return prev
  }

  /**
   * Add all the key/value pairs from a given map.
   *
   * @param from another map
   */
  override fun putAll(from: Map<out K, V>) {

    from.forEach {
      this[it.key] = it.value
    }
  }

  /**
   * Remove a given key from this map.
   *
   * @param key the key to remove
   *
   * @return the previous value associated with the given [key] or null if it was not present
   */
  override fun remove(key: K): V? {

    val prev: V? = this[key]

    val keyBuf = allocateDirect(this.env.maxKeySize)
    keyBuf.put(serializeKey(key)).flip()

    this.db.delete(keyBuf)

    return prev
  }

  /**
   * Close the DB.
   */
  override fun close() {
    this.db.close()
    this.env.close()
  }
}
