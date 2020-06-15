/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.utils

import java.nio.charset.Charset
import kotlin.math.pow
import kotlin.math.roundToLong

/**
 * Copy this list replacing the element at a given index.
 *
 * @param index the index of the replacement
 * @param elm the replacement
 *
 * @return a new list with the given element replaced
 */
fun <T> List<T>.replace(index: Int, elm: T): List<T> =
  this.mapIndexed { i, it -> if (i == index) elm else it }

/**
 * Remove the elements from this list from the given index.
 *
 * @param fromIndex the index from which to remove items
 *
 * @return the list without element after the index
 */
fun <T> MutableList<T>.removeFrom(fromIndex: Int): MutableList<T> = this.apply {
  subList(fromIndex, this.size).clear()
}

/**
 * Removes an element at the position of the first element that matches the given [predicate].
 *
 * @return the element that has been removed.
 */
fun <T> MutableList<T>.removeAtIndexOfFirst(predicate: (T) -> Boolean) =
  this.removeAt(this.indexOfFirst(predicate))

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
fun <T> List<T>.combine(): List<Pair<T, T>> = this.foldIndexed(mutableListOf()) { i, acc, element ->

  for (j in i + 1 until this.size) {
    acc.add(Pair(element, this[j]))
  }

  acc
}

/**
 * Performs the given [action] on each subrange of this list.
 *
 * @param min the minimal length of a subrange
 * @param max the maximal length of a subrange
 * @param action function that acts on a subrange of this list
 */
fun <T>List<T>.forEachIndicesRange(min: Int, max: Int, action: (IntRange) -> Unit) {

  require(min >= 1) { "Expected min >= 1. Found $min" }

  for (length in min ..this.size.coerceAtMost(max)) {
    for (start in 0 .. this.size - length) {
      action(start until start + length)
    }
  }
}

/**
 * Performs the given [action] on each sub-sequence of this list.
 *
 * @param min the minimal length of a sub-sequence
 * @param max the maximal length of a sub-sequence
 * @param action function that acts on a sub-sequence of this list
 */
fun <T>List<T>.forEachGroup(min: Int,
                            max: Int,
                            action: (List<T>) -> Unit) {

  this.forEachIndicesRange(min = min, max = max) {
    action(this.subList(it.first, it.last + 1))
  }
}

/**
 * Returns a list containing all elements of the this list and, if not null, all the [other] elements.
 *
 * @param other the elements to concatenate (can be null)
 */
fun <T> List<T>.concat(other: List<T>?): List<T> =
  other?.let { this.plus(it) } ?: this

/**
 * Fold up these nested lists inverting the outer with the inner lists.
 * All the inner lists must contain the same number of elements.
 *
 * If the outer list contains M inner lists and, in turn, each of them contains N elements, then a list
 * with N inner lists will be given in output, each with M elements.
 *
 * @return the outer list folded up with the inner lists
 */
fun <T> List<List<T>>.foldUp(): List<List<T>> {

  val outer = this
  val ret: List<MutableList<T>> = List(outer.first().size) { mutableListOf<T>() }

  outer.forEach { inner -> // 1 .. M
    inner.forEachIndexed { j, elm -> // 1 .. N
      ret[j].add(elm)
    }
  }

  return ret.map { it.toList() }
}

/**
 * Encodes the contents of this string using the specified character set and returns the resulting byte list.
 */
fun String.toByteList(charset: Charset = Charsets.UTF_8) =
  this.toByteArray(charset).toList()

/**
 * Return the number with the specified number of decimals.
 *
 * @param numDecimalPlaces the number of decimals
 *
 * @return the number with the specified number of decimals
 */
fun Double.toFixed(numDecimalPlaces: Int): Double {

  val factor: Double = 10.0.pow(numDecimalPlaces)

  return (this * factor).roundToLong() / factor
}
