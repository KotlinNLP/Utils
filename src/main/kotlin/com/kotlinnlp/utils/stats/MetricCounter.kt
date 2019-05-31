/* Copyright 2017-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.utils.stats

/**
 * The counter of a metric, that accumulates true and false results, positive and negative.
 */
class MetricCounter {

  /**
   * The number of true positive results (correctly marked as positive).
   */
  var truePos: Int = 0

  /**
   * The number of false positive results (that should have been negative).
   */
  var falsePos: Int = 0

  /**
   * The number of false negative results (that should have been positive).
   */
  var falseNeg: Int = 0

  /**
   * The number of relevant results (that are intended to be actually positive).
   */
  val relevant: Int get() = truePos + falseNeg

  /**
   * The precision statistic.
   */
  val precision: Double get() = (truePos.toDouble() / (truePos + falsePos)).ifNaN { 0.0 }

  /**
   * The recall statistic.
   */
  val recall: Double get() = (truePos.toDouble() / relevant).ifNaN { 0.0 }

  /**
   * The f1 score statistic.
   */
  val f1Score: Double get() = (2 * precision * recall / (precision + recall)).ifNaN { 0.0 }

  /**
   * @return the string representation of the statistic metrics
   */
  override fun toString(): String = "precision %5.2f %% | recall %5.2f %% | f1 %5.2f %%"
    .format(100 * precision, 100 * recall, 100 * f1Score)

  /**
   * Reset all the counters.
   */
  fun reset() {
    this.truePos = 0
    this.falsePos = 0
    this.falseNeg = 0
  }

  /**
   * @param callback a callback called if this number is NaN
   *
   * @return this number if it is not NaN, otherwise the value returned by the [callback]
   */
  private fun Double.ifNaN(callback: () -> Double): Double = if (this.isNaN()) callback() else this
}
