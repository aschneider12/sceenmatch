package br.dev.as.screenmatch.model;

import java.util.Arrays;
import java.util.Optional;

public enum Categoria {

    ACAO("Action"),ROMANCE("Romance"),COMEDIA("Comedy"),DRAMA("Drama"),CRIME("Crime"),TERROR("Terror");

    private String categoriaOmdb;

    Categoria(String categoryOmdb){
        this.categoriaOmdb = categoryOmdb;
    }

    public static Categoria fromString(String text) {
        Optional<Categoria> first = Arrays.stream(Categoria.values()).filter(categoria -> categoria.categoriaOmdb.equals(text)).findFirst();
        if(first.isPresent())
            return first.get();

        throw new IllegalArgumentException("Categoria não encontrada!");
    }
}
