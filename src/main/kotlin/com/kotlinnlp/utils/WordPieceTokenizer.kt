/* Copyright 2020-present Simone Cangialosi. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.utils

/**
 * Text tokenizer in word pieces.
 *
 * @param vocabulary a vocabulary on which to base the tokenization
 * @param unknownToken the string used to indicate unknown tokens
 */
class WordPieceTokenizer(private val vocabulary: DictionarySet<String>, private val unknownToken: String = "[UNK]") {

  /**
   * Tokenize a piece of text into its word pieces.
   *
   * This uses a greedy longest-match-first algorithm to perform tokenization using the given [vocabulary].
   *
   * For example:
   *   input = "unaffable"
   *   output = ["un", "##aff", "##able"]
   *
   * @param text the input text
   * @param maxCharsPerToken the max number of chars of a token to consider it unknown
   *
   * @return the word piece tokens
   */
  fun tokenize(text: String, maxCharsPerToken: Int = 100): List<String> {

    val tokens: MutableList<String> = mutableListOf()

    this.basicTokenize(text).forEach { token ->

      if (token.length > maxCharsPerToken) {

        tokens.add(this.unknownToken)

      } else {

        var isBad = false
        var start = 0
        val subTokens: MutableList<String> = mutableListOf()

        while (start < token.length) {

          var end: Int = token.length
          var curSubstr: String? = null

          while (start < end) {

            val substr: String = token.substring(start, end).let { if (start > 0) "##$it" else it }

            if (substr in this.vocabulary) {
              curSubstr = substr
              break
            }

            end -= 1
          }

          if (curSubstr == null) {
            isBad = true
            break
          }

          subTokens.add(curSubstr)
          start = end
        }

        if (isBad) tokens.add(this.unknownToken) else tokens.addAll(subTokens)
      }
    }

    return tokens.toList()
  }

  /**
   * Split a text in tokens by spaces and punctuation.
   *
   * @param text the input text
   *
   * @return the tokens split by spaces and punctuation
   */
  private fun basicTokenize(text: String): List<String> {

    val tokens: MutableList<String> = mutableListOf()
    var start = 0

    text.forEachIndexed { i, c ->

      val isPunct: Boolean = Regex.punctuation.matches(c.toString())

      if (c.isWhitespace() || isPunct) {

        if (start < i)
          tokens.add(text.substring(start, i))

        start = i + 1
      }

      if (isPunct)
        tokens.add(c.toString())
    }

    if (start < text.length)
      tokens.add(text.substring(start))

    return tokens
  }
}
