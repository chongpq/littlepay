package com.littlepay.domain;

import java.math.BigDecimal;

public class IncompleteTripCosts {
    
    private BigDecimal[] tripCosts = { new BigDecimal(7.30), new BigDecimal(5.50), new BigDecimal(7.30) };

    public IncompleteTripCosts() {
        // this is a in memory version of incompleted trip costs
        // we could read from a configuration file to setup incompleted trip 
    }

    public BigDecimal get(StopId stopId) {
        return tripCosts[stopId.arrayRef()];
    }

}
