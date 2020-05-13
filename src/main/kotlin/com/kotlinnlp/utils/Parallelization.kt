/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.utils

import java.util.concurrent.*

/**
 * Map the elements of this iterable executing the [transform] function in parallel threads and collecting the results.
 * If given, at max [maxConcurrentThreads] threads are executed in parallel.
 *
 * @param maxConcurrentThreads the max number of concurrent threads at a time
 * @param transform the transform function applied to each element of this collection
 */
fun <A, B> Iterable<A>.pmap(maxConcurrentThreads: Int? = null, transform: (A) -> B): List<B> {

  require(maxConcurrentThreads == null || maxConcurrentThreads > 0) {
    "The number of max concurrent threads must be greater than 0."
  }

  if (maxConcurrentThreads == 1) return this.map(transform)

  val results = ConcurrentHashMap<Int, B>()
  val sem: Semaphore? = maxConcurrentThreads?.let { Semaphore(it) }

  this
    .mapIndexed { i, it ->
      Thread {
        sem?.acquire()
        results[i] = transform(it)
        sem?.release()
      }.apply { start() }
    }
    .forEach { it.join() }

  return results.keys.asSequence().sorted().map { key -> results.getValue(key) }.toList()
}
