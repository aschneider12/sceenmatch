package br.dev.as.screenmatch;

import br.dev.as.screenmatch.principal.Principal;
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
		Principal p = new Principal();
		p.exibeMenu();
	}
}
