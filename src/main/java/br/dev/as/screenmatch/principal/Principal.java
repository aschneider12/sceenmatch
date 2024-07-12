package br.dev.as.screenmatch.principal;

import br.dev.as.screenmatch.model.DadosEpisodio;
import br.dev.as.screenmatch.model.DadosSerie;
import br.dev.as.screenmatch.model.DadosTemporada;
import br.dev.as.screenmatch.model.Episodio;
import br.dev.as.screenmatch.service.ConsumoAPI;
import br.dev.as.screenmatch.service.ConverteDados;
import org.springframework.cglib.core.Local;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private final String ENDERECO = "https://www.omdbapi.com/?t=";

    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados converteDados = new ConverteDados();
    private Scanner leitor = new Scanner(System.in);

        API_KEY += System.getenv("OMBD_API_KEY");

        System.out.println("Digite nome da série:");
        var nomeSerie = leitor.nextLine();

        String completeURL = API_URL + nomeSerie.replace(" ", "+") + API_KEY;

        var json = consumoAPI.obterDados(completeURL);

//        var episodio = cv.obterDados(json, DadosEpisodio.class);

        DadosSerie dadosSerie = converteDados.obterDados(json, DadosSerie.class);
        System.out.println(dadosSerie);

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            json = consumoAPI.obterDados(API_URL + nomeSerie.replace(" ", "+") + "&Season=" + i + API_KEY);
            DadosTemporada temporada = converteDados.obterDados(json, DadosTemporada.class);
            temporadas.add(temporada);
        }
        temporadas.forEach(t -> {
            System.out.println("--- ---- Temporada " + t.numero());
            t.episodios().forEach(e -> System.out.println(e.titulo()));
        });

//        List<DadosEpisodio> dados_episodios = temporadas.stream().flatMap(
//                t -> t.episodios().stream())
//                .collect(Collectors.toList());

        List<Episodio> episodios = temporadas.stream().flatMap(
                        t -> t.episodios().stream()
                                .map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());

        episodios.stream()
                .filter(e -> !"N/A".equals(e.getAvaliacao()))
                .sorted(Comparator.comparing(Episodio::getAvaliacao).reversed())

                .limit(5)
                .forEach(System.out::println);
//        episodios.add(new DadosEpisodio("asd",1, "",""));

        System.out.println("A partir de que ano você quer ver os episódios?");
        var ano = leitor.nextInt();
        leitor.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(episodio ->
                        System.out.println(
                                "Temporada: " + episodio.getTemporada()
                                        + " Episódio: " + episodio.getTitulo()
                                        + " Data lanç: " + dtf.format(episodio.getDataLancamento())
                        )
                );

        Map<Integer, Double> avaliacoes = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada, Collectors.averagingDouble(Episodio::getAvaliacao)));

        System.out.println(avaliacoes);
    }

}
