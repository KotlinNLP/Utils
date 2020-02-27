# RabbitMQ Client

A simple client that manages the connection to a RabbitMQ instance.

## Install RabbitMQ

The `RabbitMQClient` needs to communicate with RabbitMQ using the `Management Exchange plugin`.

A Dockerfile is provided to build a Docker image with the plugin already installed.

### Docker 

Build a RabbitMQ docker image and run it:

```
sudo docker build -t rabbit-kt .
sudo docker run -d -p 5672:5672 --name rabbit rabbit-kt
```
