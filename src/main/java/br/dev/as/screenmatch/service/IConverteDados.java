package br.dev.as.screenmatch.service;

public interface IConverteDados {

    <T> T obterDados(String json, Class<T> tClass);
}
