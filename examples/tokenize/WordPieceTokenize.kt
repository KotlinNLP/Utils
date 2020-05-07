/* Copyright 2020-present Simone Cangialosi. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package tokenize

import com.kotlinnlp.utils.WordPieceTokenizer
import com.kotlinnlp.utils.DictionarySet
import java.io.File

/**
 * Tokenize a text with the [WordPieceTokenizer].
 *
 * Launch with the '-h' option for help about the command line arguments.
 */
fun main(args: Array<String>) {

  val parsedArgs = CommandLineArguments(args)

  val tokenizer = WordPieceTokenizer(vocabulary = parsedArgs.vocabularyPath.let {
    println("Reading vocabulary from '$it'...")
    DictionarySet<String>().apply { File(it).forEachLine { line -> add(line) } }
  })

  while (true) {
    readInput()?.let {
      println("Tokenized text: ${tokenizer.tokenize(it).joinToString(" ")}")
    } ?: break
  }
}

/**
 * Read a text from the standard input.
 *
 * @return the string read or null if it was empty
 */
private fun readInput(): String? {

  print("\nInput text (empty to exit): ")

  return readLine()!!.trim().ifEmpty { null }
}
