package com.mateusgomes.luizalabs.api;

import com.mateusgomes.luizalabs.model.ProductAPIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class LuizalabsGateway{

    private RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<ProductAPIResponse> getProductById(UUID idProduct) {
        return restTemplate.getForEntity(
                String.format("http://challenge-api.luizalabs.com/api/product/%s/", idProduct),
                ProductAPIResponse.class
        );
    }
}
