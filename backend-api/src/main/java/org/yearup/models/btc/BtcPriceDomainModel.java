package org.yearup.models.btc;

import java.math.BigDecimal;

//immutable model where its internal fields cannot be changed after construction
//instead of changing state of existing instance, old object is replaced with new one with new values
public class BtcPriceDomainModel {
    private final String base;
    private final String currency;
    private final BigDecimal amount;

    public BtcPriceDomainModel(String base, String currency, BigDecimal amount) {
        this.base = base;
        this.currency = currency;
        this.amount = amount;
    }

    public String getBase() {
        return base;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
