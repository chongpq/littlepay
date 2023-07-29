package com.littlepay;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.littlepay.domain.CompletedTripCosts;
import com.littlepay.domain.IncompleteTripCosts;
import com.littlepay.domain.Tap;
import com.littlepay.domain.Trip;

public class TapsParserTest {

    @Test
    void cancelledTripWorks() {
        IncompleteTripCosts incompleteTripCosts = new IncompleteTripCosts();
        CompletedTripCosts completedTripCosts = new CompletedTripCosts();
        TripCalculator tripCalculator = new TripCalculator(incompleteTripCosts, completedTripCosts);
        List<Tap> taps = new ArrayList<>(2);
        // taps.add("1, 22-01-2023 13:00:00, ON, Stop1, Company1, Bus37, 5500005555555559");
        // taps.add("2, 22-01-2023 13:05:00, OFF, Stop2, Company1, Bus37, 5500005555555559");
    
        List<Trip> trips = TapsParser.getTrips(taps, tripCalculator);
    }
    
}
