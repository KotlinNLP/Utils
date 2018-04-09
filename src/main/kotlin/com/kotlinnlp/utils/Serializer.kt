/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.utils

import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.OutputStream

/**
 * The Serializer provides methods to serialize and deserialize any object.
 */
object Serializer {

  /**
   * Serialize an object and write it to an output stream.
   *
   * @param outputStream the [OutputStream] in which to write the serialized the object
   */
  fun <T> serialize(obj: T, outputStream: OutputStream) {

    val oos = ObjectOutputStream(outputStream)

    oos.writeObject(obj)
    oos.close()
  }

  /**
   * Read an object (serialized) from an input stream and decode it.
   *
   * @param inputStream the [InputStream] from which to read the serialized object
   *
   * @return the object read from [inputStream] and decoded
   */
  @Suppress("UNCHECKED_CAST")
  fun <T> deserialize(inputStream: InputStream): T {

    val ois = ObjectInputStream(inputStream)
    val obj = ois.readObject() as T

    ois.close()

    return obj
  }
}
