package com.littlepay;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.littlepay.domain.CompletedTripCosts;
import com.littlepay.domain.IncompleteTripCosts;
import com.littlepay.domain.Tap;

public class App {

    public static void main(String[] args) {
        IncompleteTripCosts incompleteTripCosts = new IncompleteTripCosts();
        CompletedTripCosts completedTripCosts = new CompletedTripCosts();
        TripCalculator tripCalculator = new TripCalculator(incompleteTripCosts, completedTripCosts);

        List<Tap> taps;
        try (Stream<String> stream = Files.lines(Paths.get(args[0])).skip(1)) {
            //todo optional to remove dud Taps ?
            taps = stream.map(TapsParser::StringToTapMapper)
                .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println(String.format("Error reading file '%s'", args[0]));
            return;
        }
        System.out.println("Started, Finished, DurationSecs, FromStopId, " + 
            "ToStopId, ChargeAmount, CompanyId, BusID, PAN, Status");
        TapsParser.getTrips(taps, tripCalculator)
            .stream()
            .forEach(System.out::println);
    }
}
