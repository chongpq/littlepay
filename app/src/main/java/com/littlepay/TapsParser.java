package com.littlepay;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import com.littlepay.domain.Constants;
import com.littlepay.domain.Tap;
import com.littlepay.domain.TapType;
import com.littlepay.domain.Trip;

public class TapsParser {

    private static final String CSV_SPLIT_REGEX = "\\s*,\\s*";

    static final Comparator<Tap> TAPS_GROUPING_COMPARATOR = Comparator.comparing(Tap::getPAN)
        .thenComparing(Tap::getBusId)
        .thenComparing(Tap::getCompanyId);
    static final Comparator<Tap> TAPS_GROUPING_SORTING_COMPARATOR = TAPS_GROUPING_COMPARATOR
        .thenComparing(Tap::getDateTimeUTC)
        .thenComparing(Tap::getTapType);
    
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

    private TripCalculator tripCalculator;

    public TapsParser(TripCalculator tripCalculator) {
        this.tripCalculator = tripCalculator;
    }
    
    /**
     * This is an important function in this solution. It is closely related to 
     * <pre>TAPS_GROUPING_SORTING_COMPARATOR</pre>.
     * 
     * <pre>TAPS_GROUPING_SORTING_COMPARATOR</pre> is used to sort the incoming
     * taps list, the most variable field is going to be DateTimeUTC. We use the 
     * <pre>TAPS_GROUPING_COMPARATOR</pre> to find out whether we need to compare
     * 2 adjacent TapTypes. When both Taps belong to the same group we need to compare 
     * TapTypes next to answer our question.  
     * 
     * The TapTypes and the result is list in the table below.
     * 
     * tap.TapType | tap2.TapType | result
     * ====================================
     * OFF         | _            | false
     * ON          | OFF          | true
     * ON          | ON           | false
     *
     * @param tap
     * @param tap2
     * @return
     */
    boolean isTakingTwoTaps(Tap tap, Tap tap2) {
        boolean isBothTapsInTheSameGroup = TAPS_GROUPING_COMPARATOR.compare(tap, tap2) == 0;
        boolean isTapTypeTable = (tap.getTapType().equals(TapType.ON) &&
            tap2.getTapType().equals(TapType.OFF));

        return isBothTapsInTheSameGroup && isTapTypeTable;
    }

    public List<Trip> getTrips(Stream<Tap> stream) {
        Tap[] taps = stream.sorted(TapsParser.TAPS_GROUPING_SORTING_COMPARATOR)
            .toArray(Tap[]::new);
        return getTrips(taps);
    }

    List<Trip> getTrips(Tap[] sortedTaps) {
        List<Trip> trips = new ArrayList<>();
        for (int index = 0; index < sortedTaps.length; index++) {
            if (index + 1 == sortedTaps.length) {
                //1 tap belongs to a person, take 1 tap off the array because
                //we are using 1 tap in the tripCalculation. This is the last remain tap.
                trips.add(tripCalculator.calculate(sortedTaps[index], null));
            } else {
                //2 taps being examined
                if (isTakingTwoTaps(sortedTaps[index], sortedTaps[index + 1])) {
                    //Both taps belong to the same person, take 2 taps off the array because 
                    //we are using both taps in the tripCalculation
                    trips.add(tripCalculator.calculate(sortedTaps[index], sortedTaps[index + 1]));
                    index++;
                } else {
                    //1 tap belongs to a person, take 1 tap off the array because
                    //we are using 1 tap in the tripCalculation
                    trips.add(tripCalculator.calculate(sortedTaps[index], null));
                }
            }
        }
        return trips;
    }
    
}
