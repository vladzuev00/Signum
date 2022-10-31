package by.aurorasoft.signum.protocol.wialon.it;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.config.property.ServerProperty;
import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.model.entity.CommandEntity;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import by.aurorasoft.signum.protocol.core.connectionmanager.ConnectionManager;
import by.aurorasoft.signum.protocol.core.contextmanager.ContextManager;
import by.aurorasoft.signum.protocol.wialon.server.Server;
import io.netty.channel.ChannelHandlerContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static by.aurorasoft.signum.crud.model.entity.CommandEntity.Status.SUCCESS;
import static by.aurorasoft.signum.crud.model.entity.CommandEntity.Type.ANSWER;
import static by.aurorasoft.signum.crud.model.entity.CommandEntity.Type.COMMAND;
import static by.aurorasoft.signum.crud.model.entity.DeviceEntity.Type.TRACKER;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.Instant.parse;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

public class InboundPackageHandlingIT extends AbstractContextTest {
    private static boolean serverWasRan = false;
    private static final String RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION = "#AL#1\r\n";

    @Autowired
    private Server server;

    @Autowired
    private ServerProperty serverProperty;

    @Autowired
    private ContextManager contextManager;

    @Autowired
    private ConnectionManager connectionManager;

    private Client client;

    @Before
    public void startServerAndClient() {
        if (!serverWasRan) {
            new Thread(this.server::run).start();
            serverWasRan = true;
        }
        this.client = new Client(this.serverProperty);
    }

    @After
    public void closeClient() throws IOException {
        this.client.close();
    }

