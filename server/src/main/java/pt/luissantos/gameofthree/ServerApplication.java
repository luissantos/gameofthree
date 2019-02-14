package pt.luissantos.gameofthree;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ServerApplication implements CommandLineRunner {

    public static void main(String[] args) { SpringApplication.run(ServerApplication.class, args); }

    @Value("${server.port}")
    Integer PORT;

    @Override
    public void run(String... args) throws Exception {

        Server server = new Server();

        server.run(PORT);
    }
}
