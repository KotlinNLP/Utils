/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.utils

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
