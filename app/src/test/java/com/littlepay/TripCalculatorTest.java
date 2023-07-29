package com.littlepay;

import org.junit.jupiter.api.Test;

import com.littlepay.domain.CompletedTripCosts;
import com.littlepay.domain.IncompleteTripCosts;
import com.littlepay.domain.StopId;
import com.littlepay.domain.Tap;
import com.littlepay.domain.Trip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

class TripCalculatorTest {

    @Test
    void completeTripCosts() {
        CompletedTripCosts completedTripCosts = new CompletedTripCosts();
        TripCalculator classUnderTest = new TripCalculator(null, completedTripCosts);
        Tap start = new Tap("1", "22-01-2023 13:00:00", "ON", "Stop1", "Company1", "Bus37", "5500005555555559");
        Tap end = new Tap("2", "22-01-2023 13:30:00", "OFF", "Stop3", "Company1", "Bus37", "5500005555555559");
        
        Trip trip = classUnderTest.calculate(start, end);

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
    void incompleteTripCosts() {
        IncompleteTripCosts incompleteTripCosts = new IncompleteTripCosts();
        TripCalculator classUnderTest = new TripCalculator(incompleteTripCosts, null);
        Tap start = new Tap("1", "22-01-2023 13:00:00", "ON", "Stop1", "Company1", "Bus37", "5500005555555559");
        
        Trip trip = classUnderTest.calculate(start, null);

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
    void cancelledTripWorks() {
        CompletedTripCosts completedTripCosts = new CompletedTripCosts();
        TripCalculator classUnderTest = new TripCalculator(null, completedTripCosts);
        Tap start = new Tap("1", "22-01-2023 13:00:00", "ON", "Stop1", "Company1", "Bus37", "5500005555555559");
        Tap end = new Tap("2", "22-01-2023 13:00:05", "OFF", "Stop1", "Company1", "Bus37", "5500005555555559");
        
        Trip trip = classUnderTest.calculate(start, end);

        assertEquals(LocalDateTime.of(2023,1,22,13,0,0), trip.getStarted());
        assertEquals(LocalDateTime.of(2023,1,22,13,0,5), trip.getFinished());
        assertEquals(5l, trip.getDurationSec());
        assertEquals(StopId.Stop1, trip.getFromStopId());
        assertEquals(StopId.Stop1, trip.getToStopId());
        assertEquals(BigDecimal.ZERO, trip.getChargeAmount());
        assertEquals("Company1", trip.getCompanyId());
        assertEquals("Bus37", trip.getBusID());
        assertEquals("5500005555555559", trip.getPAN());
        assertEquals("CANCELLED", trip.getStatus());
    }
}