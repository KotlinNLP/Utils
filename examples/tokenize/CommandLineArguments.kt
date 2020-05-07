/* Copyright 2020-present Simone Cangialosi. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * -----------------------------------------------------------------------------*/

package tokenize

import com.xenomachina.argparser.ArgParser

/**
 * The interpreter of command line arguments.
 *
 * @param args the array of command line arguments
 */
internal class CommandLineArguments(args: Array<String>) {

  /**
   * The parser of the string arguments.
   */
  private val parser = ArgParser(args)

  /**
   * The file path of the tokenization vocabulary.
   */
  val vocabularyPath: String by parser.storing(
    "-v",
    "--vocabulary",
    help="the file path of the tokenization vocabulary"
  )

  /**
   * Force parsing all arguments (only read ones are parsed by default).
   */
  init {
    this.parser.force()
  }
}
