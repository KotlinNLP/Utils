/* Copyright 2020-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

import com.kotlinnlp.utils.rabbitmq.RabbitMQClient

/**
 * Test the methods of the RabbitMQ client (connected to the default instance 'localhost:5672'):
 *  1. Declare a RabbitMQ queue
 *  2. List the existing queues
 *  3. Send a message to a queue
 *  4. Read a message from the queue
 */
fun main() {

  RabbitMQClient().use { rabbit ->

    val queueName = "Test queue"

    println("1. Declare queue '$queueName'")
    rabbit.declareQueue(queueName)

    println("2. Declared queues: '${rabbit.getQueuesNames().joinToString("', '")}'")

    println("3. Send message to '$queueName'")
    rabbit.sendMessage(queueName = queueName, message = "This is a message sent to the queue '$queueName'")

    print("4. Waiting to read the message from '$queueName'... ")
    Thread.sleep(500)
    println("received: " + rabbit.readMessage(queueName))
  }
}
