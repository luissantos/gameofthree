
package pt.luissantos.gameofthree;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pt.luissantos.gameofthree.client.Client;

@SpringBootApplication
public class ClientApplication implements CommandLineRunner {

    Logger logger = LoggerFactory.getLogger(ClientApplication.class);

    @Value("${game.endpoint}?game=${game.gameId}")
    String URL;

    @Override
    public void run(String... args) throws Exception {
        Client client = new Client();
        client.run(URL);
    }

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }
}
