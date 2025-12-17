package org.yearup.models;

import java.math.BigDecimal;
//immutable model where its internal fields cannot be changed after construction
//instead of changing state of existing instance, old object is replaced with new one with new values
public class BtcPrice {
    private final String btc;
    private final String usd;
    private final BigDecimal amount;


    public BtcPrice(String btc, String usd, BigDecimal amount) {
        this.btc = btc;
        this.usd = usd;
        this.amount = amount;
    }

    public String getBtc() {
        return btc;
    }

    public String getUsd() {
        return usd;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
