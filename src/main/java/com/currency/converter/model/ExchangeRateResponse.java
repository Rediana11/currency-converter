package com.currency.converter.model;

import lombok.Data;


@Data
public class ExchangeRateResponse {

    private boolean success;
    private String terms;
    private String privacy;
    private Query query;
    private Info info;
    private double result;
}
