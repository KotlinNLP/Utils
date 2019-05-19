/* Copyright 2017-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.utils.stats

/**
 * Convenient class to compute the moving average, giving more importance to the recent added values.
 */
class MovingAverage {

  /**
   * The mean.
   */
  var mean: Double = 0.0
    private set

  /**
   * The variance.
   */
  var variance: Double = 0.0
    private set

  /**
   * Counts the added values.
   */
  var count: Long = 0
    private set

  /**
   * Add the given [value] to the moving average
   *
   * @param value the value to add to the moving average
   */
  fun add(value: Double) {

    this.count++
    this.mean += (2.0 / this.count) * (value - this.mean)
    this.variance += (2.0 / this.count) * ((value - this.mean) * (value - this.mean) - this.variance)
  }
}