# Littlepay
taps and trips calculator

## Run
Assuming you are executing on a POSIX box, Windows machines are very similar. Install [Java 17](http://www.oracle.com/technetwork/java/javase/downloads/index.html). Go to the littlepay project and run `./gradlew clean build` to build this project. This will download all dependencies and create the jar in `./app/build/libs` folder. 

To run this project type `java -jar ./app/build/libs/app-1.0.jar  <file-location>`. This will execute the program. There is an input file in the test resources folder so you could use the following command
```
java -jar ./app/build/libs/app-1.0.jar ./app/src/test/resources/taps1.csv
```
The output is feed to the screen like a standard UNIX command.

To execute the program as required please use the `./run.sh  <input-file-location> <output-file-location>`

## Testing
The results of testing can be viewed after the running `./gradlew clean build` by viewing ./app/build/reports/jacoco/test/html/index.html

## Design
The major observation is that Completed Trip Costs can be represented by a 2d Array and the Incomplete Trip Costs can be represented by a normal array. They are currently a in memory arrays in CompletedTripCosts.java and IncompleteTripCosts.java. In a proper systems they can be represented in a SQL or noSQL system if we need to immediately update cost or by S3 file in which we could trigger a read if its update is only needed over a period of time.

For the Taps Parser I assume it was more important to evaluate taps as a pair if possible than to evaluate them as separate incomplete trips because I thought the tap on/off might not be as reliable piece of data. So even if we receive two ON's, OFF's or reverse OFF and the ON the Taps Parser will try and make complete trip from them. I'm aware that it might be more lucrative for littlepay if I didn't do this.

The jar file works like a standard UNIX cmd line utility and it can be scripted in a standard bash script, as demo'ed in the `run.sh` file.

## Assumptions
* OFF without a corresponding ON tap with result in an Incomplete trip and will be billed the largest amount.
* there isn't a time limit on cancelling a trip.
* there aren't time limits on the length of time spent on getting to a stop.
* that the machine this service is run on is on UTC time.
* that taps are equivalent if they have the same PAN, BusID, CompanyId.
* that the output files in the example given where wrong. I've fixed the dates for Started and Finished to be 2023 and the DurationSecs to be 300.
* that TapType ON or OFF are not important. We just need to see if 2 taps are the same in order to calculate the fare.