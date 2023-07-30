package com.littlepay;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.littlepay.domain.CompletedTripCosts;
import com.littlepay.domain.IncompleteTripCosts;
import com.littlepay.domain.StopId;
import com.littlepay.domain.Tap;
import com.littlepay.domain.Trip;

public class TapsParserTest {

    private TripCalculator tripCalculator;

    @BeforeEach
    void setup() {
        IncompleteTripCosts incompleteTripCosts = new IncompleteTripCosts();
        CompletedTripCosts completedTripCosts = new CompletedTripCosts();
        this.tripCalculator = new TripCalculator(incompleteTripCosts, completedTripCosts);
    }

    @Test
    void emptyTapsArrayWorks() {
        Tap[] taps = {};

        List<Trip> trips = TapsParser.getTrips(taps, tripCalculator);

        assertEquals(Collections.emptyList(), trips);
    }

    @Test
    void oneTapsArrayWorks() {
        Tap tap = new Tap("1", "22-01-2023 13:00:00", "ON", "Stop1", "Company1", "Bus37", "5500005555555559");

        Tap[] taps = { tap };

        List<Trip> trips = TapsParser.getTrips(taps, tripCalculator);

        assertEquals(1, trips.size());
        Trip trip = trips.get(0);

        assertEquals(LocalDateTime.of(2023,1,22,13,0,0), trip.getStarted());
        assertNull(trip.getFinished());
        assertNull(trip.getDurationSec());
        assertEquals(StopId.Stop1, trip.getFromStopId());
        assertNull(trip.getToStopId());
        assertEquals(new BigDecimal(7.30), trip.getChargeAmount());
        assertEquals("Company1", trip.getCompanyId());
        assertEquals("Bus37", trip.getBusID());
        assertEquals("5500005555555559", trip.getPAN());
        assertEquals("INCOMPLETED", trip.getStatus());
    }

    @Test
    void twoTapsArrayWorks() {
        Tap tap = new Tap("1", "22-01-2023 13:00:00", "ON", "Stop1", "Company1", "Bus37", "5500005555555559");
        Tap tap2 = new Tap("2", "22-01-2023 13:30:00", "OFF", "Stop3", "Company1", "Bus37", "5500005555555559");

        Tap[] taps = { tap, tap2 };
    
        List<Trip> trips = TapsParser.getTrips(taps, tripCalculator);

        assertEquals(1, trips.size());
        Trip trip = trips.get(0);

        assertEquals(LocalDateTime.of(2023,1,22,13,0,0), trip.getStarted());
        assertEquals(LocalDateTime.of(2023,1,22,13,30,0), trip.getFinished());
        assertEquals(1800l, trip.getDurationSec());
        assertEquals(StopId.Stop1, trip.getFromStopId());
        assertEquals(StopId.Stop3, trip.getToStopId());
        assertEquals(new BigDecimal(7.30), trip.getChargeAmount());
        assertEquals("Company1", trip.getCompanyId());
        assertEquals("Bus37", trip.getBusID());
        assertEquals("5500005555555559", trip.getPAN());
        assertEquals("COMPLETED", trip.getStatus());
    }

    @Test
    void threeTapsArrayWorks() {
        Tap tap = new Tap("1", "22-01-2023 13:00:00", "ON", "Stop1", "Company1", "Bus37", "5500005555555559");
        Tap tap2 = new Tap("2", "22-01-2023 13:30:00", "OFF", "Stop3", "Company1", "Bus37", "5500005555555559");
        Tap tap3 = new Tap("3", "22-01-2023 13:00:00", "ON", "Stop1", "Company1", "Bus37", "4111111111111111");

        Tap[] taps = { tap, tap2, tap3};

        List<Trip> trips = TapsParser.getTrips(taps, tripCalculator);

        assertEquals(2, trips.size());
        Trip trip = trips.get(0);

        assertEquals(LocalDateTime.of(2023,1,22,13,0,0), trip.getStarted());
        assertEquals(LocalDateTime.of(2023,1,22,13,30,0), trip.getFinished());
        assertEquals(1800l, trip.getDurationSec());
        assertEquals(StopId.Stop1, trip.getFromStopId());
        assertEquals(StopId.Stop3, trip.getToStopId());
        assertEquals(new BigDecimal(7.30), trip.getChargeAmount());
        assertEquals("Company1", trip.getCompanyId());
        assertEquals("Bus37", trip.getBusID());
        assertEquals("5500005555555559", trip.getPAN());
        assertEquals("COMPLETED", trip.getStatus());

        trip = trips.get(1);
        assertEquals(LocalDateTime.of(2023,1,22,13,0,0), trip.getStarted());
        assertNull(trip.getFinished());
        assertNull(trip.getDurationSec());
        assertEquals(StopId.Stop1, trip.getFromStopId());
        assertNull(trip.getToStopId());
        assertEquals(new BigDecimal(7.30), trip.getChargeAmount());
        assertEquals("Company1", trip.getCompanyId());
        assertEquals("Bus37", trip.getBusID());
        assertEquals("4111111111111111", trip.getPAN());
        assertEquals("INCOMPLETED", trip.getStatus());
    }

    @Test
    void threeTapsDifferentTripsOrderArrayWorks() {
        Tap tap = new Tap("1", "22-01-2023 13:00:00", "ON", "Stop1", "Company1", "Bus37", "5500005555555559");
        Tap tap2 = new Tap("2", "22-01-2023 13:30:00", "OFF", "Stop3", "Company1", "Bus37", "5500005555555559");
        Tap tap3 = new Tap("3", "22-01-2023 13:00:00", "ON", "Stop1", "Company1", "Bus37", "4111111111111111");

        Tap[] taps = { tap3, tap, tap2 };
    
        List<Trip> trips = TapsParser.getTrips(taps, tripCalculator);

        assertEquals(2, trips.size());
        Trip trip = trips.get(0);

        assertEquals(LocalDateTime.of(2023,1,22,13,0,0), trip.getStarted());
        assertNull(trip.getFinished());
        assertNull(trip.getDurationSec());
        assertEquals(StopId.Stop1, trip.getFromStopId());
        assertNull(trip.getToStopId());
        assertEquals(new BigDecimal(7.30), trip.getChargeAmount());
        assertEquals("Company1", trip.getCompanyId());
        assertEquals("Bus37", trip.getBusID());
        assertEquals("4111111111111111", trip.getPAN());
        assertEquals("INCOMPLETED", trip.getStatus());

        trip = trips.get(1);

        assertEquals(LocalDateTime.of(2023,1,22,13,0,0), trip.getStarted());
        assertEquals(LocalDateTime.of(2023,1,22,13,30,0), trip.getFinished());
        assertEquals(1800l, trip.getDurationSec());
        assertEquals(StopId.Stop1, trip.getFromStopId());
        assertEquals(StopId.Stop3, trip.getToStopId());
        assertEquals(new BigDecimal(7.30), trip.getChargeAmount());
        assertEquals("Company1", trip.getCompanyId());
        assertEquals("Bus37", trip.getBusID());
        assertEquals("5500005555555559", trip.getPAN());
        assertEquals("COMPLETED", trip.getStatus());
    }

}
