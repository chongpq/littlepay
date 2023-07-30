# Littlepay
Taps and trips calculator

## Run
Assuming you are executing on a POSIX box, Windows machines are very similar. Install [Java 17](http://www.oracle.com/technetwork/java/javase/downloads/index.html). Go to the littlepay project and run `./gradlew clean build` to build this project. This will download all dependencies and create the jar in `./app/build/libs` folder. 

To run this project type `java -jar ./app/build/libs/app-1.0.jar  <input-file-location>`. This will execute the program. There is an input file in the test resources folder so you could use the following command
```
java -jar ./app/build/libs/app-1.0.jar ./app/src/test/resources/taps1.csv
```
The output is fed to the screen like a standard UNIX command.

To execute the program as required please use the `./run.sh  <input-file-location> <output-file-location>`

## Testing
The results of testing can be viewed after the running `./gradlew clean build` by viewing `./app/build/reports/jacoco/test/html/index.html`

## Design
The major observation is that Completed Trip Costs can be represented by a 2d Array and the Incomplete Trip Costs can be represented by a normal array. They are currently in-memory arrays in CompletedTripCosts.java and IncompleteTripCosts.java. In a proper system they can be replaced by a SQL or noSQL service (if we need an immediate update), or by a S3 file and a redeploy depending on requirements i.e. if time is not a factor.

For the Taps Parser I assumed it was more important to evaluate taps as a pair if possible than to evaluate them as separate incomplete trips. I assumed the tap on/off might not be as reliable piece of data, so even if we receive 2 ON's, 2 OFF's or the reverse, OFF and then ON, the Taps Parser will try and make complete trip from them. I'm aware that it might be more lucrative for Littlepay if I did't make these assumptions.

The jar file works like a standard UNIX command line utility and it can be scripted in a standard bash script, as demo'ed in the `run.sh` file.

## Assumptions
* That the input file is well formed and is not missing data.
* OFF without a corresponding ON tap will result in an Incomplete trip and will be billed the largest amount.
* There isn't a time limit on cancelling a trip.
* There aren't time limits on the length of time spent on getting to a stop.
* That the machine this service is run on is on UTC time.
* That taps are equivalent if they have the same PAN, BusID, CompanyId.
* That the output files in the example given was wrong. I've fixed the dates for Started and Finished to be 2023 and the DurationSecs to be 300.
* That TapType ON or OFF are not important. We just need to see if 2 taps are the same in order to calculate the fare.