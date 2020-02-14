/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.utils

import java.util.concurrent.ConcurrentHashMap

/**
 * Map the elements of this iterable executing the [transform] function in parallel threads and collecting the results.
 *
 * @param transform the transform function applied to each element of this collection
 */
fun <A, B> Iterable<A>.pmap(transform: (A) -> B): List<B> {

  val results = ConcurrentHashMap<Int, B>()

  this
    .mapIndexed { i, it -> Thread { results[i] = transform(it) }.apply { start() } }
    .forEach { it.join() }

  return results.values.toList()
}
