package com.littlepay;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.littlepay.domain.CompletedTripCosts;
import com.littlepay.domain.IncompleteTripCosts;
import com.littlepay.domain.Tap;

public class App {

    private static final String OUTPUT_FILE_HEADER = "Started, Finished, DurationSecs, FromStopId, ToStopId,"
        + " ChargeAmount, CompanyId, BusID, PAN, Status";
    private static final String INPUT_FILE_HEADER = "ID, DateTimeUTC, TapType, StopId, CompanyId, BusID, PAN";

    public static void main(String[] args) {
        IncompleteTripCosts incompleteTripCosts = new IncompleteTripCosts();
        CompletedTripCosts completedTripCosts = new CompletedTripCosts();
        TripCalculator tripCalculator = new TripCalculator(incompleteTripCosts, completedTripCosts);
        TapsParser tapsParser = new TapsParser(tripCalculator);

        try (Stream<String> stream = Files.lines(Paths.get(args[0]))) {
            //todo use optional to remove dud Taps ?
            Stream<Tap> taps = stream.filter(
                 s -> !s.equals(INPUT_FILE_HEADER))
                .map(TapsParser::StringToTapMapper);

            System.out.println(OUTPUT_FILE_HEADER);
            tapsParser.getTrips(taps)
                .stream()
                .map(TapsParser::TripToStringMapper)
                .forEach(System.out::println);
        } catch (IOException e) {
            System.err.println(String.format("Error reading file '%s'", args[0]));
            return;
        }
    }
}