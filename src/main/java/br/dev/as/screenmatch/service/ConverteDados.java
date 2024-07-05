package br.dev.as.screenmatch.service;

import br.dev.as.screenmatch.model.DadosSerie;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConverteDados implements  IConverteDados {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T obterDados(String json, Class<T> tClass) {
        try {

            return mapper.readValue(json, tClass);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
