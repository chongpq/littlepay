package com.littlepay.domain;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

public class Constants {
    
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("$#,###.00");
}