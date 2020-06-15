/* Copyright 2020-present Simone Cangialosi. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

import com.kotlinnlp.utils.foldUp
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

/**
 *
 */
class BaseExtensionsSpec : Spek({

  describe("the Utils BaseExtensions") {

    context("Lists extensions") {

      context("foldUp()") {

        val l: List<List<Int>> = listOf(
          listOf(1, 2, 3, 4),
          listOf(5, 6, 7, 8)
        )
        val expected: List<List<Int>> = listOf(
          listOf(1, 5),
          listOf(2, 6),
          listOf(3, 7),
          listOf(4, 8)
        )

        it("should return the expected folded lists") {

          assertEquals(expected, l.foldUp())
        }
      }
    }
  }
})
