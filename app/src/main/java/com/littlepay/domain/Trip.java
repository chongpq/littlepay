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