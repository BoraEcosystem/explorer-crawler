# BORA Shell Crawler

This module collects index data to use BORA Shell Blockchain data in the BORA Explorer.

### Prerequisite
#### Required development environment
##### Java 1.8
BORA Explorer is a tool written in Java. Therefore, you need to install the JDK on the machine you want to build.
- OpenJDK: https://openjdk.java.net/install/
- OracleJDK: https://www.oracle.com/technetwork/java/javase/downloads/index.html

##### MySQL6
To construct a list for Block and Transaction, you need to build an index database. 
To understand MySQL and how to install, see [MySQL Official Site](https://www.mysql.com/). 
Please see [here](https://github.com/BoraEcosystem/explorer-crawler/blob/master/src/main/resources/schema.sql) for scripts on related schemas.

##### Gradle
A tool for building this project, requires gradle 4.x or later. https://gradle.org/install/

#### Understanding Spring Framework, Ethereum and Web3J
The BORA Explorer is a solution using the Java based Spring Boot Framework. In this regard, you need a basic knowledge of Java and Spring Boot. The BORA Explorer runs on an Ethereum-based node and its related API leverages Web3J. If you want to understand the code, you need to have prior knowledge of Ethereum and Web3J.
- [Ethereum Official Site](https://ethereum.org/)
- [Web3J Official Site](https://web3j.readthedocs.io/en/latest/)

### Build

```sh
./gradlew clean build -x test
```

### Run BORA Crawler Application 
The BORA Crawler Application provides four options regards data collection. If no parameter is given, it starts with "Stream Mode" as a default.

#### Stream Mode
- Indexing blocks and transactions from the current block number at the time of starting the application.
```sh
java \
  -jar ./build/libs/bora-explorer-point-crawler-0.0.1-SNAPSHOT.jar \
  --stream
```
#### Catch-Up Mode
- Indexing blocks and transactions since the last block number that was stored when the application was stopped.
```sh
java \
  -jar ./build/libs/bora-explorer-point-crawler-0.0.1-SNAPSHOT.jar \
  --catch-up
```
#### Range-From Mode
- Indexing blocks and transactions after the specified block number
```sh
java \
  -jar ./build/libs/bora-explorer-point-crawler-0.0.1-SNAPSHOT.jar \
  --start=1096300
```
#### Range Mode
- Indexing blocks and transactions within the specified block number range
```sh
java \
  -jar ./build/libs/bora-explorer-point-crawler-0.0.1-SNAPSHOT.jar \
  --start=1096300 --end=1103000 
```
