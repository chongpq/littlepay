package com.littlepay.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Trip {

    private LocalDateTime started;
    private LocalDateTime finished;
    private Long durationSec;
    private StopId fromStopId;
    private StopId toStopId;
    private BigDecimal chargeAmount;
    private String companyId;
    private String busID;
    private String PAN;
    private String status;

    public Trip(LocalDateTime started, LocalDateTime finished, Long durationSec, StopId fromStopId, StopId toStopId,
                    BigDecimal chargeAmount, String companyId, String busID, String PAN, String status) {
        this.started = started;
        this.finished = finished;
        this.durationSec = durationSec;
        this.fromStopId = fromStopId;
        this.toStopId = toStopId;
        this.chargeAmount = chargeAmount;
        this.companyId = companyId;
        this.busID = busID;
        this.PAN = PAN;
        this.status = status;
    }

    public Object getStarted() {
        return started;
    }
    public Object getFinished() {
        return finished;
    }
    public Long getDurationSec() {
        return durationSec;
    }
	public StopId getFromStopId() {
		return fromStopId;
	}
    public StopId getToStopId() {
        return toStopId;
    }
    public BigDecimal getChargeAmount() {
        return chargeAmount;
    }
    public String getCompanyId() {
        return companyId;
    }
	public String getBusID() {
		return busID;
	}
	public String getPAN() {
		return PAN;
	}
    public String getStatus() {
        return status;
    }

}