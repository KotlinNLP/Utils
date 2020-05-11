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
 * @property vocabulary a vocabulary on which to base the tokenization
 * @property unknownToken the string used to indicate unknown tokens
 * @property splitPrefix the prefix that indicates the word pieces after the first
 */
class WordPieceTokenizer(
  val vocabulary: DictionarySet<String>,
  val unknownToken: String = "[UNK]",
  val splitPrefix: String = "##"
) {

  /**
   * Tokenize a piece of text into its word-pieces.
   *
   * This uses a greedy longest-match-first algorithm to perform tokenization using the given [vocabulary].
   *
   * For example:
   *   input = "unaffable"
   *   output = ["un", "##aff", "##able"]
   *
   * @param text the input text
   * @param maxCharsPerToken the max number of chars of a token to consider it unknown
   * @param neverSplit tokens that must not be split
   *
   * @return the word-piece tokens
   */
  fun tokenize(text: String, maxCharsPerToken: Int = 100, neverSplit: Set<String>? = null): List<String> =
    this.basicTokenize(text = text, neverSplit = neverSplit)
      .flatMap {
        if (it.length > maxCharsPerToken)
          sequenceOf(this.unknownToken)
        else
          this.tokenize(it)
      }
      .toList()

  /**
   * @param pieces the word-pieces resulting from a tokenization
   *
   * @return the basic words obtained concatenating the consecutive word-pieces of the given list
   */
  fun piecesToWords(pieces: List<String>): List<String> {

    val words: MutableList<String> = mutableListOf()

    pieces.forEach { piece ->
      if (piece.startsWith(this.splitPrefix))
        words[words.lastIndex] += piece.substring(this.splitPrefix.length)
      else
        words.add(piece)
    }

    return words.toList()
  }

  /**
   * Tokenize a token into its word-pieces.
   *
   * @param token a token
   *
   * @return the word-pieces of the given token
   */
  private fun tokenize(token: String): Sequence<String> {

    val wordPieces: MutableList<String> = mutableListOf()
    val subTokens: MutableList<String> = mutableListOf()
    var isBad = false
    var start = 0

    while (start < token.length) {

      var end: Int = token.length
      var curSubstr: String? = null

      while (start < end) {

        val substr: String = token.substring(start, end).let { if (start > 0) "$splitPrefix$it" else it }

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

    if (isBad) wordPieces.add(this.unknownToken) else wordPieces.addAll(subTokens)

    return wordPieces.asSequence()
  }

  /**
   * Split a text in tokens by spaces and punctuation.
   *
   * @param text the input text
   * @param neverSplit tokens that must not be split
   *
   * @return the sequence of tokens split by spaces and punctuation
   */
  private fun basicTokenize(text: String, neverSplit: Set<String>?): Sequence<String> =

    text.split(" ").asSequence().flatMap { token ->

      if (neverSplit?.contains(token) == true) {

        sequenceOf(token)

      } else {

        val subTokens: MutableList<String> = mutableListOf()
        var start = 0

        token.forEachIndexed { i, c ->

          if (Regex.punctuation.matches(c.toString())) {

            if (start < i)
              subTokens.add(token.substring(start, i))

            subTokens.add(c.toString())

            start = i + 1
          }
        }

        if (start < token.length)
          subTokens.add(token.substring(start))

        subTokens.asSequence()
      }
    }
}
