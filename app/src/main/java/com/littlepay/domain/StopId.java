package com.littlepay.domain;

public enum StopId {
    Stop1(0),
    Stop2(1),
    Stop3(2);

    private final int arrayRef;

    StopId(int arrayRef) {
        this.arrayRef = arrayRef;
    }

    int arrayRef() {
        return arrayRef;
    }
}
