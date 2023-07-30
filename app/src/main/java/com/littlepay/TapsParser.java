package com.littlepay;

import java.util.ArrayList;
import java.util.List;

import com.littlepay.domain.Constants;
import com.littlepay.domain.Tap;
import com.littlepay.domain.Trip;

public class TapsParser {

    private static final String CSV_SPLIT_REGEX = "\\s*,\\s*";

    public static Tap StringToTapMapper(String line) {
        String[] strings = line.split(CSV_SPLIT_REGEX);
        return new Tap(strings[0], strings[1], strings[2],
                strings[3], strings[4], strings[5], strings[6]);
    }

    public static String TripToStringMapper(Trip trip) {
        String started = trip.getStarted().format(Constants.FORMATTER);
        String finished = trip.getFinished() != null? trip.getFinished().format(Constants.FORMATTER) : "";
        String chargeAmount = Constants.DECIMAL_FORMAT.format(trip.getChargeAmount());
        String duration = trip.getDurationSec() != null ? trip.getDurationSec().toString() : "";
        String toStopId = trip.getToStopId() != null ? trip.getToStopId().toString() : "";

        return String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s", started, finished,
            duration, trip.getFromStopId(), toStopId, chargeAmount, trip.getCompanyId(),
            trip.getBusID(), trip.getPAN(), trip.getStatus());
    }

    /**
     * It is a parser deciscion to determine whether 2 taps are equivalent or not.
     *
     * Hence this function not being in the Tap class in the domain model.
     *
     * @param tap
     * @param tap2
     * @return
     */
    private static boolean tapsAreEquivalent(Tap tap, Tap tap2) {
        return tap.getPAN().equals(tap2.getPAN()) && tap.getBusId().equals(tap2.getBusId())
            && tap.getCompanyId().equals(tap2.getCompanyId());
    }

    public static List<Trip> getTrips(Tap[] taps, TripCalculator tripCalculator) {
        List<Trip> trips = new ArrayList<>();
        for (int index = 0; index < taps.length; index++) {
            if (index + 1 == taps.length) {
                //1 tap belongs to a person, take 1 tap off the array because
                //we are using 1 tap in the tripCalculation. This is the last remain tap.
                trips.add(tripCalculator.calculate(taps[index], null));
            } else {
                //2 taps being examined
                if (tapsAreEquivalent(taps[index], taps[index + 1])) {
                    //Both taps belong to the same person, take 2 taps off the array because 
                    //we are using both taps in the tripCalculation
                    trips.add(tripCalculator.calculate(taps[index], taps[index + 1]));
                    index++;
                } else {
                    //1 tap belongs to a person, take 1 tap off the array because
                    //we are using 1 tap in the tripCalculation
                    trips.add(tripCalculator.calculate(taps[index], null));
                }
            }
        }
        return trips;
    }
    
}
