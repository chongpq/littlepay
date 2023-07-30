# Littlepay
taps and trips calculator

## Run
Assuming you are executing on a POSIX box, Windows machines are very similar. Install [Java 17](http://www.oracle.com/technetwork/java/javase/downloads/index.html). Go to the littlepay project and run `./gradlew clean build` to build this project. This will download all dependencies and create the jar in `./app/build/libs` folder. To run this project type `java -jar ./app/build/libs/app-1.0.jar  <file-location>`. This will execute the program. There is an input file in the test resources folder so you could use the following command
```
java -jar ./app/build/libs/app-1.0.jar ./app/src/test/resources/taps1.csv
```

### Testing
The results of testing can be viewed after the running `./gradlew clean build` by viewing ./app/build/reports/jacoco/test/html/index.html

## Assumptions
* OFF without a corresponding ON tap with result in an Incomplete trip and will be billed the largest amount
* there isn't a time limit on cancelling a trip
* there aren't time limits on the length of time spent on getting to a stop
* that the machine this service is run on is on UTC time
* that taps are equivalent if they have the same PAN, BusID, CompanyId
* that the output files in the example given where wrong. I've fixed the dates for Started and Finished to be 2023 and the DurationSecs to be 300