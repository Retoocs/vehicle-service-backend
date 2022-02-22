# Netgrif Application Engine Backend Starter

This is a starter project for [Netgrif Application Engine](https://github.com/netgrif/application-engine)
to make easier to start implementing your own NAE based application.

It helps if you are familiar with [Spring Boot framework,](https://spring.io/) but it is not necessary to enjoy
possibilities of process driven application.

## Requirements

The Application engine has some requirements for runtime environment. The following table is summary of requirements to
run and use the engine:

| Name                                                   | Version | Description                                                     | Recommendation                                                         |
|--------------------------------------------------------|---------|-----------------------------------------------------------------|------------------------------------------------------------------------|
| [Java](https://openjdk.java.net/)                      | 11+     | Java Development Kit                                            | [OpenJDK 11](https://openjdk.java.net/install/)                        |
| [Redis](https://redis.io/)                             | 5+      | Key-value in-memory database used for user sessions and caching | [Redis 6.2.6](https://redis.io/download)                               |
| [MongoDB](https://www.mongodb.com/)                    | 4.4+    | Main document store database                                    | [MongoDB 4.4.11](https://docs.mongodb.com/v4.4/installation/)          |
| [Elasticsearch](https://www.elastic.co/elasticsearch/) | 7.10+   | Index database used for better application search               | [Elasticsearch 7.10.2](https://www.elastic.co/downloads/elasticsearch) |

If you are planning on developing docker container based solution you can use our [docker-compose](docker-compose.yml)
configuration to run all necessary databases to develop with NAE.

## Installation

This project can be used as a base to your NAE application. Before you start coding please consider doing following
steps to personalize project:

- Rename root java package
- Edit maven project attributes in pom.xml, mainly groupId and artifactId
- Generate security certificates for token encryption

#### Generate certificates

You should generate own certificates to encrypt token used by NAE.

```shell
$ cd src/main/resources/certificates && openssl genrsa -out keypair.pem 4096 && openssl rsa -in keypair.pem -pubout -out public.crt && openssl pkcs8 -topk8 -inform PEM -outform DER -nocrypt -in keypair.pem -out private.der && cd ../../../..
```

## Class Description

#### CustomActionDelegate

#### CustomRunner

#### CustomRunnerController

#### StarterApplication - Main class


