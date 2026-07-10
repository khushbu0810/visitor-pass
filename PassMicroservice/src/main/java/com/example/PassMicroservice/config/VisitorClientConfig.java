package com.example.PassMicroservice.config;

import com.example.PassMicroservice.utils.VisitorDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Service
public class VisitorClientConfig {

     private final RestClient restClient;

     public VisitorClientConfig(@Value("${visitor.service.url}") String url){
         this.restClient = RestClient.builder()
                 .baseUrl(url)
                 .build();
     }

    public VisitorDTO getVisitorById(UUID visitorId,String token){
         return restClient.get()
                 .uri("visitor/{visitorId}",visitorId)
                 .header(HttpHeaders.AUTHORIZATION,token)
                 .retrieve()
                 .body(VisitorDTO.class);
    }
}
