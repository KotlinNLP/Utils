/* Copyright 2016-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package progressindicator

import com.kotlinnlp.utils.progressindicator.ProgressIndicatorIcon

/**
 *
 */
fun main(args: Array<String>) {

  val loopSize = 1000
  val progressIndicator = ProgressIndicatorIcon(loopSize)

  (0 until loopSize).forEach {
    progressIndicator.tick()
    Thread.sleep(30)
  }
}
