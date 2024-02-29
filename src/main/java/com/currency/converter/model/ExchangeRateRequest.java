package com.currency.converter.model;

import lombok.Data;

@Data
public class ExchangeRateRequest extends BaseInput {

    private String date;
}
