package com.littlepay.domain;

import java.math.BigDecimal;

public class CompletedTripCosts {

    private BigDecimal[][] tripCosts = {
        {BigDecimal.ZERO, new BigDecimal(3.25), new BigDecimal(7.30)},
        {new BigDecimal(3.25), BigDecimal.ZERO, new BigDecimal(5.50)},
        {new BigDecimal(7.30), new BigDecimal(5.50), BigDecimal.ZERO}
    };

    public CompletedTripCosts() {
        // this is a in memory version of completed trip costs
        // we could read from a configuration file to setup completed trip costs
        
    }

    public BigDecimal get(StopId start, StopId end) {
        return tripCosts[start.arrayRef()][end.arrayRef()];
    }
}