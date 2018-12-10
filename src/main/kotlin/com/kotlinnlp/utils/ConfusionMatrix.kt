/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.utils

/**
 * Implement a confusion matrix.
 *
 * @property labels the list of labels for the matrix data
 */
class ConfusionMatrix(val labels: List<String>) {

  /**
   * Check requirements.
   */
  init {
    require(this.labels.isNotEmpty()) { "The labels list cannot be empty." }
  }

  /**
   * The size of a single dimension of the square matrix.
   */
  val size: Int = this.labels.size

  /**
   * The maximum length of the labels.
   */
  private val maxLabelLength: Int = this.labels.maxBy { it.length }!!.length

  /**
   * The length of the formatted labels.
   * The minimum value is equal to the length of the formatted percentage (%5.1f%%).
   */
  private val formattedLabelLength: Int = maxOf(6, this.maxLabelLength)

  /**
   * A string with length equal to [maxLabelLength] and composed only by spaces.
   */
  private val emptyLabel: String = " ".repeat(this.maxLabelLength)

  /**
   * The headers of the table.
   * They are the labels centered in strings with the same length, equal to [formattedLabelLength].
   */
  private val headers: List<String> = listOf(emptyLabel) + this.labels.map { it.center(this.formattedLabelLength) }

  /**
   * The starting label of each row.
   * They are the labels centered in strings with the same length, equal to [maxLabelLength].
   */
  private val rowLabels: List<String> = this.labels.map { it.center(this.maxLabelLength) }

  /**
   * The confusion matrix data.
   */
  private val data: List<IntArray> = List(size = this.size, init = { IntArray(this.size) })

  /**
   * Reset the matrix counts to zeros.
   */
  fun reset() {
    this.data.forEach { row -> row.indices.forEach { i -> row[i] = 0 } }
  }

  /**
   * Increment the count of an expected value.
   *
   * @param expected the index of the expected value
   * @param found the index of the found value
   */
  fun increment(expected: Int, found: Int) {
    this.data[expected][found] += 1
  }

  /**
   * @return the string representation of the confusion matrix
   */
  override fun toString(): String {

    val header = this.headers.joinToString(prefix = " ", separator = " | ", postfix = " ")
    var str = header

    str += "\n"
    str += "-".repeat(header.length)
    str += "\n"

    str += (0 until this.size).joinToString(
      transform = { i ->
        val row: IntArray = this.data[i]
        val normRow = DoubleArray(size = row.size, init = { row[it].toDouble() })
        val rowSum: Int = row.sum()

        if (rowSum > 0) normRow.indices.forEach { normRow[it] = normRow[it] / rowSum }

        (0 until this.size).joinToString(
          prefix = " ${this.rowLabels[i]} | ",
          transform = { j -> "%5.1f%%".format(100.0 * normRow[j]) },
          separator = " | ",
          postfix = " ")
      },
      separator = "\n")

    return str
  }

  /**
   * @param width the length of the returning string
   *
   * @return this string centered in a string of the given width, padded with spaces
   */
  private fun String.center(width: Int): String {

    val totalPaddingLength: Int = width - this.length
    val nextPaddingLength: Int = totalPaddingLength / 2
    val prevPaddingLength: Int = totalPaddingLength - nextPaddingLength

    return " ".repeat(prevPaddingLength) + this + " ".repeat(nextPaddingLength)
  }
}