package org.yearup.client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

//class for only calling Coinbase API
@Component
public class CoinbaseBtcUsdClient {
    //HttpClient sends and receives requests/responses
    //Used here to call Coinbase API and receive the body as JSON text
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private final String url;

    @Autowired
    public CoinbaseBtcUsdClient(@Value("${api.coinbase.uri}") String url){
        this.url = url;
    }

    public String fetchBtcUsdJson() throws Exception{
        //converts the String url into URI object
        URI uri = URI.create(url);
        //builds HTTP GET request to the provided URI (no body required)
        HttpRequest request = HttpRequest.newBuilder(uri).header("Accept", "application/json").GET().build();
        //sends the request and converts response body (JSON bytes) to a String
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() / 100 !=2){ //integer division gives exactly 2 (allows all 2xx codes)
            throw new RuntimeException("Coinbase request failed: HTTP " + response.statusCode() + " body=" + response.body());
        }
        return response.body();
    }
}
