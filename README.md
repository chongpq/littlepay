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
The major observation is that Completed Trip Costs can be represented by a 2D array and the Incomplete Trip Costs can be represented by a normal array. They are currently in-memory arrays in CompletedTripCosts.java and IncompleteTripCosts.java. In a proper system they can be replaced by a SQL or noSQL service (if we need an immediate update), or by a S3 file and a redeploy, depending on requirements i.e. if time is not a factor.

For the Taps Parser I assumed it is sufficient to group and sort the input.csv into PAN, BusID and CompanyId groups and sort these groups by DateTimeUTC and TapType and store them in an Array. We then go over this array check if there is only one element in the array and calculate on Tap if there is or else we need to check whether we are doing a 2 Tap or a 1 Tap to calculate the trip fare. Trip fares are looked up in the 2D array for the 2 Tap calc and they can be looked up in the array for the 1 Tap calc.

Part of the no. of taps calulation is comparing if the taps belong to the same group. If the do then we use the the TapTypes and the result is list in the table below.
     
| tap.TapType | tap2.TapType | take 2 taps?
|-------------|--------------|--------------- 
| OFF         | _            | false
| ON          | OFF          | true
| ON          | ON           | false

The jar file works like a standard UNIX command line utility and it can be scripted in a standard bash script, as demo'ed in the `run.sh` file.

## Assumptions
* That the input file is well formed and is not missing data.
* OFF without a corresponding ON tap will result in an Incomplete trip and will be billed the largest amount.
* There isn't a time limit on cancelling a trip.
* There aren't time limits on the length of time spent on getting to a stop.
* That the machine this service is run on is on UTC time.
* That taps are equivalent if they have the same PAN, BusID, CompanyId.
* That the output files in the example given was wrong. I've fixed the dates for Started and Finished to be 2023 and the DurationSecs to be 300.

## Output csv
The `output.csv` can be found at `./app/src/test/resources/output.csv` and is the result of running the app against `./app/src/test/resources/taps1.csv` the required input file.