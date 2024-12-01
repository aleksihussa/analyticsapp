# Analyticsapp by JavaTheHutt

## Tools

Let's use java 21 and maven version in this range: 3.6.3 ... 3.9.9

## Documentation

Documentation can be found in file java_the_hutt_documentation.pdf.
If the images in the documentation cannot be seen well, better versions are provided as separate .png files. 

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

### Run the Application from the Docker Image using VNC

To run the application from the Docker image and expose the VNC server on port 5900, use:

```sh
docker run -p 5900:5900 --rm analyticsapp:latest
```
