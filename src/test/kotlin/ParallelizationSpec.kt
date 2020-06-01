/* Copyright 2020-present Simone Cangialosi. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

import com.kotlinnlp.utils.pmap
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.math.pow
import kotlin.test.assertEquals

/**
 *
 */
class ParallelizationSpec : Spek({

  describe("a pmap function") {

    val elements = 10.0.pow(5).toInt()

    context("parallelization = 1") {

      val res: List<Int> = List(elements) { it }.pmap(1) { it + 1 }
      val expected: List<Int> = List(elements) { it + 1 }

      assertEquals(expected, res)
    }

    context("parallelization = 2") {

      val res: List<Int> = List(elements) { it }.pmap(2) { it + 1 }
      val expected: List<Int> = List(elements) { it + 1 }

      assertEquals(expected, res)
    }

    context("parallelization = 4") {

      val res: List<Int> = List(elements) { it }.pmap(4) { it + 1 }
      val expected: List<Int> = List(elements) { it + 1 }

      assertEquals(expected, res)
    }
  }
})
