package com.littlepay;

import java.util.Collections;
import java.util.List;

import com.littlepay.domain.Tap;
import com.littlepay.domain.Trip;

public class TapsParser {

    private static final String CSV_SPLIT_REGEX = "\\s*,\\s*";

    public static Tap StringToTapMapper(String line) {
        String[] strings = line.split(CSV_SPLIT_REGEX);
        return new Tap(strings[0], strings[1], strings[2],
                strings[3], strings[4], strings[5], strings[6]);
    }

    public static List<Trip> getTrips(List<Tap> taps, TripCalculator tripCalculator) {
        return Collections.emptyList();
    }
    
}
