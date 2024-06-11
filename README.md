# kafka-airplane-accidents

### Services

The project consists of 3 services:

* kafka-airplane-accidents-producer
* kafka-airplane-accidents-streams
* kafka-airplane-accidents-consumer

### How to run docker-compose ?
```commandline
docker-compose -f docker/docker-compose.yml up -d
```

### How to build the project ?
```commandline
mvn clean install
```

### How to start the services ?
```commandline
# Producer
java -jar kafka-airplane-accidents-producer/target/kafka-airplane-accidents-producer-0.0.1-SNAPSHOT.jar 
# Consumer
java -jar kafka-airplane-accidents-consumer/target/kafka-airplane-accidents-consumer-0.0.1-SNAPSHOT.jar
# Streams
java -jar kafka-airplane-accidents-streams/target/kafka-airplane-accidents-streams-0.0.1-SNAPSHOT.jar
```

### How to get the results using curl ?
In order to modify the path for the export change the application.yml file of the consumer service:
```commandline
csv:
  export:
    file:
      path: /Users/yolanda/topN_accidents.csv
```

Trigger the export endpoint:
```commandline
curl -HGET localhost:9090/results/export
```

