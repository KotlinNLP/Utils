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
   * The number of true positive results.
   */
  var truePos: Int = 0

  /**
   * The number of false positive results.
   */
  var falsePos: Int = 0

  /**
   * The number of true negative results.
   */
  var trueNeg: Int = 0

  /**
   * The number of false negative results.
   */
  var falseNeg: Int = 0

  /**
   * The total amount of results.
   */
  val total: Int get() = truePos + falsePos + trueNeg + falseNeg

  /**
   * The precision statistic.
   */
  val precision: Double get() = truePos.toDouble() / (truePos + falsePos)

  /**
   * The recall statistic.
   */
  val recall: Double get() = truePos.toDouble() / (truePos + falseNeg)

  /**
   * The f1 score statistic.
   */
  val f1Score: Double get() = 2 * precision * recall / (precision + recall)

  /**
   * @return the string representation of the statistic metrics
   */
  override fun toString(): String = "precision: %5.2f %% recall: %5.2f %% f1: %5.2f %%"
    .format(100 * precision, 100 * recall, 100 * f1Score)
}
