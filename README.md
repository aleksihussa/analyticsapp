# Analyticsapp by JavaTheHutt

## Tools

Let's use java 21 and maven version in this range: 3.6.3 ... 3.9.9

## Commands

#### Running Formatting Plugin and Tests

To run the formatting plugin and tests, use the following command:
`mvn verify`

### Fixing Formatting Errors

To fix formatting-related errors, run:

```sh
mvn spotless:apply
```

### Building the Project

To build the entire project, use:

```sh
mvn clean install
```

### Running the Application from the Command Line

To run the application from the root directory, use:

```sh
mvn -pl ui exec:java -Dexec.mainClass="com.javathehutt.Main"
```

### Building the Docker Image

To build the Docker image, use:

```sh
docker build -t analyticsapp:latest .
```
