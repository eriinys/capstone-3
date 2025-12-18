package org.yearup.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.yearup.client.CoinbaseBtcUsdClient;
import org.yearup.models.btc.BtcPriceDomainModel;
import org.yearup.models.btc.CoinbaseData;
import org.yearup.models.btc.CoinbaseSpotPriceResponse;

import java.math.BigDecimal;
@Component
public class CoinbaseBtcPriceService {
    private CoinbaseBtcUsdClient client;
    private ObjectMapper objectMapper;

    public CoinbaseBtcPriceService(CoinbaseBtcUsdClient client, ObjectMapper objectMapper){
        this.client = client;
        this.objectMapper = objectMapper;
    }

    public BtcPriceDomainModel getSpotPrice() throws Exception {
       //gets JSON as String
        String json = client.fetchBtcUsdJson();
        //parses JSON String into outer DTO
        CoinbaseSpotPriceResponse response = objectMapper.readValue(json, CoinbaseSpotPriceResponse.class);
        //records into nested DTO
        CoinbaseData data = response.data();
        //map DTO into domain model
        BigDecimal amount = new BigDecimal(data.amount());
        //return new domain model
        return new BtcPriceDomainModel(data.base(), data.currency(), amount);
    }
}
