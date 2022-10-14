package by.aurorasoft.signum.protocol.wialon.it;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.config.property.ServerProperty;
import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import by.aurorasoft.signum.crud.model.entity.UnitEntity;
import by.aurorasoft.signum.protocol.wialon.server.Server;
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

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.Instant.parse;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

public class RequestHandlingIT extends AbstractContextTest {
    private static boolean serverWasRan = false;
    private static final String RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION = "#AL#1\r\n";

    @Autowired
    private Server server;

    @Autowired
    private ServerProperty serverProperty;

    private Client client;

    @Before
    public void startServerAndClient() {
        if (!serverWasRan) {
            new Thread(() -> this.server.run()).start();
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
                + "firstparam:1:654321,secondparam:2:65.4321,thirdparam:3:param-value\r\n";

        final String actualResponse = this.client.doRequest(request).get();
        final String expectedResponse = "#AD#1\r\n";
        assertEquals(expectedResponse, actualResponse);

        final List<MessageEntity> messagesFromDB = super.findEntitiesFromDB(MessageEntity.class);
        assertEquals(1, messagesFromDB.size());

        final MessageEntity actualSavedMessage = messagesFromDB.get(0);
        final MessageEntity expectedSavedMessage = new MessageEntity(
                super.entityManager.getReference(UnitEntity.class, 25551L),
                parse("2022-11-15T14:56:43Z"),
                57.406944F, 39.548332F, 100, 15, 10, 177,
                545.4554F, "firstparam:654321,secondparam:65.4321,thirdparam:param-value");

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
                //wrong amount of ':' after 'firstparam'
                + "firstparam::1:654321,secondparam:2:65.4321,thirdparam:3:param-value\r\n";

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
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value"
                + "|"
                + "161122;145644;5544.6025;N;03739.6834;E;101;16;11;178;545.4555;18;19;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654322,param-name:2:66.4321,param-name:3:param-value\r\n";

        final String actualResponse = this.client.doRequest(request).get();
        final String expectedResponse = "#AB#2\r\n";
        assertEquals(expectedResponse, actualResponse);

        final List<MessageEntity> messagesFromDB = super.findEntitiesFromDB(MessageEntity.class);
        assertEquals(2, messagesFromDB.size());

        final MessageEntity actualFirstSavedMessage = messagesFromDB.get(0);
        final MessageEntity expectedFirstSavedMessage = new MessageEntity(
                super.entityManager.getReference(UnitEntity.class, 25551L),
                parse("2022-11-11T14:56:43Z"),
                57.406944F, 39.548332F, 100, 15, 10, 177,
                545.4554F, "param-name:654321,param-name:65.4321,param-name:param-value");
        checkEqualsExceptId(expectedFirstSavedMessage, actualFirstSavedMessage);

        final MessageEntity secondActualSavedMessage = messagesFromDB.get(1);
        final MessageEntity expectedSecondSavedMessage = new MessageEntity(
                super.entityManager.getReference(UnitEntity.class, 25551L),
                parse("2022-11-16T14:56:44Z"),
                57.406944F, 39.548332F, 101, 16, 11, 178,
                545.4555F, "param-name:654322,param-name:66.4321,param-name:param-value");
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

    private static void checkEqualsExceptId(MessageEntity expected, MessageEntity actual) {
        assertEquals(expected.getUnit().getId(), actual.getUnit().getId());
        assertEquals(expected.getDateTime(), actual.getDateTime());
        assertEquals(expected.getLatitude(), actual.getLatitude(), 0.);
        assertEquals(expected.getLongitude(), actual.getLongitude(), 0.);
        assertEquals(expected.getSpeed(), actual.getSpeed());
        assertEquals(expected.getCourse(), actual.getCourse());
        assertEquals(expected.getAltitude(), actual.getAltitude());
        assertEquals(expected.getAmountSatellite(), actual.getAmountSatellite());
        assertEquals(expected.getHdop(), actual.getHdop(), 0.);
        assertEquals(expected.getParameters(), actual.getParameters());
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

        @Override
        public void close() throws IOException {
            this.executorService.shutdownNow();
            this.socket.close();
        }
    }
}
