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
   * Match whitespaces.
   */
  val whitespaces = Regex("\\s")

  /**
   * Match numeric values.
   */
  val numbers = Regex("^\\d+(?:[,.]\\d+)*(?:\\[.,]\\d+)?\$")

  /**
   * Match a punctuation token.
   *
   * Following a list of the punctuation types that match:
   * - apostrophe: ’'
   * - brackets: ()[]{}<>
   * - colon: :
   * - comma: ,
   * - dashes: ‒–—―
   * - ellipsis: …
   * - exclamation mark: !
   * - full stop/period: .
   * - guillemets: «»
   * - hyphen: -‐
   * - question mark: ?
   * - quotation marks: ‘’“”
   * - semicolon: ;
   * - slash/stroke: /
   * - backslash: \\
   * - solidus: ⁄
   * - space: ␠
   * - interpunct: ·
   * - ampersand: &
   * - at sign: @
   * - asterisk: *
   * - bullet: •
   * - caret: ^
   * - currency: ¤¢$€£¥₩₪
   * - dagger: †‡
   * - degree: °
   * - inverted exclamation point: ¡
   * - inverted question mark: ¿
   * - negation: ¬
   * - number sign (hashtag): #
   * - number sign: №
   * - percent and related signs: %‰‱
   * - pilcrow: ¶
   * - prime: ′
   * - section sign: §
   * - tilde/swung dash: ~
   * - umlaut/diaeresis: ¨
   * - underscore/understrike: _
   * - vertical/pipe/broken bar: |¦
   * - asterism: ⁂
   * - index/fist: ☞
   * - therefore sign: ∴
   * - interrobang: ‽
   * - reference mark: ※
   */
  val punctuation = Regex("^[….,;:#№!?¿|¦/\\\\%‰‱&=~*@\\-‐‒–—―_\"“”″‘’'`′^()<>«»\\[\\]{}⁄␠·•¤¢$€£¥₩₪†‡°¨¡¬¶§⁂∴☞‽※]+$")
}
