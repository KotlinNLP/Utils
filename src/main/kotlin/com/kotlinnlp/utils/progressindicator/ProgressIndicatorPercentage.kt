/* Copyright 2016-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.utils.progressindicator

import java.io.OutputStream

/**
 * Helper that tracks a progress indicating it with simple percentage.
 */
class ProgressIndicatorPercentage(total: Int, outputStream: OutputStream = System.out)
  : ProgressIndicator(total = total, outputStream = outputStream) {

  /**
   * Build the current progress string.
   *
   * @return a string with the current progress indicator
   */
  override fun getProgressString(): String = "[${this.perc}%]"
}
