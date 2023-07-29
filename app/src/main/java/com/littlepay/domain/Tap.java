package com.littlepay.domain;

import java.time.LocalDateTime;

public class Tap {

    private Long id;
    private LocalDateTime dateTimeUTC;
    private TapType tapType;
    private StopId stopId;
    private String companyId;
    private String busId;
    private String PAN;

    public Tap(String ID, String dateTimeUTC, String tapType, String stopId, String companyId, String busID, String PAN) {
        this.id = Long.parseLong(ID);
        this.dateTimeUTC = LocalDateTime.parse(dateTimeUTC, Constants.FORMATTER);
        this.tapType = TapType.valueOf(tapType);
        this.stopId = StopId.valueOf(stopId);
        this.companyId = companyId;
        this.busId = busID;
        this.PAN = PAN;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDateTimeUTC() {
        return dateTimeUTC;
    }

    public TapType getTapType() {
        return tapType;
    }

    public StopId getStopId() {
        return stopId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getBusId() {
        return busId;
    }

    public String getPAN() {
        return PAN;
    }
}
