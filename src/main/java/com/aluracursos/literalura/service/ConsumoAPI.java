package com.aluracursos.literalura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoAPI {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private ConsumoAPI(){}

    public static <T> T get(String url, Class<T> clase){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response;
        try {
            response = client .send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();
            return OBJECT_MAPPER.readValue(json, clase);
        } catch (JsonProcessingException | InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

}
