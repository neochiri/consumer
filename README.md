# Application

Test application created with the purpose of testing a Kafka Consumer.

## How to run it?

There are 2 parts to this. One of them is the Java application itself and another one a small set of unit tests.

### Running Kafka broker
1. Have Docker installed
2. Run `docker run --name kafka apache/kafka:latest`

This will give you a basic Kafka broker running in your machine.

### Running Java application
1. Go to the root folder of the project with your terminal
2. Execute `./gradlew.bat runApplication -x test` if you are a Windows user or `./gradlew runApplication -x test` if you are Unix user
3. Application will start to run

### Running Tests
1. Go to the root folder of the project with your terminal
2. Execute `./gradlew.bat test --info` if you are a Windows user or `./gradlew test --info` if you are Unix user
3. Console will spit the result of the tests

## Considerations
- Both for the java application and the unit tests an `application.properties` file has been created. It contains basic properties such as the servers urls, group ID and deserializer classes.
- Should we want to test with a real Kafka broker we just need to change the url on the property `bootstrap.servers`
- A complete report of the tests can be found in `./consumer/build/reports/tests/test/classes/ConsumerTest.html`