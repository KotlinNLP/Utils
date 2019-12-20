/* Copyright 2017-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.utils.stats

/**
 * Compute the moving average considering only the last [windowSize] values added.
 *
 * @param windowSize the size of the observation window
 */
class MovingAverage(private val windowSize: Int) {

  /**
   * The values collected within the observation window.
   */
  private val values: MutableList<Double> = mutableListOf()

  /**
   * Add a value to the moving average.
   *
   * @param value a new value
   */
  fun add(value: Double) {

    this.values.add(value)

    if (this.values.size > this.windowSize) this.values.removeAt(0)
  }

  /**
   * @return the mean of the values in the observation window
   */
  fun calcMean(): Double = this.values.average()

  /**
   * @return the variance of the values in the observation window
   */
  fun calcVar(): Double {

    val mean: Double = this.calcMean()

    return this.values.asSequence()
      .map {
        val diff: Double = it - mean
        diff * diff
      }
      .average()
  }

  /**
   * @return the standard deviation of the values in the observation window
   */
  fun calcStdDev(): Double = Math.sqrt(this.calcVar())
}
