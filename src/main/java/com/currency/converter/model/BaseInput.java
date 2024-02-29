package com.currency.converter.model;

import lombok.Data;

@Data
public class BaseInput {

    private String from;
    private String to;
    private double amount;
}
