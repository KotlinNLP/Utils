/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.utils

import java.nio.charset.Charset

/**
 * Copy this list replacing the element at a given index.
 *
 * @param index the index of the replacement
 * @param elm the replacement
 *
 * @return a new list with the given element replaced
 */
fun <T> List<T>.replace(index: Int, elm: T): List<T> =
  this.mapIndexed { i, it ->  if (i == index) elm else it }

/**
 * Remove the elements from this list from the given index.
 *
 * @param fromIndex the index from which to remove items
 *
 * @return the list without element after the index
 */
fun <T>MutableList<T>.removeFrom(fromIndex: Int): MutableList<T> {
  this.subList(fromIndex, this.size).clear()
  return this
}

/**
 * @param callback a callback that returns a collection
 *
 * @return this collection if it is not empty, otherwise the value returned by the callback
 */
fun <T, C : Collection<T>> C.notEmptyOr(callback: () -> C): C =
  if (this.isNotEmpty()) this else callback()

/**
 * @param callback a callback that returns a map
 *
 * @return this map if it is not empty, otherwise the value returned by the callback
 */
fun <K, V, M : Map<K, V>> M.notEmptyOr(callback: () -> M): M =
  if (this.isNotEmpty()) this else callback()

/**
 * Returns the combinations of the elements in this list as a list of pairs.
 *
 * The returned list is empty if this collection contains less than two elements.
 *
 * @return the combinations of the elements
 */
fun <T>List<T>.combine(): List<Pair<T, T>> = this.foldIndexed(mutableListOf()) { i, acc, element ->

  for (j in i + 1 until this.size) {
    acc.add(Pair(element, this[j]))
  }

  acc
}

/**
 * Returns a list containing all elements of the this list and, if not null, all the [other] elements.
 *
 * @param other the elements to concatenate (can be null)
 */
fun <T> List<T>.concat(other: List<T>?): List<T> = other?.let { this.plus(it) } ?: this

/**
 * Encodes the contents of this string using the specified character set and returns the resulting byte list.
 */
fun String.toByteList(charset: Charset = Charsets.UTF_8) = this.toByteArray(charset).toList()