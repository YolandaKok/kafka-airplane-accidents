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

### Services Description

#### Producer Service
The producer service is reading a csv file, and it's producing JSON messages that are being written into
```raw-data-topic``` in a synchronous way.

#### Kafka Streams Service
The kafka streams service is reading data from the ```raw-data-topic``` and is transforming them by
using some rules. The result of that data cleaning process is being written into ```clean-data-topic```.
The kafka streams topology reads the data from the ```clean-data-topic``` and by using a priority queue is
selecting the N top elements for each year of the incoming data and writes the result into ```sliding-window-result```
topic.

#### Consumer Service
The consumer service reads data from the ```sliding-window-topic``` and stores them into a redis cache.
By calling the ```/results/export``` endpoint an export of the ```top5``` results is being created. The cache service 
is being used because the stream is unbounded, and we want to see the live results when we call the above endpoint.
