package br.dev.as.screenmatch.principal;


import br.dev.as.screenmatch.model.DadosSerie;
import br.dev.as.screenmatch.model.DadosTemporada;
import br.dev.as.screenmatch.model.Episodio;
import br.dev.as.screenmatch.model.Serie;
import br.dev.as.screenmatch.repository.SerieRepository;
import br.dev.as.screenmatch.service.ConsumoApi;
import br.dev.as.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private String API_KEY = "&apikey=";

    List<Serie> allSeries;
    private SerieRepository repository;

    public void Principal(){
        API_KEY += System.getenv("OMBD_API_KEY");
    }

    public void exibeMenu(SerieRepository serieRepository) {
        this.repository = serieRepository;
        var menu = """
                1 - Buscar séries
                2 - Buscar episódios
                3 - Buscar séries cadastradas
                4 - Buscar série por título
                5 - Buscar série por ator
                6 - Buscar top 5    
                0 - Sair                                 
                """;

        System.out.println(menu);
        var opcao = leitura.nextInt();
        leitura.nextLine();

        switch (opcao) {
            case 1:
                buscarSerieWeb();
                break;
            case 2:
                buscarEpisodioPorSerie();
                break;
            case 3:
                listarSeriesBuscadas();
                break;
            case 4:
                buscarSeriePorTitulo();
                break;
            case 5:
                buscarSeriePorAtor();
                break;
            case 6:
                buscarTopCinco();
                break;
            case 0:
                System.out.println("Saindo...");
                break;
            default: {
                System.out.println("Opção inválida");
                exibeMenu(repository);
            }
        }
    }

    private void buscarTopCinco() {
        List<Serie> sa = repository.findTop5ByOrderByAvaliacaoDesc();
        sa.forEach(serie -> System.out.println(serie.getTitulo() + ", "+serie.getAvaliacao()));
    }

    private void buscarSeriePorAtor() {

        System.out.println("Digite o nome do ator para buscar:");
        var nome = leitura.nextLine();

        List<Serie> sa = repository.findByAtoresContainingIgnoreCase(nome);

        if(!sa.isEmpty())
            sa.forEach(System.out::println);
        else System.out.println("Não encontrada!");

        exibeMenu(repository);
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Digite o titulo da série para buscar:");
        var nomeSerie = leitura.nextLine();
        Optional<Serie> s = repository.findByTituloContainingIgnoreCase(nomeSerie);
        if(s.isPresent())
            System.out.println(s.get());
        else System.out.println("Não encontrada!");

        exibeMenu(repository);
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repository.save(serie);

        exibeMenu(repository);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);

        return dados;
    }

    private void buscarEpisodioPorSerie() {
        listarSeriesBuscadas();
        System.out.println("Escolha a série pelo ID: ");

        Long idSerie = Long.parseLong(leitura.nextLine());

        Optional<Serie> first = allSeries.stream().filter(serie -> serie.getId().equals(idSerie)).findFirst();
        if(first.isPresent()) {

            Serie serieSelecionada = first.get();

            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieSelecionada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieSelecionada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }

//            temporadas.forEach(System.out::println);
           List<Episodio> episodios = temporadas.stream().flatMap(
                    d -> d.episodios().stream().map(
                            e -> new Episodio(d.numero(),e))).collect(Collectors.toList());

            serieSelecionada.setEpisodios(episodios);

            repository.save(serieSelecionada);

        } else
            System.out.println("Série não encontrada.");

        exibeMenu(repository);
    }

    private void listarSeriesBuscadas() {
        allSeries = repository.findAll();
        allSeries.stream().sorted(Comparator.comparing(Serie::getTitulo))
                .forEach(System.out::println);
    }
}
