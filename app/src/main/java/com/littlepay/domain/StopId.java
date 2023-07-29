package com.littlepay.domain;

import java.util.Optional;

public enum StopId {
    STOP1(0),
    STOP2(1),
    STOP3(2);

    private final int arrayRef;

    StopId(int arrayRef) {
        this.arrayRef = arrayRef;
    }

    int arrayRef() {
        return arrayRef;
    }

    public Optional<StopId> getStopId(String str) {
        return Optional.empty();
    }
}
