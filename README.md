# Dagacube-Service
Dagacube Service Demo as per requirements.

## **1. Prerequisites**

1. Java runtime
2. Maven
3. Docker (optional)

## **2. Setup**
    
1. Clone the repo from github:
```
git clone https://github.com/lablouw/DagacubeService
```
2. Build the project:
```
mvn clean install
```

## **3. Running the service**
Start the service as a Spring Boot microservice:
```
java -jar target/dagacube-service-1.0.0-SNAPSHOT.jar
```
or in your favorite IDE.
The server runs on port 9000 by default.

Alternatively it can be run in a Docker container by executing the following from the project root directory:
```
mvn clean install
docker build --tag=dagacube-service:latest .
docker run -p9000:9000 dagacube-service
```

## *4. Using the service*

### *4.1. Creating a player*
**Url**: (POST) http://localhost:9000/dagacube-service/v1/dagacubeService

**Headers**: Content-Type: application/json

**Body**: (json data)

```
curl --request POST \
  --url http://localhost:9000/dagacube-service/v1/dagacubeService \
  --header 'content-type: application/json' \
  --data '{
  "username": "Lloyd",
  "balance": 100
}'
```

**Return statuses:**

*201 Created:* Player created (Id returned in response)

*409 Conflict:* Player with supplied username already exists


### *4.2. Getting a players balance*
**Url**: (GET) http://localhost:9000/dagacube-service/v1/dagacubeService/{playerId}/balance

```
curl --request GET \
  --url http://localhost:9000/dagacube-service/v1/dagacubeService/1/balance
```

**Return statuses:**

*200 OK:* Balance returned in response


### *4.3. Wager*
This request is idempotent on the transactionId header.

**Url**: (POST) http://localhost:9000/dagacube-service/v1/dagacubeService/wager

**Headers**: Content-Type: application/json

**Body**: (json data, promoCode field is optional, other fields are required) 

```
curl --request GET \
  --url http://localhost:9000/dagacube-service/v1/dagacubeService/wager \
  --header 'content-type: application/json' \
  --header 'TransactionId: txn1' \
  --data '{
  "playerId": "1",
  "amount": 50,
  "promoCode": "paper"
}'
```

**Return statuses:**

*200 OK:* Transaction performed


### *4.4. Win*
This request is idempotent on the transactionId header.

**Url**: (POST) http://localhost:9000/dagacube-service/v1/dagacubeService/win

**Headers**: Content-Type: application/json

**Body**: (json data, fields are required)

```
curl --request POST \
  --url http://localhost:9000/dagacube-service/v1/dagacubeService/win \
  --header 'content-type: application/json' \
  --header 'TransactionId: txn2' \
  --data '{
  "playerId": "1",
  "amount": 50
}'
```

**Return statuses:**

*200 OK:* Transaction performed


### *4.4. Get player's transaction history*
This request is idempotent on the transactionId header.

**Url**: (POST) http://localhost:9000/dagacube-service/v1/dagacubeService/transactions

**Headers**: Content-Type: application/json

**Body**: (json data, fields are required)

```
curl --request POST \
  --url http://localhost:9000/dagacube-service/v1/dagacubeService/transactions \
  --header 'content-type: application/json' \
  --data '{
  "username": "Lloyd",
  "password": "swordfish"
}'
```

**Return statuses:**

*200 OK:* Transaction returned in response

*401 Unauthorized:* Incorrect password provided




## **5. Documentation**
### **5.1. Yaml**
http://localhost:9000/dagacube-service/v2/api-docs
### **5.2. Testing**
**Swagger UI:** http://localhost:9000/dagacube-service/swagger-ui.html

**.rest file:** src/test/rest/test.rest (requires IntelliJ IDEA)

**H2 Database console:** http://localhost:9000/dagacube-service/h2

Driver Class:org.h2.Driver

JDBC URL:jdbc:h2:mem:testdb

User Name:sa

Password:<blank>