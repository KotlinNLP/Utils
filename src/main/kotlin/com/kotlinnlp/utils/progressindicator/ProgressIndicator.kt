/* Copyright 2016-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.utils.progressindicator

import java.io.OutputStream
import java.lang.Math.floor

/**
 * Used to implement a helper that tracks a progress.
 *
 * @param total the total amount of iterable elements to track
 * @param outputStream the stream into which the output will be written
 */
abstract class ProgressIndicator(protected val total: Int, protected val outputStream: OutputStream) {

  /**
   * The current amount of progress units.
   */
  private var current: Int = 0

  /**
   * The current percentage of the progress (as int in the range [-1, 100]).
   * -1 is the initialization value.
   */
  protected var perc: Int = -1

  /**
   * Advance the progress by a given amount of units and update the indicator.
   *
   * @param amount the amount of units to add to the progress
   */
  fun tick(amount: Int = 1) {

    this.current += amount

    val curPerc = floor(100.0 * this.current / this.total).toInt()

    if (curPerc > this.perc) {
      this.perc = curPerc
      this.print()
    }
  }

  /**
   * Update the indicator writing in the [outputStream].
   */
  private fun print() {

    this.outputStream.write("\r%s%s".format(
      this.buildProgressString(),
      if (this.perc == 100) "\n" else "").toByteArray())

    this.outputStream.flush()
  }

  /**
   * Build the current progress string.
   *
   * @return a string with the current progress indicator
   */
  protected abstract fun buildProgressString(): String
}
