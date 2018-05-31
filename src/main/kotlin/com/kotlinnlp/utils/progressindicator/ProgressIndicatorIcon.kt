/* Copyright 2016-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.utils.progressindicator

import java.io.OutputStream

/**
 * Helper that tracks a progress indicating it with a rotating icon.
 */
class ProgressIndicatorIcon(total: Int, outputStream: OutputStream = System.out)
  : ProgressIndicator(total = total, outputStream = outputStream) {

  /**
   * The sequence of chars that compose the icon.
   */
  private val iconSequence: Array<Char> = arrayOf('-', '\\', '|', '/')

  /**
   * The index of the current position within the icon sequence.
   */
  private var iconIndex: Int = 0

  /**
   * Build the current progress string.
   *
   * @return a string with the current progress indicator
   */
  override fun buildProgressString(): String {

    val printStr = "${iconSequence[this.iconIndex]}"
    this.iconIndex = (this.iconIndex + 1) % iconSequence.size

    return printStr
  }
}
