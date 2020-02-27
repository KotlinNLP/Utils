FROM rabbitmq:3.8.2-alpine

#####
## Install Management Exchange plugin
#####

RUN wget -P /plugins https://github.com/rabbitmq/rabbitmq-management-exchange/releases/download/v3.8.0/rabbitmq_management_exchange-3.8.0.ez
RUN rabbitmq-plugins enable rabbitmq_management_exchange
