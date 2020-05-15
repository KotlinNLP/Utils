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

    val vocabulary = DictionarySet(
      listOf("Lorem", "ipsum", "dol", "##or", "sit", "amet", "cons", "##ecte", "##tur", ",", ".", "[SPECIAL_1]"))

    context("From text: default parameters") {

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

      tests.forEachIndexed { i, (text, expected) ->
        it("should return the expected tokenization of the text $i `$text`") {
          assertEquals(expected, tokenizer.tokenize(text))
        }
      }
    }

    context("From text: custom unknown token") {

      val tokenizer = WordPieceTokenizer(vocabulary, unknownToken = "XXX")
      val text = "Lorem ipsum or"

      it("should return the expected tokenization of the text `$text`") {
        assertEquals(listOf("Lorem", "ipsum", "XXX"), tokenizer.tokenize(text))
      }
    }

    context("From text: long tokens") {

      val tokenizer = WordPieceTokenizer(vocabulary)
      val text = "Lorem ipsum dolor sit amet, consectetur"

      it("should return the expected tokenization of the text `$text`") {
        assertEquals(
          listOf("Lorem", "ipsum", "dol", "##or", "sit", "amet", ",", "[UNK]"),
          tokenizer.tokenize(text, maxCharsPerToken = 5))
      }
    }

    context("From text: never split tokens") {

      val tokenizer = WordPieceTokenizer(vocabulary)
      val text = "Lorem [SPECIAL_1] ipsum dolor [SPECIAL_2] amet"

      it("should return the expected tokenization of the text `$text`") {
        assertEquals(
          listOf("Lorem", "[SPECIAL_1]", "ipsum", "dol", "##or", "[UNK]", "amet"),
          tokenizer.tokenize(text, neverSplit = setOf("[SPECIAL_1]", "[SPECIAL_2]")))
      }
    }

    context("From tokens: default parameters") {

      val tokenizer = WordPieceTokenizer(vocabulary)

      val tests = listOf(
        sequenceOf("Lorem", "ipsum") to listOf("Lorem", "ipsum"),
        sequenceOf("ipsum") to listOf("ipsum"),
        sequenceOf("or") to listOf("[UNK]"),
        sequenceOf("Lorem", "ipsum", "or") to listOf("Lorem", "ipsum", "[UNK]"),
        sequenceOf(" ", "Lorem ", "ipsum ", "or") to listOf("Lorem", "ipsum", "[UNK]"),
        sequenceOf("   ", "Lorem", "ipsum", "or") to listOf("Lorem", "ipsum", "[UNK]"),
        sequenceOf("Lorem", "ipsum", "dolor") to listOf("Lorem", "ipsum", "dol", "##or"),
        sequenceOf("Lorem", "ipsum", "dolor", " ") to listOf("Lorem", "ipsum", "dol", "##or"),
        sequenceOf("Lorem", "ipsum", "dolor", "   ") to listOf("Lorem", "ipsum", "dol", "##or"),
        sequenceOf(" ", "Lorem ", "ipsum ", "dolor") to listOf("Lorem", "ipsum", "dol", "##or"),
        sequenceOf("   ", "Lorem", "ipsum", "dolor") to listOf("Lorem", "ipsum", "dol", "##or"),
        sequenceOf("   ", "Lorem", "ipsum", "   ", "dolor") to listOf("Lorem", "ipsum", "dol", "##or"),
        sequenceOf("   ", "Lorem", "ipsum", "   ", "dolor", " ") to listOf("Lorem", "ipsum", "dol", "##or"),
        sequenceOf("   ", "Lorem", "ipsum", "   ", "dolor", "   ") to listOf("Lorem", "ipsum", "dol", "##or"),
        sequenceOf("Lorem", "ipsum", "dolor", "sit", "amet") to listOf("Lorem", "ipsum", "dol", "##or", "sit", "amet"),
        sequenceOf("Lorem", "ipsum", "dolor", "sit", "amet,") to listOf("Lorem", "ipsum", "dol", "##or", "sit", "[UNK]"),
        sequenceOf("Lorem", "ipsum.", "dolor", "sit", "amet,") to listOf("Lorem", "[UNK]", "dol", "##or", "sit", "[UNK]"),
        sequenceOf("Lorem", "ipsum...", "dolor", "sit", "amet,") to listOf("Lorem", "[UNK]", "dol", "##or", "sit", "[UNK]"),
        sequenceOf("Lorem", "ipsum", "..", "dolor", "sit", "amet,") to listOf("Lorem", "ipsum", "[UNK]", "dol", "##or", "sit", "[UNK]"),
        sequenceOf("Lorem", "ipsum",  " ..", "dolor", "sit", "amet,") to listOf("Lorem", "ipsum", "[UNK]", "dol", "##or", "sit", "[UNK]"),
        sequenceOf("Lorem", "ipsum", ".dolor", "sit", "amet,") to listOf("Lorem", "ipsum", "[UNK]", "sit", "[UNK]"),
        sequenceOf("Lorem", "ipsum", ".", "  dolor", "sit", "amet,") to listOf("Lorem", "ipsum", ".", "dol", "##or", "sit", "[UNK]"),
        sequenceOf("Lorem", "ipsum", ".", "  ", "dolor", "sit", "amet",  ",") to listOf("Lorem", "ipsum", ".", "dol", "##or", "sit", "amet", ","),
        sequenceOf("Lorem", "ipsum", ".", "dolor", "sit", "amet", " ,", "   ") to listOf("Lorem", "ipsum", ".", "dol", "##or", "sit", "amet", ","),
        sequenceOf("Lorem", ",  ipsum", "  dolor", "sit", "amet", "   ") to listOf("Lorem", "[UNK]", "dol", "##or", "sit", "amet"),
        sequenceOf("Lorem", "  ", "ip", "sum", " dolor", "sit", "amet   ") to listOf("Lorem", "[UNK]", "[UNK]", "dol", "##or", "sit", "amet"),
        sequenceOf("sit", "amet,", "consectetur") to listOf("sit", "[UNK]", "cons", "##ecte", "##tur"),
        sequenceOf("sit", "amet", ",", "consectetur") to listOf("sit", "amet", ",", "cons", "##ecte", "##tur"),
        sequenceOf("sit", "amet", ",", " consectetur") to listOf("sit", "amet", ",", "cons", "##ecte", "##tur"),
        sequenceOf("sit", "amet", ",", " consectetur ") to listOf("sit", "amet", ",", "cons", "##ecte", "##tur"),
        sequenceOf("sit", "amet", ",", " consectetur  ") to listOf("sit", "amet", ",", "cons", "##ecte", "##tur"),
        sequenceOf("  ", "sit", "amet", ",", ", consectetur  ") to listOf("sit", "amet", ",", "[UNK]")
      )

      tests.forEachIndexed { i, (tokens, expected) ->
        it("should return the expected word-pieces from the tokens #$i `${tokens.joinToString(" ")}`") {
          assertEquals(expected, tokenizer.tokenize(tokens))
        }
      }
    }

    context("From tokens: custom unknown token") {

      val tokenizer = WordPieceTokenizer(vocabulary, unknownToken = "XXX")
      val tokens = sequenceOf("Lorem", "ipsum", "or")

      it("should return the expected word-pieces from the tokens `${tokens.joinToString(" ")}`") {
        assertEquals(listOf("Lorem", "ipsum", "XXX"), tokenizer.tokenize(tokens))
      }
    }

    context("From tokens: long tokens") {

      val tokenizer = WordPieceTokenizer(vocabulary)
      val tokens = sequenceOf("Lorem", "ipsum", "dolor", "sit", "amet", ",", "consectetur")

      it("should return the expected word-pieces from the tokens `${tokens.joinToString(" ")}`") {
        assertEquals(
          listOf("Lorem", "ipsum", "dol", "##or", "sit", "amet", ",", "[UNK]"),
          tokenizer.tokenize(tokens, maxCharsPerToken = 5))
      }
    }

    context("Pieces to words") {

      val tokenizer = WordPieceTokenizer(vocabulary)

      val tests = listOf(
        listOf("Lorem", "ipsum", "dolor") to listOf("Lorem", "ipsum", "dolor"),
        listOf("Lorem", "ipsum", "dol", "##or") to listOf("Lorem", "ipsum", "dolor"),
        listOf("Lorem", "ipsum", "dol", "##or", "sit", "am", "##et") to listOf("Lorem", "ipsum", "dolor", "sit", "amet")
      )

      tests.forEach { (pieces, words) ->
        it("should return the expected reconstruction of the pieces: `${tests.joinToString("`, ")}`") {
          assertEquals(words, tokenizer.piecesToWords(pieces))
        }
      }
    }

    context("Group pieces") {

      val tokenizer = WordPieceTokenizer(vocabulary)

      val tests = listOf(
        listOf("Lorem", "ipsum", "dolor") to listOf(IntRange(0, 0), IntRange(1, 1), IntRange(2, 2)),
        listOf("Lorem", "ipsum", "dol", "##or") to listOf(IntRange(0, 0), IntRange(1, 1), IntRange(2, 3)),
        listOf("Lorem", "ipsum", "dol", "##or", "sit", "am", "##et") to
          listOf(IntRange(0, 0), IntRange(1, 1), IntRange(2, 3), IntRange(4, 4), IntRange(5, 6))
      )

      tests.forEach { (pieces, groups) ->
        it("should return the expected reconstruction of the pieces: `${tests.joinToString("`, ")}`") {
          assertEquals(groups, tokenizer.groupPieces(pieces))
        }
      }
    }
  }
})
