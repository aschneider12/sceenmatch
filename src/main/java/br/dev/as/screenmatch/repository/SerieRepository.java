package br.dev.as.screenmatch.repository;

import br.dev.as.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * https://docs.spring.io/spring-data/jpa/reference/
 */
public interface SerieRepository extends JpaRepository<Serie, Long> {

    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);
    List<Serie> findByAtoresContainingIgnoreCase(String nome);
    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nome, Double avaliacao);
    List<Serie> findTop5ByOrderByAvaliacaoDesc();
}

