/* Copyright 2020-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.utils.rabbitmq

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.rabbitmq.client.*
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream
import java.io.Closeable

/**
 * A simple client that manages the connection to a RabbitMQ instance.
 *
 * @param host the RabbitMQ instance host
 * @param port the RabbitMQ instance port
 * @param username the RabbitMQ instance username
 * @param password the RabbitMQ instance password
 */
class RabbitMQClient(
  host: String = "localhost",
  port: Int = 5672,
  username: String = "guest",
  password: String = "guest"
) : Closeable {

  /**
   * The connection to Rabbit MQ.
   */
  private val connection = ConnectionFactory().apply {
    this.host = host
    this.port = port
    this.username = username
    this.password = password
  }.newConnection()

  /**
   * @return the list of existing queues names
   */
  fun getQueuesNames(): List<String> =

    this.connection.createChannel().use { channel ->

      val replyQueue: String = channel.queueDeclare().queue
      channel.exchangeDeclare("mgmt", "x-management")

      val props = MessageProperties.BASIC.builder().type("GET").replyTo(replyQueue).build()
      channel.basicPublish("mgmt", "/queues?columns=name", props, byteArrayOf())

      val res: ByteArray = channel.basicGet(replyQueue, true).body
      val jsonRes: JsonArray<*> = Klaxon().parseJsonArray(ByteInputStream(res, res.size).reader())

      jsonRes.mapNotNull {
        (it as JsonObject).string("name")!!.let { name ->
          if (!name.startsWith("amq")) name else null
        }
      }
    }

  /**
   * Declare a queue with a name and a deliver callback.
   *
   * @param name the queue name
   * @param purgeIfExisting if true (the default) and the queue already exists it will be purged (all messages deleted)
   * @param deliverCallback the deliver callback called when a message is sent to the queue (null to manage
   */
  fun declareQueue(name: String,
                   purgeIfExisting: Boolean = true,
                   deliverCallback: ((message: String) -> Unit)? = null) {

    this.connection.createChannel().use {

      it.queueDeclare(name, false, false, false, null)

      if (purgeIfExisting) it.queuePurge(name)

      if (deliverCallback != null)
        it.basicConsume(name, true, { _, delivery -> deliverCallback(String(delivery.body)) }, { _ -> })
    }
  }

  /**
   * Read a message from a queue.
   *
   * @param queueName the queue name
   *
   * @return the message read or null if there is no message has been retrieved
   */
  fun readMessage(queueName: String): String? =
    this.connection.createChannel().use {
      it.basicGet(queueName, true)?.let { res -> String(res.body) }
    }

  /**
   * Send a message to a queue.
   *
   * @param queueName the queue name
   * @param message the message to send
   */
  fun sendMessage(queueName: String, message: String) {

    this.connection.createChannel().use {
      it.basicPublish("", queueName, null, message.toByteArray())
    }
  }

  /**
   * Close the RabbitMQ connection.
   */
  override fun close() {
    this.connection.close()
  }
}
