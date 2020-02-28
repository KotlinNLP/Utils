/* Copyright 2020-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.utils

import com.beust.klaxon.JsonObject
import com.beust.klaxon.json
import kotlin.reflect.full.declaredMemberProperties

/**
 * An object that can be converted to a [JsonObject] associating its member properties to keys with the same name.
 */
interface JSONSerializable {

  /**
   * @return the JSON representation of this object
   */
  fun toJSON(): JsonObject {

    val self = this

    return json {
      obj(*self::class.declaredMemberProperties.map { it.name to it.getter.call(self).toJSON() }.toTypedArray())
    }
  }

  /**
   * @return the JSON representation of this object
   */
  private fun Any?.toJSON(): Any? = when (val self = this) {
    is List<*> -> json { array(self.map { it.toJSON() }) }
    is JSONSerializable -> self.toJSON()
    else -> self
  }
}
