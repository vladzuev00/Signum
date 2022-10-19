package by.aurorasoft.signum;

import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.dto.Tracker;
import by.aurorasoft.signum.protocol.wialon.server.Server;
import by.aurorasoft.signum.protocol.wialon.service.sendcommand.SendCommandService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class ApplicationRunner {
    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(ApplicationRunner.class, args);
        final Server server = context.getBean(Server.class);
        new Thread(() -> {
            try {
                TimeUnit.MINUTES.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            final SendCommandService sendCommandService = context.getBean(SendCommandService.class);

            final Tracker tracker = new Tracker(2L, "862430055592329", "448447045");
            final Command command = new Command(null, "TPASS: 11111;flush;", tracker);
            sendCommandService.sendCommand(command);
        }).start();
        server.run();
    }
}
