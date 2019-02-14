# Introduction

This project implements a game of three  server and a client. The server accepts simultaneous games but each
game can be played by 2 players.
The clients are designed to play automatically until the game finishes.

## Technical architecture

This project follows a classic client-server architecture. The clients use websockets to communicate with the server.

Both the server and the client were written using Java and Netty. Netty is a framework designed to build high performing network applications.
Some of the reasons to choose netty:

* Nonblocking-IO support
* Netty Channels, a socket abstraction that enables the development of modular network applications that are easy to write
  without sacrificing performance or maintainability.
* Support for native file descriptor monitoring such Epoll and Kqueue
* A high performance buffer api that enables the use of Off-Heap Memory and Zero copy.
* A set of utilities to handle the conversion of network bytes into application objects and vice-versa.

This application is divided into 3 modules:
* Client
  * All the code the client needs to play
* Server
  * Contains the code for server
* Common
  * Contains all the code common to both the server and the client


The use of NonBlocking IO enables high performance at lower memory consumption. This implementation
can handle tens of tens of thousands of clients at the same time with minimal memory and CPU usage.


# Building and running

## Requirements

* Modern Linux or OSX
* Java JDK 8

## How to build

    ./gradlew test bootJar


## How to run the client

    # Change the gameId to connect to a different game
    java -jar server/build/libs/client.jar --game.gameId=123

## How to run the server

    java -jar server/build/libs/server.jar


## Client and server configuration

The following configuration options are available:

* client
    * game.endpoint
        * The server endpoint
    * game.gameId
        * Game the client wants to join
* server
    * server.port
        * the port server is listening on

The application can be configured using any of the methods provided by spring boot.
https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html


# Testing and quality assurance

The test suite covers the most relevant parts of the code base.
Some parts of the codebase such as the Server boostrapping and configuration don't benefit form
unit testing. Instead integration tests would be more suitable.
The supplied test client can be used as an integration test suite to validate that the server is working as expected.

## How to run the test suit
    ./gradlew test jacocoTestReport


# Future improvements

* Handle misbehaved clients. The current implementation doesn't care if the message was delivered
and doesn't deal with slow clients. A misbehaved client could cause the system to run out of memory
* Improve client unit tests


# Testing the game

 1. start the server using: ```java -jar server/build/libs/server.jar```
 1. start client 1 using: ``` java -jar server/build/libs/client.jar --game.gameId=123```
 1. start client 2 using : ``` java -jar server/build/libs/client.jar --game.gameId=123```
 1. The console should output something similar to:

        pt.luissantos.gameofthree.client.Client  : Connecting to: ws://127.0.0.1:8080/websocket?game=123
        p.l.g.client.WebSocketClientHandler      : Connected!
        p.l.g.client.GameMessageHandler          : Starting game: 897
        p.l.g.client.GameMessageHandler          : Got: 299
        p.l.g.client.GameMessageHandler          : Playing: 100
        p.l.g.client.GameMessageHandler          : Got: 33
        p.l.g.client.GameMessageHandler          : Playing: 11
        p.l.g.client.GameMessageHandler          : Game is over: You win!

