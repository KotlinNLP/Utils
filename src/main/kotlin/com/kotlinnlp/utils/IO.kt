/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.utils

import java.io.FileInputStream
import java.io.InputStream
import java.net.URL
import java.util.*

/**
 * Get the URL of a resource contained in the `resources` directory of the package.
 *
 * @param relPath the resource path, relative to the `resources` directory and with slashes `/` as separators
 *
 * @throws RuntimeException if the resource is not present
 *
 * @return the URL of the requested resource
 */
fun getResource(relPath: String): URL = Charsets.javaClass.getResource("/$relPath")
  ?: throw RuntimeException("Cannot find the package resource under the path `$relPath`")

/**
 * Get the input stream of a resource contained in the `resources` directory of the package.
 *
 * @param relPath the resource path, relative to the `resources` directory and with slashes `/` as separators
 *
 * @throws RuntimeException if the resource is not present
 *
 * @return the input stream of the requested resource
 */
fun getResourceAsStream(relPath: String): InputStream = Charsets.javaClass.getResourceAsStream("/$relPath")
  ?: throw RuntimeException("Cannot find the package resource under the path `$relPath`")

/**
 * Iterate over the lines of this input stream.
 *
 * @param callback the callback called for each line
 */
fun InputStream.forEachLine(callback: (String) -> Unit) {

  val sc = Scanner(this)

  while (sc.hasNextLine()) {
    callback(sc.nextLine())
  }

  if (sc.ioException() != null) {
    throw sc.ioException()
  }

  this.close()
  sc.close()
}

/**
 * Iterate over the lines of a file.
 *
 * @param filename the name of the file
 * @param callback the callback called for each line
 */
fun forEachLine(filename: String, callback: (String) -> Unit) {
  FileInputStream(filename).forEachLine(callback)
}

/**
 * Count the lines of a file.
 *
 * @param filename the name of a file
 *
 * @return the number of lines of the file with the given [filename]
 */
fun getLinesCount(filename: String): Int {

  var count = 0

  val newLineByte = '\n'.toByte()
  val inputStream = FileInputStream(filename)
  val buffer = ByteArray(8192)
  var n: Int = inputStream.read(buffer)

  while (n > 0) {

    (0 until n).forEach { i -> if (buffer[i] == newLineByte) count++ }

    n = inputStream.read(buffer)
  }

  inputStream.close()

  return count
}