    @Test
    public void loginPackageShouldBeHandledSuccess() throws Exception {
        final String request = "#L#355234055650192;NA\r\n";

        final String actual = this.client.doRequest(request).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, actual);
    }

    @Test
    public void loginPackageShouldBeDenied() throws Exception {
        final String request = "#L#11111;NA\r\n";

        final String actual = this.client.doRequest(request).get();
        final String expected = "#AL#0\r\n";
        assertEquals(expected, actual);
    }

    @Test
    public void pingPackageShouldBeHandled() throws Exception {
        final String request = "#P#\r\n";

        final String actual = this.client.doRequest(request).get();
        final String expected = "#AP#\r\n";
        assertEquals(expected, actual);
    }

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "DELETE FROM message", executionPhase = AFTER_TEST_METHOD)
    public void dataPackageShouldBeHandled() throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final String request = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "21:1:1,secondparam:2:65.4321,VPWR:2:54\r\n";

        final String actualResponse = this.client.doRequest(request).get();
        final String expectedResponse = "#AD#1\r\n";
        assertEquals(expectedResponse, actualResponse);

        final List<MessageEntity> messagesFromDB = super.findEntities(MessageEntity.class);
        assertEquals(1, messagesFromDB.size());

        final MessageEntity actualSavedMessage = messagesFromDB.get(0);
        final MessageEntity expectedSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .dateTime(parse("2022-11-15T14:56:43Z"))
                .latitude(57.406944F)
                .longitude(39.548332F)
                .speed(100)
                .course(15)
                .altitude(10)
                .amountSatellite(177)
                .gsmLevel(16)
                .onboardVoltage(32)   //smallint in db parameters are bounded before insert or update in db
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .build();

        checkEqualsExceptId(expectedSavedMessage, actualSavedMessage);
    }

    @Test
    public void notValidDataPackageShouldNotBeHandled() throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final String givenRequest = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                //wrong amount of ':' after '21'
                + "21::1:1,secondparam:2:65.4321,VPWR:2:54\r\n";

        final String actual = this.client.doRequest(givenRequest).get();
        final String expected = "#AD#0\r\n";
        assertEquals(expected, actual);
    }

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "DELETE FROM message", executionPhase = AFTER_TEST_METHOD)
    public void blackBoxPackageShouldBeHandled() throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final String request = "#B#"
                + "111122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "21:1:1,secondparam:2:65.4321,VPWR:2:31.5"
                + "|"
                + "161122;145644;5544.6025;N;03739.6834;E;101;16;11;178;545.4555;18;19;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654322,param-name:2:66.4321,param-name:3:param-value\r\n";

        final String actualResponse = this.client.doRequest(request).get();
        final String expectedResponse = "#AB#2\r\n";
        assertEquals(expectedResponse, actualResponse);

        final List<MessageEntity> messagesFromDB = super.findEntities(MessageEntity.class);
        assertEquals(2, messagesFromDB.size());

        final MessageEntity actualFirstSavedMessage = messagesFromDB.get(0);
        final MessageEntity expectedFirstSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .dateTime(parse("2022-11-11T14:56:43Z"))
                .latitude(57.406944F)
                .longitude(39.548332F)
                .speed(100)
                .course(15)
                .altitude(10)
                .amountSatellite(177)
                .gsmLevel(16)
                .onboardVoltage(31.5F)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .build();
        checkEqualsExceptId(expectedFirstSavedMessage, actualFirstSavedMessage);

        final MessageEntity secondActualSavedMessage = messagesFromDB.get(1);
        final MessageEntity expectedSecondSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .dateTime(parse("2022-11-16T14:56:44Z"))
                .latitude(57.406944F)
                .longitude(39.548332F)
                .speed(101)
                .course(16)
                .altitude(11)
                .amountSatellite(178)
                .gsmLevel(-1)
                .onboardVoltage(-1)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .build();
        checkEqualsExceptId(expectedSecondSavedMessage, secondActualSavedMessage);
    }

    @Test
    public void notValidBlackBoxPackageShouldNotBeHandled() throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final String request = "#B#"
                + "111122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value"
                + "|"
                + "161122;145644;5544.6025;N;03739.6834;E;101;16;11;178;545.4555;18;19;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                //wrong amount of ':' after first 'param-name'
                + "param-name::1:654322,param-name:2:66.4321,param-name:3:param-value\r\n";

        final String actualResponse = this.client.doRequest(request).get();
        final String expectedResponse = "#AB#0\r\n";
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "DELETE FROM command", executionPhase = AFTER_TEST_METHOD)
    public void requestCommandPackageShouldBeHandled() throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final String givenRequest = "#M#command\r\n";

        final String actualResponse = this.client.doRequest(givenRequest).get();
        final String expectedResponse = "#AM#1\r\n";
        assertEquals(expectedResponse, actualResponse);

        final List<CommandEntity> commandsFromDB = super.findEntities(CommandEntity.class);
        assertEquals(1, commandsFromDB.size());

        final CommandEntity actualSavedCommand = commandsFromDB.get(0);
        final CommandEntity expectedSavedCommand = CommandEntity.builder()
                .text("command")
                .status(SUCCESS)
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .type(ANSWER)
                .build();
        checkEqualsExceptId(expectedSavedCommand, actualSavedCommand);
    }

    @Test
    @Sql(statements = "INSERT INTO command(id, text, status, device_id, type) VALUES (255, 'command', 'SENT', 25551, 'COMMAND')")
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "DELETE FROM command", executionPhase = AFTER_TEST_METHOD)
    public void responseCommandPackageShouldBeHandledSuccess()
            throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final Device device = new Device(25551L, "355234055650192", "+37257063997", TRACKER);
        final ChannelHandlerContext context = this.connectionManager.findContext(device).orElseThrow();
        this.contextManager.putCommandWaitingResponse(context, new Command(255L, "command", device));

        final String givenResponse = "#AM#1\r\n";
        this.client.doResponse(givenResponse);

        SECONDS.sleep(1);

        assertFalse(this.contextManager.isExistCommandWaitingResponse(context));

        final List<CommandEntity> commandsFromDB = super.findEntities(CommandEntity.class);
        assertEquals(1, commandsFromDB.size());

        final CommandEntity actualSavedCommand = commandsFromDB.get(0);
        final CommandEntity expectedSavedCommand = CommandEntity.builder()
                .text("command")
                .status(SUCCESS)
                .device(DeviceEntity.builder()
                        .id(25551L)
                        .build())
                .type(COMMAND)
                .build();
        checkEqualsExceptId(expectedSavedCommand, actualSavedCommand);
    }

    private static void checkEqualsExceptId(MessageEntity expected, MessageEntity actual) {
        assertEquals(expected.getDevice().getId(), actual.getDevice().getId());
        assertEquals(expected.getDateTime(), actual.getDateTime());
        assertEquals(expected.getLatitude(), actual.getLatitude(), 0.);
        assertEquals(expected.getLongitude(), actual.getLongitude(), 0.);
        assertEquals(expected.getSpeed(), actual.getSpeed());
        assertEquals(expected.getCourse(), actual.getCourse());
        assertEquals(expected.getAltitude(), actual.getAltitude());
        assertEquals(expected.getAmountSatellite(), actual.getAmountSatellite());
        assertEquals(expected.getGsmLevel(), actual.getGsmLevel());
        assertEquals(expected.getOnboardVoltage(), actual.getOnboardVoltage(), 0.);
        assertEquals(expected.getEcoCornering(), actual.getEcoCornering(), 0.);
        assertEquals(expected.getEcoAcceleration(), actual.getEcoAcceleration(), 0.);
        assertEquals(expected.getEcoBraking(), actual.getEcoBraking(), 0.);
    }

    private static void checkEqualsExceptId(CommandEntity expected, CommandEntity actual) {
        assertEquals(expected.getText(), actual.getText());
        assertSame(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getDevice().getId(), actual.getDevice().getId());
        assertSame(expected.getStatus(), actual.getStatus());
    }

    private static final class Client implements AutoCloseable {
        private static final char PACKAGE_LAST_CHARACTER = '\n';

        private final Socket socket;
        private final ExecutorService executorService;

        public Client(ServerProperty serverProperty) {
            try {
                this.socket = new Socket(serverProperty.getHost(), serverProperty.getPort());
                this.executorService = newSingleThreadExecutor();
            } catch (final Exception cause) {
                throw new RuntimeException(cause);
            }
        }

        public Future<String> doRequest(String request) {
            return this.executorService.submit(() -> {
                final OutputStream outputStream = this.socket.getOutputStream();
                outputStream.write(request.getBytes(UTF_8));

                final InputStream inputStream = this.socket.getInputStream();
                final StringBuilder responseBuilder = new StringBuilder();
                char currentReadChar;
                do {
                    currentReadChar = (char) inputStream.read();
                    responseBuilder.append(currentReadChar);
                } while (currentReadChar != PACKAGE_LAST_CHARACTER);

                return responseBuilder.toString();
            });
        }

        public void doResponse(String response) {
            try {
                final OutputStream outputStream = this.socket.getOutputStream();
                outputStream.write(response.getBytes(UTF_8));
            } catch (IOException cause) {
                throw new RuntimeException(cause);
            }
        }

        @Override
        public void close() throws IOException {
            this.executorService.shutdownNow();
            this.socket.close();
        }
    }
}
