/* Copyright 2016-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

import com.kotlinnlp.utils.progressindicator.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals

/**
 *
 */
class ProgressIndicatorSpec: Spek({

  describe("a ProgressIndicatorBar") {

    describe("total = 10, barLength = default (50)") {

      val outputStream = ByteArrayOutputStream()
      val progress = ProgressIndicatorBar(total = 10, outputStream = outputStream)

      context("first tick") {

        progress.tick()

        it("should print the expected string") {
          assertEquals(
            "\r|█████                                             | 10%",
            String(outputStream.toByteArray()))
        }
      }
    }

    describe("total = 10, barLength = 20") {

      context("first tick") {

        val outputStream = ByteArrayOutputStream()
        val progress = ProgressIndicatorBar(total = 10, outputStream = outputStream, barLength = 20)

        progress.tick()

        it("should print the expected string") {
          assertEquals("\r|██                  | 10%", String(outputStream.toByteArray()))
        }
      }

      context("second tick") {

        val outputStream = ByteArrayOutputStream()
        val progress = ProgressIndicatorBar(total = 10, outputStream = outputStream, barLength = 20)

        progress.tick()
        progress.tick()

        it("should print the expected string") {
          assertEquals(
            "\r|██                  | 10%" +
              "\r|████                | 20%",
            String(outputStream.toByteArray()))
        }
      }
    }
  }

  describe("a ProgressIndicatorPercentage") {

    context("first tick") {

      val outputStream = ByteArrayOutputStream()
      val progress = ProgressIndicatorPercentage(total = 10, outputStream = outputStream)

      progress.tick()

      it("should print the expected string") {
        assertEquals("\r[10%]", String(outputStream.toByteArray()))
      }
    }

    context("last tick") {

      val outputStream = ByteArrayOutputStream()
      val progress = ProgressIndicatorPercentage(total = 10, outputStream = outputStream)

      repeat(10) { progress.tick() }

      it("should print the expected string") {
        assertEquals(
          "\r[10%]" +
            "\r[20%]" +
            "\r[30%]" +
            "\r[40%]" +
            "\r[50%]" +
            "\r[60%]" +
            "\r[70%]" +
            "\r[80%]" +
            "\r[90%]" +
            "\r[100%]\n",
          String(outputStream.toByteArray()))
      }
    }
  }

  describe("a ProgressIndicatorIcon") {

    context("first tick") {

      val outputStream = ByteArrayOutputStream()
      val progress = ProgressIndicatorIcon(total = 10, outputStream = outputStream)

      progress.tick()

      it("should print the expected string") {
        assertEquals("\r-", String(outputStream.toByteArray()))
      }
    }

    context("second tick") {

      val outputStream = ByteArrayOutputStream()
      val progress = ProgressIndicatorIcon(total = 10, outputStream = outputStream)

      progress.tick()
      progress.tick()

      it("should print the expected string") {
        assertEquals(
          "\r-" +
            "\r\\",
          String(outputStream.toByteArray()))
      }
    }

    context("last tick") {

      val outputStream = ByteArrayOutputStream()
      val progress = ProgressIndicatorIcon(total = 10, outputStream = outputStream)

      repeat(10) { progress.tick() }

      it("should print the expected string") {
        assertEquals(
          "\r-" +
            "\r\\" +
            "\r|" +
            "\r/" +
            "\r-" +
            "\r\\" +
            "\r|" +
            "\r/" +
            "\r-" +
            "\r\\\n"
          , String(outputStream.toByteArray()))
      }
    }
  }
})
