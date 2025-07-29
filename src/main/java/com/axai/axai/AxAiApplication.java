package com.axai.axai;

import com.axai.axai.cli.CliHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AxAiApplication implements CommandLineRunner {

    private final CliHandler cliHandler;

    public AxAiApplication(CliHandler cliHandler) {
        this.cliHandler = cliHandler;
    }

    public static void main(String[] args) {
        SpringApplication.run(AxAiApplication.class, args);
    }

    @Override
    public void run(String... args) {
        cliHandler.start();
    }
}
