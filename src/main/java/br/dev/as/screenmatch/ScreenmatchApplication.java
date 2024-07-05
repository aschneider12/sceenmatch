package br.dev.as.screenmatch;

import br.dev.as.screenmatch.model.DadosSerie;
import br.dev.as.screenmatch.service.ConsumoAPI;
import br.dev.as.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@java.lang.Override
	public void run(java.lang.String... args) throws Exception {
		System.out.println("Primeiro projeto rodando");

		ConsumoAPI api = new ConsumoAPI();
		var json = api.obterDados("https://www.omdbapi.com/?t=anne&apikey=6585022c");
		System.out.println(json);

		ConverteDados cv = new ConverteDados();
		DadosSerie dadosSerie = cv.obterDados(json, DadosSerie.class);

		System.out.println(dadosSerie);

	}
}
