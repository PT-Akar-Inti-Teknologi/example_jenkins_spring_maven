# Spring Boot Jenkins Example

Sample project to demonstrate how to use Jenkins & SonarQube in Spring Boot.

## Table of Contents

- [General Information](#general-information)
- [Technologies Used](#technologies-used)
- [Setup](#setup)

## General Information

- This sample project uses Jenkins' built in tools to build and test a typical Spring Boot project
- Please see `Jenkinsfile` and `sonar-project.properties` as a reference

## Technologies Used

- Spring Boot 2.5.2
- JDK 8
- Spring Boot Test with Junit 5

## Setup

### Development

- `./mvnw clean verify`
- `./mvnw spring-boot:run`

### Test

- `./mvnw test`
