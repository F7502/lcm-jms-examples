# lcm-jms-examples

A collection of examples of using [Java Message Service (JMS)](http://download.oracle.com/otndocs/jcp/7195-jms-1.1-fr-spec-oth-JSpec/) for sending and receiving messages to/from a queue.

All examples can be run through the `main` method of the corresponding classes.

Please note:  All examples are based on JMS in combination with [ActiveMQ](http://activemq.apache.org/). Please have a look at [this](https://github.com/F7502/lcm-jms-helloworld) example project to see how JMS can be used together with JNDI to be more independent from any specific messaging middleware. This project also contains some documentation about setting up and running ActiveMQ.

You can use the ActiveMQ console to inspect queues and message and also to manually send messages to queues. The console is available at `http://localhost:8161/admin` (username/password = admin/admin). A graph view of the filling quantity of all queues is available at `http://localhost:8161/admin/queueGraph.jsp`. Please note that this view has to be refreshed manually (e.g. by pressing F5).

The `de.unistuttgart.iaas.lcm.jms.examples.QueueSpam` class sends 50 messages to the queue `Q2`. This is useful for queue monitoring using the graph view in the AtiveMQ console (otherwise, if only one queue is shown, it always looks "full" in the UI as the value range of the x axis is continuously adapted to the size of the fullest queue).

## Example A

Package: `de.unistuttgart.iaas.lcm.jms.examples.a`

This example demonstrates how to send a text message to a queue and how to receive a text message from a queue.

Running the `SimpleSend` class sends one text message to the queue `MyFirstQueue`. Running the `SimpleReceive` class retrieves one message from the same queue (if a message is available). You can modify lines 52 to 57 of the `SimpleReceive` class to play around with the different variants of receiving a message (blocking, blocking with timeout, non-blocking).

## Example B

Package: `de.unistuttgart.iaas.lcm.jms.examples.b`

This example demonstrates how to continuously send text messages to a queue and how to continuously receive text messages from a queue. Demonstrates synchronous receive ("pull") as well as asynchronous receive ("push").

Running the `LoopingSender` class sends one text message per second to the queue `Q1`. You can change the send interval by adapting the `waitingTime` constant (line 26).

Running the `LoopingConsumerSynchronous` class retrieves one message each two seconds from the queue `Q1` using the synchronous `receive` method. If no message is available, the receiver will wait until the next messages arrives (i.e. no timeout is used). You can modify lines 70 to 74 of the `LoopingConsumerSynchronous` class to play around with the different variants of receiving a message (blocking, blocking with timeout, non-blocking).

Running the `LoopingConsumerAsynchronous` class receives one message each two seconds from the queue `Q1` in an asynchronous way. For that, the class register a listener object at the queue which is then called whenever a message is available in the queue.

The classes of this example can be used to demonstrate load balancing and scaling. Try the following:
* Start the `LoopingSender`
* Q1 fills up (use the ActiveMQ console for monitoring it)
* Start a consumer (synchronous or asynchronous)
* Q1 still fills up, but slower (one message is sent each second, but only one message each two second gets processed)
* Start another consumer instance
* Q1 now keeps its size (sending and receiving messages now happens at the same rate)
* Start another consumer instance
* Q1 runs empty (now, more messages are processed than sent)

## Example C

Package: `de.unistuttgart.iaas.lcm.jms.examples.c`

This example demonstrates how to continuously send text messages to a queue and how to continuously receive text messages from a queue. Demonstrates the use of message properties and message filtering ("selective consumers").

Running the `LoopingSender` class sends one text message per second to the queue `Q1`. You can change the send interval by adapting the `waitingTime` constant (line 29). In addition, each message gets one boolean property (alternating set to true and false) and one integer property (starting at 1 and then increasing) assigned.

Running the `SelectiveConsumer_01` class retrieves one message per second from the queue `Q1`. The receiver defines a message selector expression so that it only receives messages where the boolean property is true.

Running the `SelectiveConsumer_02` class retrieves one message per second from the queue `Q1`. The receiver defines a message selector expression so that it only receives messages where the integer property is greater than 30.