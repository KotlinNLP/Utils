/* Copyright 2020-present Simone Cangialosi. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.utils

import java.lang.RuntimeException

/**
 * The operating system type.
 */
enum class OS { Linux, Windows, MacOS }

/**
 * @return the current operating system type
 */
fun getOS(): OS {

  val os: String = System.getProperty("os.name").toLowerCase()

  return when {
    os.contains("nix") || os.contains("aix") || os.contains("nux") -> OS.Linux
    os.contains("win") -> OS.Windows
    os.contains("osx") -> OS.MacOS
    else -> throw RuntimeException("Cannot determine the operating system")
  }
}
