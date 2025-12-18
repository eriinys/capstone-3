package org.yearup.models.btc;

//nested DTO
//record implicitly creates constructor, getters and setters by the complier
public record CoinbaseData(String base, String currency, String amount) {

}
