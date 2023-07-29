package com.littlepay;

import java.math.BigDecimal;
import java.time.Duration;

import com.littlepay.domain.CompletedTripCosts;
import com.littlepay.domain.IncompleteTripCosts;
import com.littlepay.domain.Tap;
import com.littlepay.domain.Trip;

public class TripCalculator {

    private static final String CANCELLED = "CANCELLED";
    private static final String INCOMPLETED = "INCOMPLETED";

    private IncompleteTripCosts incompleteTripCosts;
    private CompletedTripCosts completedTripCosts;

    public TripCalculator(IncompleteTripCosts incompleteTripCosts, CompletedTripCosts completedTripCosts) {
        this.incompleteTripCosts = incompleteTripCosts;
        this.completedTripCosts = completedTripCosts;
    }

    public Trip calculate(Tap start, Tap nullableEnd) {
        if (nullableEnd != null ) {
            Long duration = Duration.between(start.getDateTimeUTC(), nullableEnd.getDateTimeUTC()).getSeconds();

            if (start.getStopId() == nullableEnd.getStopId()) {
                return new Trip(start.getDateTimeUTC(), nullableEnd.getDateTimeUTC(), duration, start.getStopId(),
                        nullableEnd.getStopId(), BigDecimal.ZERO, start.getCompanyId(),
                        start.getBusId(), start.getPAN(), CANCELLED);
            } else {
                return null;
            }
        } else {
            return new Trip(start.getDateTimeUTC(), null, null, start.getStopId(),
                    null, incompleteTripCosts.get(start.getStopId()), start.getCompanyId(),
                    start.getBusId(), start.getPAN(), INCOMPLETED);
        }
    }
    
}
