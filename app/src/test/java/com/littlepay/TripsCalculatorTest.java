package com.littlepay;

import org.junit.jupiter.api.Test;

import com.littlepay.domain.StopId;
import com.littlepay.domain.Tap;
import com.littlepay.domain.Trip;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class TripsCalculatorTest {

    @Test void basicTestTapToTripConversion() {
        TripsCalculator classUnderTest = new TripsCalculator();
        List<Tap> taps = new ArrayList<Tap>(2);
        taps.add(new Tap());
        taps.add(new Tap());
        
        Trip trip = classUnderTest.calculate(taps);

        assertEquals(LocalDateTime.of(2018,1,22,13,0,0), trip.getStarted());
        assertEquals(LocalDateTime.of(2018,1,22,13,5,0), trip.getFinished());
        assertEquals(900, trip.getDurationSec());
        assertEquals(StopId.STOP1, trip.getFromStopId());
        assertEquals(StopId.STOP2, trip.getToStopId());
        assertEquals("$3.25", trip.getChargeAmount());
        assertEquals("Company1", trip.getCompanyId());
        assertEquals("Bus37", trip.getBusID());
        assertEquals("5500005555555559,", trip.getPAN());
        assertEquals("COMPLETED", trip.getStatus());
    }

}