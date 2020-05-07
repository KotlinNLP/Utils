/* Copyright 2020-present Simone Cangialosi. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

import com.kotlinnlp.utils.DictionarySet
import com.kotlinnlp.utils.WordPieceTokenizer
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

/**
 *
 */
class WordPieceTokenizerSpec : Spek({

  describe("a WordPieceTokenizer") {

    val vocabulary =
      DictionarySet(listOf("Lorem", "ipsum", "dol", "##or", "sit", "amet", "cons", "##ecte", "##tur", ",", "."))

    context("Default parameters") {

      val tokenizer = WordPieceTokenizer(vocabulary)

      val tests = listOf(
        "Lorem ipsum" to listOf("Lorem", "ipsum"),
        "ipsum" to listOf("ipsum"),
        "or" to listOf("[UNK]"),
        "Lorem ipsum or" to listOf("Lorem", "ipsum", "[UNK]"),
        " Lorem ipsum or" to listOf("Lorem", "ipsum", "[UNK]"),
        "   Lorem ipsum or" to listOf("Lorem", "ipsum", "[UNK]"),
        "Lorem ipsum dolor" to listOf("Lorem", "ipsum", "dol", "##or"),
        "Lorem ipsum dolor " to listOf("Lorem", "ipsum", "dol", "##or"),
        "Lorem ipsum dolor   " to listOf("Lorem", "ipsum", "dol", "##or"),
        " Lorem ipsum dolor" to listOf("Lorem", "ipsum", "dol", "##or"),
        "   Lorem ipsum dolor" to listOf("Lorem", "ipsum", "dol", "##or"),
        "   Lorem ipsum   dolor" to listOf("Lorem", "ipsum", "dol", "##or"),
        "   Lorem ipsum   dolor " to listOf("Lorem", "ipsum", "dol", "##or"),
        "   Lorem ipsum   dolor   " to listOf("Lorem", "ipsum", "dol", "##or"),
        "Lorem ipsum dolor sit amet" to listOf("Lorem", "ipsum", "dol", "##or", "sit", "amet"),
        "Lorem ipsum dolor sit amet," to listOf("Lorem", "ipsum", "dol", "##or", "sit", "amet", ","),
        "Lorem ipsum. dolor sit amet," to listOf("Lorem", "ipsum", ".", "dol", "##or", "sit", "amet", ","),
        "Lorem ipsum... dolor sit amet," to listOf("Lorem", "ipsum", ".", ".", ".", "dol", "##or", "sit", "amet", ","),
        "Lorem ipsum .. dolor sit amet," to listOf("Lorem", "ipsum", ".", ".", "dol", "##or", "sit", "amet", ","),
        "Lorem ipsum   .. dolor sit amet," to listOf("Lorem", "ipsum", ".", ".", "dol", "##or", "sit", "amet", ","),
        "Lorem ipsum .dolor sit amet," to listOf("Lorem", "ipsum", ".", "dol", "##or", "sit", "amet", ","),
        "Lorem ipsum .   dolor sit amet," to listOf("Lorem", "ipsum", ".", "dol", "##or", "sit", "amet", ","),
        "Lorem ipsum .   dolor sit amet  ," to listOf("Lorem", "ipsum", ".", "dol", "##or", "sit", "amet", ","),
        "Lorem ipsum .   dolor sit amet  ,   " to listOf("Lorem", "ipsum", ".", "dol", "##or", "sit", "amet", ","),
        "Lorem   ipsum  dolor sit amet   " to listOf("Lorem", "ipsum", "dol", "##or", "sit", "amet"),
        "Lorem   ip sum  dolor sit amet   " to listOf("Lorem", "[UNK]", "[UNK]", "dol", "##or", "sit", "amet"),
        "sit amet, consectetur" to listOf("sit", "amet", ",", "cons", "##ecte", "##tur"),
        "sit amet , consectetur" to listOf("sit", "amet", ",", "cons", "##ecte", "##tur"),
        "sit amet ,  consectetur" to listOf("sit", "amet", ",", "cons", "##ecte", "##tur"),
        "sit amet ,  consectetur " to listOf("sit", "amet", ",", "cons", "##ecte", "##tur"),
        "sit amet ,  consectetur  " to listOf("sit", "amet", ",", "cons", "##ecte", "##tur"),
        "  sit amet ,  consectetur  " to listOf("sit", "amet", ",", "cons", "##ecte", "##tur")
      )

      tests.forEach { (text, expected) ->
        it("should return the expected tokenization of the text `$text`") {
          assertEquals(expected, tokenizer.tokenize(text))
        }
      }
    }

    context("Custom unknown token") {

      val tokenizer = WordPieceTokenizer(vocabulary, unknownToken = "XXX")
      val text = "Lorem ipsum or"

      it("should return the expected tokenization of the text `$text`") {
        assertEquals(listOf("Lorem", "ipsum", "XXX"), tokenizer.tokenize(text))
      }
    }

    context("Long tokens") {

      val tokenizer = WordPieceTokenizer(vocabulary)
      val text = "Lorem ipsum dolor sit amet, consectetur"

      it("should return the expected tokenization of the text `$text`") {
        assertEquals(
          listOf("Lorem", "ipsum", "dol", "##or", "sit", "amet", ",", "[UNK]"),
          tokenizer.tokenize(text, maxCharsPerToken = 5))
      }
    }
  }
})
