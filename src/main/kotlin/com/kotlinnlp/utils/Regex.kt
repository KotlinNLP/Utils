/* Copyright 2020-present Simone Cangialosi. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.utils

/**
 * Collection of common regular expressions.
 */
object Regex {

  /**
   * Match numeric values.
   */
  val numbers = Regex("^\\d+(?:[,.]\\d+)*(?:\\[.,]\\d+)?\$")

  /**
   * Match a punctuation token.
   */
  val punctuation = Regex("^[….,;:#!?|/\\\\$%&=~*\\-–_\"“”″‘'`^()<>«»\\[\\]{}]+$")
}
