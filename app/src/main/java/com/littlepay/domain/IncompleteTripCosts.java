package com.littlepay.domain;

import java.math.BigDecimal;

public class IncompleteTripCosts {
    
    private BigDecimal[] tripCosts = {};

    public IncompleteTripCosts() {
        // this is a in memory version of completed trip costs
        // we could read from a configuration file to setup completed trip 
    }

    public BigDecimal get(StopId stopId) {
        return tripCosts[stopId.arrayRef()];
    }

}
