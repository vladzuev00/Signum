package by.aurorasoft.signum;

import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.protocol.wialon.server.Server;
import by.aurorasoft.signum.protocol.wialon.service.sendcommand.CommandSenderService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
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
            final CommandSenderService sendCommandService = context.getBean(CommandSenderService.class);

            final List<Command> commands = List.of(
                    new Command(null, "TPASS: 11111;flush;", 11L),
                    new Command(null, "TPASS: 11111;getver;", 11L),
                    new Command(null, "TPASS: 11111;getparam 0505;", 11L),
                    new Command(null, "TPASS: 11111;flush;", 11L),
                    new Command(null, "TPASS: 11111;getver;", 11L),
                    new Command(null, "TPASS: 11111;getparam 0505;", 11L),
                    new Command(null, "TPASS: 11111;flush;", 11L),
                    new Command(null, "TPASS: 11111;getver;", 11L),
                    new Command(null, "TPASS: 11111;getparam 0505;", 11L),
                    new Command(null, "TPASS: 11111;flush;", 11L),
                    new Command(null, "TPASS: 11111;getver;", 11L),
                    new Command(null, "TPASS: 11111;getparam 0505;", 11L),
                    new Command(null, "TPASS: 11111;flush;", 11L),
                    new Command(null, "TPASS: 11111;getver;", 11L),
                    new Command(null, "TPASS: 11111;getparam 0505;", 11L)
            );
            sendCommandService.send(commands);
        }).start();

        server.run();
    }
}
