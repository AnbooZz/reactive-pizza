# Reactive Pizza (2022/01/01)

## Preface

This repository is the small sample of n-layers web api using play-scala framework for ordering food. The project uses reactive programing to implement with `Future` that is managed by Akka Actor System

**Domain entities**

|Entity|Class|Description|
|---|---|---|
|User||Customer must be register/login before make an order|
|Item|SizeableItem|Food have size S or M or L. Choose one size and quantity to add  to cart|
||NoSizeableItem|Food have no size. Only choose quantity to add cart|
||ComboItem|Combo contains many foods|
|Coupon|MoneyCoupon|Reduce money by directly or percent|
||GiftCoupon|Give one other food to attach order|
|Cart||Food&Coupon can be added|
|Order||Cart + Customer Info|

**APIs**

Default port at local: `localhost:9000`

Base path: `/api/v1`

||Name|Method|Path|
|---|-----|------|------|
|1|Login|POST|/user?action=login|
|2|Register|POST|/user?action=register|
|3|List all food|GET|/items|
|4|Get one food|GET|/items/:itemId|
|5|Get user's cart|GET|/cart?userId=:id|
|6|Add food to cart|POST|/cart?userId=:id|
|7|Update cart|PUT|/cart?userId=:id|

Detail at [here](src/main/resources/routes)

## Libs
```
- Mysql driver v8.0 
- Play framework v2.8.0
- Scala v2.13.7
- Sbt native package v1.9.7 for build docker image
- Play-flyway v7.17.0 for auto migration
- Play-slick v5.0.0 for working database
```

## Setup
- Install JDK 8 or JDK 11 is required
- Install Mysql server 8.0. View [file](src/main/resources/application.conf) to get database config information
- Install Sbt

## Running

|||
|--|--|
|1|Dev mode|`sbt run`|
|2|Production mode|`sbt 'runProd'` or `sbt start`|
|3|Debug mode|`sbt -jvm-debug 5005 run` && `attach process 5005` at InteIJ IDEA|
|4|Build docker file| `sbt docker:publishLocal`|

Open browser at: `http://localhost:9000` to migrate database

## Todo list 2022
- Impl front-end by using ReactJs, Angular, VueJs
- Impl back-end web APIs with DDD, CQRS & Event Source, Akka persistence, Akka cluster
- Impl microservices with Akka libs
- Impl back-end web APIs with spring framework. Compare performance Play-scala and Spring-java
- Impl by using Quarkus framework with GraalVM


