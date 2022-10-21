package by.aurorasoft.signum;

import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.dto.Tracker;
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

            final Tracker tracker = new Tracker(2L, "862430055592329", "448447045");
            final List<Command> commands = List.of(
                    new Command(null, "TPASS: 11111;flush;", tracker),
                    new Command(null, "TPASS: 11111;getver;", tracker),
                    new Command(null, "TPASS: 11111;getparam 0505;", tracker),
                    new Command(null, "TPASS: 11111;flush;", tracker),
                    new Command(null, "TPASS: 11111;getver;", tracker),
                    new Command(null, "TPASS: 11111;getparam 0505;", tracker),
                    new Command(null, "TPASS: 11111;flush;", tracker),
                    new Command(null, "TPASS: 11111;getver;", tracker),
                    new Command(null, "TPASS: 11111;getparam 0505;", tracker),
                    new Command(null, "TPASS: 11111;flush;", tracker),
                    new Command(null, "TPASS: 11111;getver;", tracker),
                    new Command(null, "TPASS: 11111;getparam 0505;", tracker),
                    new Command(null, "TPASS: 11111;flush;", tracker),
                    new Command(null, "TPASS: 11111;getver;", tracker),
                    new Command(null, "TPASS: 11111;getparam 0505;", tracker)
            );
            sendCommandService.send(commands);
        }).start();
        server.run();
    }
}
