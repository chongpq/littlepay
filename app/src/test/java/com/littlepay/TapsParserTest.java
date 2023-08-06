package com.littlepay;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.littlepay.domain.CompletedTripCosts;
import com.littlepay.domain.IncompleteTripCosts;
import com.littlepay.domain.StopId;
import com.littlepay.domain.Tap;
import com.littlepay.domain.Trip;

public class TapsParserTest {

    private TripCalculator tripCalculator;
    private TapsParser tapsParser;

    @BeforeEach
    void setup() {
        IncompleteTripCosts incompleteTripCosts = new IncompleteTripCosts();
        CompletedTripCosts completedTripCosts = new CompletedTripCosts();
        this.tripCalculator = new TripCalculator(incompleteTripCosts, completedTripCosts);
        this.tapsParser = new TapsParser(tripCalculator);
    }

    @Test
    void emptyTapsArrayWorks() {
        Tap[] taps = {};

        List<Trip> trips = tapsParser.getTrips(taps);

        assertEquals(Collections.emptyList(), trips);
    }

    @Test
    void oneTapsArrayWorks() {
        Tap tap = new Tap("1", "22-01-2023 13:00:00", "ON", "Stop1", "Company1", "Bus37", "5500005555555559");

        Tap[] taps = { tap };

        List<Trip> trips = tapsParser.getTrips(taps);

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
    
        List<Trip> trips = tapsParser.getTrips(taps);

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

        List<Trip> trips = tapsParser.getTrips(taps);

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
    
        List<Trip> trips = tapsParser.getTrips(taps);

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

    @Test
    void testTripToStringMapper() {
        Trip trip = new Trip(
            LocalDateTime.of(2023,1,22,13,0,0),
            LocalDateTime.of(2023,1,22,13,0,5),
            1800l,
            StopId.Stop1,
            StopId.Stop3,
            new BigDecimal(7.30),
            "Company1",
            "Bus37",
            "5500005555555559",
            "COMPLETED"
        );

        assertEquals("22-01-2023 13:00:00, 22-01-2023 13:00:05, 1800, Stop1, Stop3, $7.30, Company1, Bus37, 5500005555555559, COMPLETED",TapsParser.TripToStringMapper(trip));
    }

    @Test
    void testIncompleteTripToStringMapper() {
        Trip trip = new Trip(LocalDateTime.of(2023,1,22,13,0,0), null, null, StopId.Stop1, null, new BigDecimal(7.30),
                        "Company1", "Bus37", "5500005555555559", "INCOMPLETED");

        assertEquals("22-01-2023 13:00:00, , , Stop1, , $7.30, Company1, Bus37, 5500005555555559, INCOMPLETED",TapsParser.TripToStringMapper(trip));
    }

    @Test
    void testTapTypeTapsComparator() {
        Tap tap = new Tap("1", "22-01-2024 13:00:00", "ON", "Stop1", "Company1", "Bus37", "5500005555555559");
        Tap tap2 = new Tap("2", "22-01-2024 13:00:00", "OFF", "Stop3", "Company1", "Bus37", "5500005555555559");
        List<Tap> taps = List.of(tap, tap2);
        
        List<Tap> result = taps.stream()
            .sorted(TapsParser.TAPS_COMPARATOR)
            .collect(Collectors.toList());

        assertEquals(1,result.get(0).getId());
        assertEquals(2,result.get(1).getId());
    }

    @Test
    void testDateTimeUTCTapsComparator() {
        Tap tap = new Tap("1", "22-01-2024 13:00:00", "ON", "Stop1", "Company1", "Bus37", "5500005555555559");
        Tap tap2 = new Tap("2", "22-01-2021 13:30:00", "OFF", "Stop3", "Company1", "Bus37", "5500005555555559");
        Tap tap3 = new Tap("3", "22-01-2023 13:00:00", "ON", "Stop1", "Company1", "Bus37", "5500005555555559");
        List<Tap> taps = List.of(tap, tap2, tap3);
        
        List<Tap> result = taps.stream()
            .sorted(TapsParser.TAPS_COMPARATOR)
            .collect(Collectors.toList());

        assertEquals(2,result.get(0).getId());
        assertEquals(3,result.get(1).getId());
        assertEquals(1,result.get(2).getId());
    }

    @Test
    void testIsTakingTwoTaps() {
        Tap tap = new Tap("1", "22-01-2024 13:00:00", "ON", "Stop1", "Company1", "Bus37", "5500005555555559");
        Tap tap2 = new Tap("2", "22-01-2024 13:00:00", "OFF", "Stop3", "Company1", "Bus37", "5500005555555559");
        
        assertTrue(tapsParser.isTakingTwoTaps(tap, tap2));
        assertFalse(tapsParser.isTakingTwoTaps(tap2, tap));
        assertFalse(tapsParser.isTakingTwoTaps(tap, tap));
        assertFalse(tapsParser.isTakingTwoTaps(tap2, tap));
    }
}