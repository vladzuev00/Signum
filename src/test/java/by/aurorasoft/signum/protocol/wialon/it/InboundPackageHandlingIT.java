package by.aurorasoft.signum.protocol.wialon.it;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.config.property.ServerProperty;
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

import static by.aurorasoft.signum.crud.model.dto.Command.Status.SUCCESS;
import static by.aurorasoft.signum.crud.model.dto.Command.Type.ANSWER;
import static by.aurorasoft.signum.crud.model.dto.Command.Type.COMMAND;
import static by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType.*;
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
    private static final String HQL_QUERY_TO_FIND_LAST_INSERTED_MESSAGE
            = "SELECT me FROM MessageEntity me WHERE me.id = (SELECT MAX(me.id) FROM MessageEntity me)";
    private static final int WAIT_MESSAGE_DELIVERING_SECONDS_AMOUNT = 3;

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
    public void startServerAndClient()
            throws Exception {
        if (!serverWasRan) {
            new Thread(this.server::run).start();
            SECONDS.sleep(1);  //to give thread starting server time to run server
            serverWasRan = true;
        }
        this.client = new Client(this.serverProperty);
    }

    @After
    public void closeClient() throws IOException {
        this.client.close();
    }

    @Test
    public void loginPackageShouldBeHandledSuccess()
            throws Exception {
        final String request = "#L#355234055650192;NA\r\n";

        final String actual = this.client.doRequest(request).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, actual);
    }

    @Test
    public void loginPackageShouldBeDeniedBecauseOfNotExistingImei() throws Exception {
        final String request = "#L#00000000000000000000;NA\r\n";

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
    @Sql(statements = "UPDATE device_state SET last_message_id = NULL", executionPhase = AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM message", executionPhase = AFTER_TEST_METHOD)
    public void dataPackageShouldBeHandledAsValidWithoutFixing() throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final String request = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "21:1:1,secondparam:2:65.4321,VPWR:2:3,"
                //HDOP, VDOP, PDOP
                + "122:1:5,123:1:6,124:1:7,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0"
                + "\r\n";

        final String actualResponse = this.client.doRequest(request).get();
        final String expectedResponse = "#AD#1\r\n";
        assertEquals(expectedResponse, actualResponse);

        waitMessageDelivering();


        final List<MessageEntity> messagesFromDB = super.findEntities(MessageEntity.class);
        assertEquals(1, messagesFromDB.size());

        final MessageEntity actualSavedMessage = messagesFromDB.get(0);
        final MessageEntity expectedSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-11-15T14:56:43Z"))
                .latitude(57.406944F)
                .longitude(39.548332F)
                .speed(100)
                .course(15)
                .altitude(10)
                .amountSatellite(177)
                .gsmLevel(16)
                .onboardVoltage(3)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(VALID)
                .gpsOdometer(0.)
                .ignition(0)
                .engineTime(0)
                .shock(5.)
                .build();

        checkEqualsExceptId(expectedSavedMessage, actualSavedMessage);
    }

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "UPDATE device_state SET last_message_id = NULL", executionPhase = AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM message", executionPhase = AFTER_TEST_METHOD)
    public void dataPackageShouldBeHandledAsIncorrectBecauseOfNotValidAmountOfSatelliteAndThereIsNoPreviousValidMessageToFix()
            throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final String request = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;1;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "21:1:1,secondparam:2:65.4321,VPWR:2:31,"
                //HDOP, VDOP, PDOP
                + "122:1:5,123:1:6,124:1:7,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0"
                + "\r\n";

        final String actualResponse = this.client.doRequest(request).get();
        final String expectedResponse = "#AD#1\r\n";
        assertEquals(expectedResponse, actualResponse);

        final List<MessageEntity> messagesFromDB = super.findEntities(MessageEntity.class);
        assertEquals(1, messagesFromDB.size());

        final MessageEntity actualSavedMessage = messagesFromDB.get(0);
        final MessageEntity expectedSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-11-15T14:56:43Z"))
                .latitude(57.406944F)
                .longitude(39.548332F)
                .speed(100)
                .course(15)
                .altitude(10)
                .amountSatellite(1)
                .gsmLevel(16)
                .onboardVoltage(31)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(INCORRECT)
                .gpsOdometer(0.)
                .ignition(0)
                .engineTime(0)
                .shock(0)
                .build();

        checkEqualsExceptId(expectedSavedMessage, actualSavedMessage);
    }

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "UPDATE device_state SET last_message_id = NULL", executionPhase = AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM message", executionPhase = AFTER_TEST_METHOD)
    public void dataPackageShouldBeHandledAsIncorrectBecauseOfAmountOfSatelliteWasNotReceivedAndThereIsNoPreviousValidMessageToFix()
            throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final String request = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;NA;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "21:1:1,secondparam:2:65.4321,VPWR:2:31,"
                //HDOP, VDOP, PDOP
                + "122:1:5,123:1:6,124:1:7,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0"
                + "\r\n";

        final String actualResponse = this.client.doRequest(request).get();
        final String expectedResponse = "#AD#1\r\n";
        assertEquals(expectedResponse, actualResponse);

        final List<MessageEntity> messagesFromDB = super.findEntities(MessageEntity.class);
        assertEquals(1, messagesFromDB.size());

        final MessageEntity actualSavedMessage = messagesFromDB.get(0);
        final MessageEntity expectedSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-11-15T14:56:43Z"))
                .latitude(57.406944F)
                .longitude(39.548332F)
                .speed(100)
                .course(15)
                .altitude(10)
                .amountSatellite(0)
                .gsmLevel(16)
                .onboardVoltage(31)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(INCORRECT)
                .gpsOdometer(0.)
                .ignition(0)
                .engineTime(0)
                .shock(0.)
                .build();

        checkEqualsExceptId(expectedSavedMessage, actualSavedMessage);
    }

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "UPDATE device_state SET last_message_id = NULL", executionPhase = AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM message", executionPhase = AFTER_TEST_METHOD)
    public void dataPackageShouldBeHandledAsIncorrectBecauseOfNotValidHDOPAndThereIsNoPreviousValidMessageToFix()
            throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final String request = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;20;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "21:1:1,secondparam:2:65.4321,VPWR:2:31,"
                //HDOP, VDOP, PDOP
                + "122:1:8,123:1:6,124:1:7,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0"
                + "\r\n";

        final String actualResponse = this.client.doRequest(request).get();
        final String expectedResponse = "#AD#1\r\n";
        assertEquals(expectedResponse, actualResponse);

        final List<MessageEntity> messagesFromDB = super.findEntities(MessageEntity.class);
        assertEquals(1, messagesFromDB.size());

        final MessageEntity actualSavedMessage = messagesFromDB.get(0);
        final MessageEntity expectedSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-11-15T14:56:43Z"))
                .latitude(57.406944F)
                .longitude(39.548332F)
                .speed(100)
                .course(15)
                .altitude(10)
                .amountSatellite(20)
                .gsmLevel(16)
                .onboardVoltage(31)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(INCORRECT)
                .gpsOdometer(0.)
                .ignition(0)
                .engineTime(0)
                .shock(0.)
                .build();

        checkEqualsExceptId(expectedSavedMessage, actualSavedMessage);
    }

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "UPDATE device_state SET last_message_id = NULL", executionPhase = AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM message", executionPhase = AFTER_TEST_METHOD)
    public void dataPackageShouldBeHandledAsIncorrectBecauseOfHDOPWasNotReceivedAndThereIsNoPreviousValidMessageToFix()
            throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final String request = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;20;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "21:1:1,secondparam:2:65.4321,VPWR:2:31,"
                //VDOP, PDOP
                + "123:1:6,124:1:7,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0"
                + "\r\n";

        final String actualResponse = this.client.doRequest(request).get();
        final String expectedResponse = "#AD#1\r\n";
        assertEquals(expectedResponse, actualResponse);

        final List<MessageEntity> messagesFromDB = super.findEntities(MessageEntity.class);
        assertEquals(1, messagesFromDB.size());

        final MessageEntity actualSavedMessage = messagesFromDB.get(0);
        final MessageEntity expectedSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-11-15T14:56:43Z"))
                .latitude(57.406944F)
                .longitude(39.548332F)
                .speed(100)
                .course(15)
                .altitude(10)
                .amountSatellite(20)
                .gsmLevel(16)
                .onboardVoltage(31)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(INCORRECT)
                .gpsOdometer(0.)
                .ignition(0)
                .engineTime(0)
                .shock(0.)
                .build();

        checkEqualsExceptId(expectedSavedMessage, actualSavedMessage);
    }

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "UPDATE device_state SET last_message_id = NULL", executionPhase = AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM message", executionPhase = AFTER_TEST_METHOD)
    public void dataPackageShouldBeHandledAsIncorrectBecauseOfNotValidVDOPAndThereIsNoPreviousValidMessageToFix()
            throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final String request = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;20;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "21:1:1,secondparam:2:65.4321,VPWR:2:31,"
                //HDOP, VDOP, PDOP
                + "122:1:7,123:1:8,124:1:7,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0"
                + "\r\n";

        final String actualResponse = this.client.doRequest(request).get();
        final String expectedResponse = "#AD#1\r\n";
        assertEquals(expectedResponse, actualResponse);

        final List<MessageEntity> messagesFromDB = super.findEntities(MessageEntity.class);
        assertEquals(1, messagesFromDB.size());

        final MessageEntity actualSavedMessage = messagesFromDB.get(0);
        final MessageEntity expectedSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-11-15T14:56:43Z"))
                .latitude(57.406944F)
                .longitude(39.548332F)
                .speed(100)
                .course(15)
                .altitude(10)
                .amountSatellite(20)
                .gsmLevel(16)
                .onboardVoltage(31)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(INCORRECT)
                .gpsOdometer(0.)
                .ignition(0)
                .engineTime(0)
                .shock(0.)
                .build();

        checkEqualsExceptId(expectedSavedMessage, actualSavedMessage);
    }

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "UPDATE device_state SET last_message_id = NULL", executionPhase = AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM message", executionPhase = AFTER_TEST_METHOD)
    public void dataPackageShouldBeHandledAsIncorrectBecauseOfVDOPWasNotReceivedAndThereIsNoPreviousValidMessageToFix()
            throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final String request = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;20;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "21:1:1,secondparam:2:65.4321,VPWR:2:31,"
                //HDOP, PDOP
                + "122:1:7,124:1:7,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0"
                + "\r\n";

        final String actualResponse = this.client.doRequest(request).get();
        final String expectedResponse = "#AD#1\r\n";
        assertEquals(expectedResponse, actualResponse);

        final List<MessageEntity> messagesFromDB = super.findEntities(MessageEntity.class);
        assertEquals(1, messagesFromDB.size());

        final MessageEntity actualSavedMessage = messagesFromDB.get(0);
        final MessageEntity expectedSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-11-15T14:56:43Z"))
                .latitude(57.406944F)
                .longitude(39.548332F)
                .speed(100)
                .course(15)
                .altitude(10)
                .amountSatellite(20)
                .gsmLevel(16)
                .onboardVoltage(31)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(INCORRECT)
                .gpsOdometer(0.)
                .ignition(0)
                .engineTime(0)
                .shock(0.)
                .build();

        checkEqualsExceptId(expectedSavedMessage, actualSavedMessage);
    }

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "UPDATE device_state SET last_message_id = NULL", executionPhase = AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM message", executionPhase = AFTER_TEST_METHOD)
    public void dataPackageShouldBeHandledAsIncorrectBecauseOfNotValidPDOPAndThereIsNoPreviousValidMessageToFix()
            throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final String request = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;20;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "21:1:1,secondparam:2:65.4321,VPWR:2:31,"
                //HDOP, VDOP, PDOP
                + "122:1:7,123:1:6,124:1:8,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0"
                + "\r\n";

        final String actualResponse = this.client.doRequest(request).get();
        final String expectedResponse = "#AD#1\r\n";
        assertEquals(expectedResponse, actualResponse);

        final List<MessageEntity> messagesFromDB = super.findEntities(MessageEntity.class);
        assertEquals(1, messagesFromDB.size());

        final MessageEntity actualSavedMessage = messagesFromDB.get(0);
        final MessageEntity expectedSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-11-15T14:56:43Z"))
                .latitude(57.406944F)
                .longitude(39.548332F)
                .speed(100)
                .course(15)
                .altitude(10)
                .amountSatellite(20)
                .gsmLevel(16)
                .onboardVoltage(31)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(INCORRECT)
                .gpsOdometer(0.)
                .ignition(0)
                .engineTime(0)
                .shock(0.)
                .build();

        checkEqualsExceptId(expectedSavedMessage, actualSavedMessage);
    }

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "UPDATE device_state SET last_message_id = NULL", executionPhase = AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM message", executionPhase = AFTER_TEST_METHOD)
    public void dataPackageShouldBeHandledAsIncorrectBecauseOfPDOPWasNotReceivedAndThereIsNoPreviousValidMessageToFix()
            throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final String request = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;20;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "21:1:1,secondparam:2:65.4321,VPWR:2:31,"
                //HDOP, VDOP
                + "122:1:7,123:1:6,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0"
                + "\r\n";

        final String actualResponse = this.client.doRequest(request).get();
        final String expectedResponse = "#AD#1\r\n";
        assertEquals(expectedResponse, actualResponse);

        final List<MessageEntity> messagesFromDB = super.findEntities(MessageEntity.class);
        assertEquals(1, messagesFromDB.size());

        final MessageEntity actualSavedMessage = messagesFromDB.get(0);
        final MessageEntity expectedSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-11-15T14:56:43Z"))
                .latitude(57.406944F)
                .longitude(39.548332F)
                .speed(100)
                .course(15)
                .altitude(10)
                .amountSatellite(20)
                .gsmLevel(16)
                .onboardVoltage(31)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(INCORRECT)
                .gpsOdometer(0.)
                .ignition(0)
                .engineTime(0)
                .shock(0.)
                .build();

        checkEqualsExceptId(expectedSavedMessage, actualSavedMessage);
    }

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "UPDATE device_state SET last_message_id = NULL", executionPhase = AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM message", executionPhase = AFTER_TEST_METHOD)
    public void dataPackageShouldBeHandledAsIncorrectBecauseOfNotValidDateTimeWasReceived()
            throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final String request = "#D#151119;145643;5544.6025;N;03739.6834;E;100;15;10;20;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "21:1:1,secondparam:2:65.4321,VPWR:2:31,"
                //HDOP, VDOP, PDOP
                + "122:1:7,123:1:6,124:1:7,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0"
                + "\r\n";

        final String actualResponse = this.client.doRequest(request).get();
        final String expectedResponse = "#AD#1\r\n";
        assertEquals(expectedResponse, actualResponse);

        final List<MessageEntity> messagesFromDB = super.findEntities(MessageEntity.class);
        assertEquals(1, messagesFromDB.size());

        final MessageEntity actualSavedMessage = messagesFromDB.get(0);
        final MessageEntity expectedSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2019-11-15T14:56:43Z"))
                .latitude(57.406944F)
                .longitude(39.548332F)
                .speed(100)
                .course(15)
                .altitude(10)
                .amountSatellite(20)
                .gsmLevel(16)
                .onboardVoltage(31)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(INCORRECT)
                .gpsOdometer(0.)
                .ignition(0)
                .engineTime(0)
                .shock(0.)
                .build();

        checkEqualsExceptId(expectedSavedMessage, actualSavedMessage);
    }

    //message is fixed by taking latitude, longitude, amount of satellites from previous valid message
    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "INSERT INTO message("
            + "time, latitude, longitude, speed, course, altitude, amount_satellite, "
            + "device_id, gsm_level, onboard_voltage, eco_cornering, eco_acceleration, eco_braking, type, "
            + "gps_odometer, ignition, engine_time, shock) "
            + "VALUES ('2022-11-15 14:56:43', 4.4, 5.5, 10, 11, 12, 13, 25551, 44, 1500, 1600, 1700, 1800, 'VALID', "
            + "100, 1, 1000, 2000)")
    @Sql(statements = "UPDATE device_state SET last_message_id = NULL", executionPhase = AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM message", executionPhase = AFTER_TEST_METHOD)
    public void dataPackageWithNotValidAmountSatelliteShouldBeHandledAsValidByFixingByPreviousValidMessage()
            throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final String requestDataPackage = "#D#151122;145645;5544.6026;N;03739.6835;E;100;15;10;1;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "21:1:1,secondparam:2:65.4321,VPWR:2:3,"
                //HDOP, VDOP, PDOP
                + "122:1:5,123:1:6,124:1:7,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0"
                + "\r\n";
        final String actualResponseDataPackage = this.client.doRequest(requestDataPackage).get();
        final String expectedResponseDataPackage = "#AD#1\r\n";
        assertEquals(expectedResponseDataPackage, actualResponseDataPackage);

        final MessageEntity actualSavedMessage = this.findLastInsertedMessage();
        final MessageEntity expectedSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-11-15T14:56:45Z"))
                .latitude(4.4F)
                .longitude(5.5F)
                .speed(100)
                .course(15)
                .altitude(10)
                .amountSatellite(13)
                .gsmLevel(16)
                .onboardVoltage(3)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(VALID)
                .gpsOdometer(0.002)
                .ignition(0)
                .engineTime(1000)
                .shock(5.)
                .build();

        checkEqualsExceptId(expectedSavedMessage, actualSavedMessage);
    }

    //message is fixed by taking latitude, longitude, amount of satellites from previous valid message
    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "INSERT INTO message("
            + "time, latitude, longitude, speed, course, altitude, amount_satellite, "
            + "device_id, gsm_level, onboard_voltage, eco_cornering, eco_acceleration, eco_braking, type, "
            + "gps_odometer, ignition, engine_time, shock) "
            + "VALUES ('2022-11-15 14:56:43', 4.4, 5.5, 10, 11, 12, 13, 25551, 44, 1500, 1600, 1700, 1800, 'VALID', "
            + "100, 1, 1000, 2000)")
    @Sql(statements = "UPDATE device_state SET last_message_id = NULL", executionPhase = AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM message", executionPhase = AFTER_TEST_METHOD)
    public void dataPackageWithNotDefinedAmountSatelliteShouldBeHandledAsValidByFixingByPreviousValidMessage()
            throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final String requestDataPackage = "#D#151122;145645;5544.6026;N;03739.6835;E;100;15;10;NA;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "21:1:1,secondparam:2:65.4321,VPWR:2:3,"
                //HDOP, VDOP, PDOP
                + "122:1:5,123:1:6,124:1:7,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0"
                + "\r\n";
        final String actualResponseDataPackage = this.client.doRequest(requestDataPackage).get();
        final String expectedResponseDataPackage = "#AD#1\r\n";
        assertEquals(expectedResponseDataPackage, actualResponseDataPackage);

        final MessageEntity actualSavedMessage = this.findLastInsertedMessage();
        final MessageEntity expectedSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-11-15T14:56:45Z"))
                .latitude(4.4F)
                .longitude(5.5F)
                .speed(100)
                .course(15)
                .altitude(10)
                .amountSatellite(13)
                .gsmLevel(16)
                .onboardVoltage(3)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(VALID)
                .gpsOdometer(0.002)
                .ignition(0)
                .engineTime(1000)
                .shock(5.)
                .build();

        checkEqualsExceptId(expectedSavedMessage, actualSavedMessage);
    }

    //message is fixed by taking latitude, longitude, amount of satellites from previous valid message
    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "INSERT INTO message("
            + "time, latitude, longitude, speed, course, altitude, amount_satellite, "
            + "device_id, gsm_level, onboard_voltage, eco_cornering, eco_acceleration, eco_braking, type, "
            + "gps_odometer, ignition, engine_time, shock) "
            + "VALUES ('2022-11-15 14:56:43', 4.4, 5.5, 10, 11, 12, 13, 25551, 44, 1500, 1600, 1700, 1800, 'VALID', "
            + "100, 1, 1000, 2000)")
    @Sql(statements = "UPDATE device_state SET last_message_id = NULL", executionPhase = AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM message", executionPhase = AFTER_TEST_METHOD)
    public void dataPackageWithNotValidHDOPShouldBeHandledAsValidByFixingByPreviousValidMessage()
            throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final String requestDataPackage = "#D#151122;145645;5544.6026;N;03739.6835;E;100;15;10;10;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "21:1:1,secondparam:2:65.4321,VPWR:2:3,"
                //HDOP, VDOP, PDOP
                + "122:1:8,123:1:6,124:1:7,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0"
                + "\r\n";
        final String actualResponseDataPackage = this.client.doRequest(requestDataPackage).get();
        final String expectedResponseDataPackage = "#AD#1\r\n";
        assertEquals(expectedResponseDataPackage, actualResponseDataPackage);

        final MessageEntity actualSavedMessage = this.findLastInsertedMessage();
        final MessageEntity expectedSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-11-15T14:56:45Z"))
                .latitude(4.4F)
                .longitude(5.5F)
                .speed(100)
                .course(15)
                .altitude(10)
                .amountSatellite(13)
                .gsmLevel(16)
                .onboardVoltage(3)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(VALID)
                .gpsOdometer(0.002)
                .ignition(0)
                .engineTime(1000)
                .shock(5.)
                .build();

        checkEqualsExceptId(expectedSavedMessage, actualSavedMessage);
    }

    //message is fixed by taking latitude, longitude, amount of satellites from previous valid message
    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "INSERT INTO message("
            + "time, latitude, longitude, speed, course, altitude, amount_satellite, "
            + "device_id, gsm_level, onboard_voltage, eco_cornering, eco_acceleration, eco_braking, type, "
            + "gps_odometer, ignition, engine_time, shock) "
            + "VALUES ('2022-11-15 14:56:43', 4.4, 5.5, 10, 11, 12, 13, 25551, 44, 1500, 1600, 1700, 1800, 'VALID', "
            + "100, 1, 1000, 2000)")
    @Sql(statements = "UPDATE device_state SET last_message_id = NULL", executionPhase = AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM message", executionPhase = AFTER_TEST_METHOD)
    public void dataPackageWithNotDefinedHDOPShouldBeHandledAsValidByFixingByPreviousValidMessage()
            throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final String requestDataPackage = "#D#151122;145645;5544.6026;N;03739.6835;E;100;15;10;10;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "21:1:1,secondparam:2:65.4321,VPWR:2:3,"
                //VDOP, PDOP
                + "123:1:6,124:1:7,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0"
                + "\r\n";
        final String actualResponseDataPackage = this.client.doRequest(requestDataPackage).get();
        final String expectedResponseDataPackage = "#AD#1\r\n";
        assertEquals(expectedResponseDataPackage, actualResponseDataPackage);

        final MessageEntity actualSavedMessage = this.findLastInsertedMessage();
        final MessageEntity expectedSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-11-15T14:56:45Z"))
                .latitude(4.4F)
                .longitude(5.5F)
                .speed(100)
                .course(15)
                .altitude(10)
                .amountSatellite(13)
                .gsmLevel(16)
                .onboardVoltage(3)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(VALID)
                .gpsOdometer(0.002)
                .ignition(0)
                .engineTime(1000)
                .shock(5.)
                .build();

        checkEqualsExceptId(expectedSavedMessage, actualSavedMessage);
    }

    //message is fixed by taking latitude, longitude, amount of satellites from previous valid message
    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "INSERT INTO message("
            + "time, latitude, longitude, speed, course, altitude, amount_satellite, "
            + "device_id, gsm_level, onboard_voltage, eco_cornering, eco_acceleration, eco_braking, type, "
            + "gps_odometer, ignition, engine_time, shock) "
            + "VALUES ('2022-11-15 14:56:43', 4.4, 5.5, 10, 11, 12, 13, 25551, 44, 1500, 1600, 1700, 1800, 'VALID', "
            + "100, 1, 1000, 2000)")
    @Sql(statements = "UPDATE device_state SET last_message_id = NULL", executionPhase = AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM message", executionPhase = AFTER_TEST_METHOD)
    public void dataPackageWithNotValidVDOPShouldBeHandledAsValidByFixingByPreviousValidMessage()
            throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final String requestDataPackage = "#D#151122;145645;5544.6026;N;03739.6835;E;100;15;10;10;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "21:1:1,secondparam:2:65.4321,VPWR:2:3,"
                //HDOP, VDOP, PDOP
                + "122:1:6,123:1:8,124:1:7,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0"
                + "\r\n";
        final String actualResponseDataPackage = this.client.doRequest(requestDataPackage).get();
        final String expectedResponseDataPackage = "#AD#1\r\n";
        assertEquals(expectedResponseDataPackage, actualResponseDataPackage);

        final MessageEntity actualSavedMessage = this.findLastInsertedMessage();
        final MessageEntity expectedSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-11-15T14:56:45Z"))
                .latitude(4.4F)
                .longitude(5.5F)
                .speed(100)
                .course(15)
                .altitude(10)
                .amountSatellite(13)
                .gsmLevel(16)
                .onboardVoltage(3)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(VALID)
                .gpsOdometer(0.002)
                .ignition(0)
                .engineTime(1000)
                .shock(5.)
                .build();

        checkEqualsExceptId(expectedSavedMessage, actualSavedMessage);
    }

    //message is fixed by taking latitude, longitude, amount of satellites from previous valid message
    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "INSERT INTO message("
            + "time, latitude, longitude, speed, course, altitude, amount_satellite, "
            + "device_id, gsm_level, onboard_voltage, eco_cornering, eco_acceleration, eco_braking, type, "
            + "gps_odometer, ignition, engine_time, shock) "
            + "VALUES ('2022-11-15 14:56:43', 4.4, 5.5, 10, 11, 12, 13, 25551, 44, 1500, 1600, 1700, 1800, 'VALID', "
            + "100, 1, 1000, 2000)")
    @Sql(statements = "UPDATE device_state SET last_message_id = NULL", executionPhase = AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM message", executionPhase = AFTER_TEST_METHOD)
    public void dataPackageWithNotDefinedVDOPShouldBeHandledAsValidByFixingByPreviousValidMessage()
            throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final String requestDataPackage = "#D#151122;145645;5544.6026;N;03739.6835;E;100;15;10;10;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "21:1:1,secondparam:2:65.4321,VPWR:2:3,"
                //HDOP, PDOP
                + "122:1:6,124:1:7,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0"
                + "\r\n";
        final String actualResponseDataPackage = this.client.doRequest(requestDataPackage).get();
        final String expectedResponseDataPackage = "#AD#1\r\n";
        assertEquals(expectedResponseDataPackage, actualResponseDataPackage);

        final MessageEntity actualSavedMessage = this.findLastInsertedMessage();
        final MessageEntity expectedSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-11-15T14:56:45Z"))
                .latitude(4.4F)
                .longitude(5.5F)
                .speed(100)
                .course(15)
                .altitude(10)
                .amountSatellite(13)
                .gsmLevel(16)
                .onboardVoltage(3)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(VALID)
                .gpsOdometer(0.002)
                .ignition(0)
                .engineTime(1000)
                .shock(5.)
                .build();

        checkEqualsExceptId(expectedSavedMessage, actualSavedMessage);
    }

    //message is fixed by taking latitude, longitude, amount of satellites from previous valid message
    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "INSERT INTO message("
            + "time, latitude, longitude, speed, course, altitude, amount_satellite, "
            + "device_id, gsm_level, onboard_voltage, eco_cornering, eco_acceleration, eco_braking, type, "
            + "gps_odometer, ignition, engine_time, shock) "
            + "VALUES ('2022-11-15 14:56:43', 4.4, 5.5, 10, 11, 12, 13, 25551, 44, 1500, 1600, 1700, 1800, 'VALID', "
            + "100, 1, 1000, 2000)")
    @Sql(statements = "UPDATE device_state SET last_message_id = NULL", executionPhase = AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM message", executionPhase = AFTER_TEST_METHOD)
    public void dataPackageWithNotValidPDOPShouldBeHandledAsValidByFixingByPreviousValidMessage()
            throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final String requestDataPackage = "#D#151122;145645;5544.6026;N;03739.6835;E;100;15;10;10;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "21:1:1,secondparam:2:65.4321,VPWR:2:3,"
                //HDOP, VDOP, PDOP
                + "122:1:6,123:1:7,124:1:8,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0"
                + "\r\n";
        final String actualResponseDataPackage = this.client.doRequest(requestDataPackage).get();
        final String expectedResponseDataPackage = "#AD#1\r\n";
        assertEquals(expectedResponseDataPackage, actualResponseDataPackage);

        final MessageEntity actualSavedMessage = this.findLastInsertedMessage();
        final MessageEntity expectedSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-11-15T14:56:45Z"))
                .latitude(4.4F)
                .longitude(5.5F)
                .speed(100)
                .course(15)
                .altitude(10)
                .amountSatellite(13)
                .gsmLevel(16)
                .onboardVoltage(3)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(VALID)
                .gpsOdometer(0.002)
                .ignition(0)
                .engineTime(1000)
                .shock(5.)
                .build();

        checkEqualsExceptId(expectedSavedMessage, actualSavedMessage);
    }

    //message is fixed by taking latitude, longitude, amount of satellites from previous valid message
    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "INSERT INTO message("
            + "time, latitude, longitude, speed, course, altitude, amount_satellite, "
            + "device_id, gsm_level, onboard_voltage, eco_cornering, eco_acceleration, eco_braking, type, "
            + "gps_odometer, ignition, engine_time, shock) "
            + "VALUES ('2022-11-15 14:56:43', 4.4, 5.5, 10, 11, 12, 13, 25551, 44, 1500, 1600, 1700, 1800, 'VALID', "
            + "100, 1, 1000, 2000)")
    @Sql(statements = "UPDATE device_state SET last_message_id = NULL", executionPhase = AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM message", executionPhase = AFTER_TEST_METHOD)
    public void dataPackageWithNotDefinedPDOPShouldBeHandledAsValidByFixingByPreviousValidMessage()
            throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final String requestDataPackage = "#D#151122;145645;5544.6026;N;03739.6835;E;100;15;10;10;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "21:1:1,secondparam:2:65.4321,VPWR:2:3,"
                //HDOP, VDOP
                + "122:1:6,123:1:7,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0"
                + "\r\n";
        final String actualResponseDataPackage = this.client.doRequest(requestDataPackage).get();
        final String expectedResponseDataPackage = "#AD#1\r\n";
        assertEquals(expectedResponseDataPackage, actualResponseDataPackage);

        final MessageEntity actualSavedMessage = this.findLastInsertedMessage();
        final MessageEntity expectedSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-11-15T14:56:45Z"))
                .latitude(4.4F)
                .longitude(5.5F)
                .speed(100)
                .course(15)
                .altitude(10)
                .amountSatellite(13)
                .gsmLevel(16)
                .onboardVoltage(3)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(VALID)
                .gpsOdometer(0.002)
                .ignition(0)
                .engineTime(1000)
                .shock(5.)
                .build();

        checkEqualsExceptId(expectedSavedMessage, actualSavedMessage);
    }

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "INSERT INTO message("
            + "time, latitude, longitude, speed, course, altitude, amount_satellite, "
            + "device_id, gsm_level, onboard_voltage, eco_cornering, eco_acceleration, eco_braking, type, "
            + "gps_odometer, ignition, engine_time, shock) "
            + "VALUES ('2022-11-15 14:56:43', 4.4, 5.5, 10, 11, 12, 13, 25551, 44, 1500, 1600, 1700, 1800, 'VALID', "
            + "100, 1, 1000, 2000)")
    @Sql(statements = "UPDATE device_state SET last_message_id = NULL", executionPhase = AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM message", executionPhase = AFTER_TEST_METHOD)
    public void dataPackageShouldBeHandledAsWrongOrderBecauseOfWrongOrderDateTime()
            throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final String requestDataPackage = "#D#151122;145642;5544.6026;N;03739.6835;E;100;15;10;10;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "21:1:1,secondparam:2:65.4321,VPWR:2:3,"
                //HDOP, VDOP, PDOP
                + "122:1:6,123:1:7,124:1:6,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0"
                + "\r\n";
        final String actualResponseDataPackage = this.client.doRequest(requestDataPackage).get();
        final String expectedResponseDataPackage = "#AD#1\r\n";
        assertEquals(expectedResponseDataPackage, actualResponseDataPackage);

        final MessageEntity actualSavedMessage = this.findLastInsertedMessage();
        final MessageEntity expectedSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-11-15T14:56:42Z"))
                .latitude(57.407223F)
                .longitude(39.54861F)
                .speed(100)
                .course(15)
                .altitude(10)
                .amountSatellite(10)
                .gsmLevel(16)
                .onboardVoltage(3)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(WRONG_ORDER)
                .gpsOdometer(0.)
                .ignition(0)
                .engineTime(0)
                .shock(0.)
                .build();

        checkEqualsExceptId(expectedSavedMessage, actualSavedMessage);
    }

    @Test
    public void notValidDataPackageShouldNotBeHandled()
            throws Exception {
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
    @Sql(statements = "UPDATE device_state SET last_message_id = NULL", executionPhase = AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM message", executionPhase = AFTER_TEST_METHOD)
    public void blackBoxPackageWithTwoValidMessageShouldBeHandledWithoutPreviousValidMessage()
            throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final String requestBlackBoxPackage = "#B#"
                + "101022;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "21:1:1,secondparam:2:65.4321,VPWR:2:31.5,"
                //HDOP, VDOP, PDOP
                + "122:1:6,123:1:7,124:1:6,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0"
                + "|"
                + "101022;145644;5544.6026;N;03739.6835;E;101;16;11;178;545.4555;18;19;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                //HDOP, VDOP, PDOP
                + "122:1:6,123:1:7,124:1:6,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0,"
                + "VPWR:2:31.5,"
                + "param-name:1:654322,param-name:2:66.4321,param-name:3:param-value\r\n";

        final String actualResponse = this.client.doRequest(requestBlackBoxPackage).get();
        final String expectedResponse = "#AB#2\r\n";
        assertEquals(expectedResponse, actualResponse);

        final List<MessageEntity> messagesFromDB = super.findEntities(MessageEntity.class);
        assertEquals(2, messagesFromDB.size());

        final MessageEntity actualFirstSavedMessage = messagesFromDB.get(0);
        final MessageEntity expectedFirstSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-10-10T14:56:43Z"))
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
                .type(VALID)
                .gpsOdometer(0.)
                .ignition(1)
                .engineTime(0)
                .shock(5)
                .build();
        checkEqualsExceptId(expectedFirstSavedMessage, actualFirstSavedMessage);

        final MessageEntity secondActualSavedMessage = messagesFromDB.get(1);
        final MessageEntity expectedSecondSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-10-10T14:56:44Z"))
                .latitude(57.406944F)
                .longitude(39.548332F)
                .speed(101)
                .course(16)
                .altitude(11)
                .amountSatellite(178)
                .gsmLevel(-1)
                .onboardVoltage(31.5F)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(VALID)
                .gpsOdometer(0.035)
                .ignition(1)
                .engineTime(1)
                .shock(5)
                .build();
        checkEqualsExceptId(expectedSecondSavedMessage, secondActualSavedMessage);
    }

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "UPDATE device_state SET last_message_id = NULL", executionPhase = AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM message", executionPhase = AFTER_TEST_METHOD)
    public void blackBoxPackageWithFirstCorrectAndSecondValidMessageShouldBeHandledWithoutPreviousValidMessage()
            throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        //first message is correct because of amount of satellites is 1
        final String requestBlackBoxPackage = "#B#"
                + "101022;145643;5544.6025;N;03739.6834;E;100;15;10;1;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "21:1:1,secondparam:2:65.4321,VPWR:2:31.5,"
                //HDOP, VDOP, PDOP
                + "122:1:6,123:1:7,124:1:6,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0"
                + "|"
                + "101022;145644;5544.6026;N;03739.6835;E;101;16;11;178;545.4555;18;19;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                //HDOP, VDOP, PDOP
                + "122:1:6,123:1:7,124:1:6,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0,"
                + "VPWR:2:31.5,"
                + "param-name:1:654322,param-name:2:66.4321,param-name:3:param-value\r\n";

        final String actualResponse = this.client.doRequest(requestBlackBoxPackage).get();
        final String expectedResponse = "#AB#2\r\n";
        assertEquals(expectedResponse, actualResponse);

        final List<MessageEntity> messagesFromDB = super.findEntities(MessageEntity.class);
        assertEquals(2, messagesFromDB.size());

        final MessageEntity actualFirstSavedMessage = messagesFromDB.get(0);
        final MessageEntity expectedFirstSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-10-10T14:56:43Z"))
                .latitude(57.406944F)
                .longitude(39.548332F)
                .speed(100)
                .course(15)
                .altitude(10)
                .amountSatellite(1)
                .gsmLevel(16)
                .onboardVoltage(31.5F)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(INCORRECT)
                .gpsOdometer(0.)
                .ignition(0)
                .engineTime(0)
                .shock(0)
                .build();
        checkEqualsExceptId(expectedFirstSavedMessage, actualFirstSavedMessage);

        final MessageEntity secondActualSavedMessage = messagesFromDB.get(1);
        final MessageEntity expectedSecondSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-10-10T14:56:44Z"))
                .latitude(57.406944F)
                .longitude(39.548332F)
                .speed(101)
                .course(16)
                .altitude(11)
                .amountSatellite(178)
                .gsmLevel(-1)
                .onboardVoltage(31.5F)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(VALID)
                .gpsOdometer(0.)
                .ignition(1)
                .engineTime(0)
                .shock(5)
                .build();
        checkEqualsExceptId(expectedSecondSavedMessage, secondActualSavedMessage);
    }

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "UPDATE device_state SET last_message_id = NULL", executionPhase = AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM message", executionPhase = AFTER_TEST_METHOD)
    public void blackBoxPackageWithFirstValidAndSecondCorrectMessageShouldBeHandledWithoutPreviousValidMessage()
            throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        //second message is correct because of amount of satellites = 1
        final String requestBlackBoxPackage = "#B#"
                + "101022;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "21:1:1,secondparam:2:65.4321,VPWR:2:31.5,"
                //HDOP, VDOP, PDOP
                + "122:1:6,123:1:7,124:1:6,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0"
                + "|"
                + "101022;145644;5544.6026;N;03739.6835;E;101;16;11;1;545.4555;18;19;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                //HDOP, VDOP, PDOP
                + "122:1:6,123:1:7,124:1:6,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0,"
                + "VPWR:2:31.5,"
                + "param-name:1:654322,param-name:2:66.4321,param-name:3:param-value\r\n";

        final String actualResponse = this.client.doRequest(requestBlackBoxPackage).get();
        final String expectedResponse = "#AB#2\r\n";
        assertEquals(expectedResponse, actualResponse);

        final List<MessageEntity> messagesFromDB = super.findEntities(MessageEntity.class);
        assertEquals(2, messagesFromDB.size());

        final MessageEntity actualFirstSavedMessage = messagesFromDB.get(0);
        final MessageEntity expectedFirstSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-10-10T14:56:43Z"))
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
                .type(VALID)
                .gpsOdometer(0.)
                .ignition(1)
                .engineTime(0)
                .shock(5)
                .build();
        checkEqualsExceptId(expectedFirstSavedMessage, actualFirstSavedMessage);

        final MessageEntity secondActualSavedMessage = messagesFromDB.get(1);
        final MessageEntity expectedSecondSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-10-10T14:56:44Z"))
                .latitude(57.406944F)
                .longitude(39.548332F)
                .speed(101)
                .course(16)
                .altitude(11)
                .amountSatellite(177)
                .gsmLevel(-1)
                .onboardVoltage(31.5F)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(VALID)
                .gpsOdometer(0.)
                .ignition(1)
                .engineTime(1)
                .shock(5)
                .build();
        checkEqualsExceptId(expectedSecondSavedMessage, secondActualSavedMessage);
    }

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "UPDATE device_state SET last_message_id = NULL", executionPhase = AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM message", executionPhase = AFTER_TEST_METHOD)
    public void blackBoxPackageWithFirstValidAndSecondWrongOrderMessagesShouldBeHandledWithoutPreviousValidMessage()
            throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final String requestBlackBoxPackage = "#B#"
                + "101022;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "21:1:1,secondparam:2:65.4321,VPWR:2:31.5,"
                //HDOP, VDOP, PDOP
                + "122:1:6,123:1:7,124:1:6,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0"
                + "|"
                + "101022;145642;5544.6026;N;03739.6835;E;101;16;11;178;545.4555;18;19;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                //HDOP, VDOP, PDOP
                + "122:1:6,123:1:7,124:1:6,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0,"
                + "VPWR:2:31.5,"
                + "param-name:1:654322,param-name:2:66.4321,param-name:3:param-value\r\n";

        final String actualResponse = this.client.doRequest(requestBlackBoxPackage).get();
        final String expectedResponse = "#AB#2\r\n";
        assertEquals(expectedResponse, actualResponse);

        final List<MessageEntity> messagesFromDB = super.findEntities(MessageEntity.class);
        assertEquals(2, messagesFromDB.size());

        final MessageEntity actualFirstSavedMessage = messagesFromDB.get(0);
        final MessageEntity expectedFirstSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-10-10T14:56:43Z"))
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
                .type(VALID)
                .gpsOdometer(0.)
                .ignition(1)
                .engineTime(0)
                .shock(5)
                .build();
        checkEqualsExceptId(expectedFirstSavedMessage, actualFirstSavedMessage);

        final MessageEntity secondActualSavedMessage = messagesFromDB.get(1);
        final MessageEntity expectedSecondSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-10-10T14:56:42Z"))
                .latitude(57.406944F)
                .longitude(39.548332F)
                .speed(101)
                .course(16)
                .altitude(11)
                .amountSatellite(178)
                .gsmLevel(-1)
                .onboardVoltage(31.5F)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(WRONG_ORDER)
                .gpsOdometer(0)
                .ignition(0)
                .engineTime(0)
                .shock(0)
                .build();
        checkEqualsExceptId(expectedSecondSavedMessage, secondActualSavedMessage);
    }

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "INSERT INTO message("
            + "time, latitude, longitude, speed, course, altitude, amount_satellite, "
            + "device_id, gsm_level, onboard_voltage, eco_cornering, eco_acceleration, eco_braking, type, "
            + "gps_odometer, ignition, engine_time, shock) "
            + "VALUES ('2022-10-10 14:56:42', 4.4, 5.5, 10, 11, 12, 13, 25551, 44, 1500, 1600, 1700, 1800, 'VALID', "
            + "100, 1, 1000, 2000)")
    @Sql(statements = "UPDATE device_state SET last_message_id = NULL", executionPhase = AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM message", executionPhase = AFTER_TEST_METHOD)
    public void blackBoxPackageWithFirstCorrectAndSecondValidMessagesShouldBeHandledWithPreviousValidMessage()
            throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        //first message is correct because of amount of satellites is 1
        final String requestBlackBoxPackage = "#B#"
                + "101022;145643;5544.6025;N;03739.6834;E;100;15;10;1;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "21:1:1,secondparam:2:65.4321,VPWR:2:31.5,"
                //HDOP, VDOP, PDOP
                + "122:1:6,123:1:7,124:1:6,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0"
                + "|"
                + "101022;145644;5544.6026;N;03739.6835;E;101;16;11;178;545.4555;18;19;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                //HDOP, VDOP, PDOP
                + "122:1:6,123:1:7,124:1:6,"
                //ACC_X, ACC_Y, ACC_Z
                + "114:1:3,115:1:4,116:1:0,"
                + "VPWR:2:31.5,"
                + "param-name:1:654322,param-name:2:66.4321,param-name:3:param-value\r\n";

        final String actualResponse = this.client.doRequest(requestBlackBoxPackage).get();
        final String expectedResponse = "#AB#2\r\n";
        assertEquals(expectedResponse, actualResponse);

        final List<MessageEntity> messagesFromDB = super.findEntities(MessageEntity.class);
        assertEquals(3, messagesFromDB.size());

        final MessageEntity actualFirstSavedMessage = messagesFromDB.get(1);
        final MessageEntity expectedFirstSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-10-10T14:56:43Z"))
                .latitude(4.4F)
                .longitude(5.5F)
                .speed(100)
                .course(15)
                .altitude(10)
                .amountSatellite(13)
                .gsmLevel(16)
                .onboardVoltage(31.5F)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(VALID)
                .gpsOdometer(0.002)
                .ignition(1)
                .engineTime(1001)
                .shock(5)
                .build();
        checkEqualsExceptId(expectedFirstSavedMessage, actualFirstSavedMessage);

        final MessageEntity secondActualSavedMessage = messagesFromDB.get(2);
        final MessageEntity expectedSecondSavedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2022-10-10T14:56:44Z"))
                .latitude(57.406944F)
                .longitude(39.548332F)
                .speed(101)
                .course(16)
                .altitude(11)
                .amountSatellite(178)
                .gsmLevel(-1)
                .onboardVoltage(31.5F)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(VALID)
                .gpsOdometer(0.)
                .ignition(1)
                .engineTime(1002)
                .shock(5)
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
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "INSERT INTO command(id, text, status, device_id, type) VALUES (255, 'command', 'SENT', 25551, 'COMMAND')")
    @Sql(statements = "DELETE FROM command", executionPhase = AFTER_TEST_METHOD)
    public void responseCommandPackageShouldBeHandledSuccess()
            throws Exception {
        final String requestLoginPackage = "#L#355234055650192;NA\r\n";
        final String responseLoginPackage = this.client.doRequest(requestLoginPackage).get();
        assertEquals(RESPONSE_LOGIN_PACKAGE_SUCCESS_AUTHORIZATION, responseLoginPackage);

        final ChannelHandlerContext context = this.connectionManager.find(25551L).orElseThrow();

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
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .type(COMMAND)
                .build();
        checkEqualsExceptId(expectedSavedCommand, actualSavedCommand);
    }

    private static void checkEqualsExceptId(MessageEntity expected, MessageEntity actual) {
//        assertEquals(expected.getDevice().getId(), actual.getDevice().getId());
//        assertEquals(expected.getDatetime(), actual.getDatetime());
//        assertTrue(InboundPackageHandlingIT.checkEqualsWithInaccuracy(expected.getLatitude(), expected.getLatitude()));
//        assertTrue(InboundPackageHandlingIT.checkEqualsWithInaccuracy(expected.getLongitude(), actual.getLongitude()));
//        assertEquals(expected.getSpeed(), actual.getSpeed());
//        assertEquals(expected.getCourse(), actual.getCourse());
//        assertEquals(expected.getAltitude(), actual.getAltitude());
//        assertEquals(expected.getAmountSatellite(), actual.getAmountSatellite());
//        assertEquals(expected.getGsmLevel(), actual.getGsmLevel());
//        assertTrue(InboundPackageHandlingIT.checkEqualsWithInaccuracy(expected.getOnboardVoltage(), actual.getOnboardVoltage()));
//        assertTrue(InboundPackageHandlingIT.checkEqualsWithInaccuracy(expected.getEcoCornering(), actual.getEcoCornering()));
//        assertTrue(InboundPackageHandlingIT.checkEqualsWithInaccuracy(expected.getEcoAcceleration(), actual.getEcoAcceleration()));
//        assertTrue(InboundPackageHandlingIT.checkEqualsWithInaccuracy(expected.getEcoBraking(), actual.getEcoBraking()));
//        assertSame(expected.getType(), actual.getType());
//        assertTrue(checkEqualsWithInaccuracy(expected.getGpsOdometer(), actual.getGpsOdometer()));
//        assertEquals(expected.getIgnition(), actual.getIgnition());
//        assertEquals(expected.getEngineTime(), actual.getEngineTime());
//        assertTrue(checkEqualsWithInaccuracy(expected.getShock(), actual.getShock()));
    }

    private MessageEntity findLastInsertedMessage() {
        return super.entityManager
                .createQuery(HQL_QUERY_TO_FIND_LAST_INSERTED_MESSAGE, MessageEntity.class)
                .getSingleResult();
    }

    private static void checkEqualsExceptId(CommandEntity expected, CommandEntity actual) {
        assertEquals(expected.getText(), actual.getText());
        assertSame(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getDevice().getId(), actual.getDevice().getId());
        assertSame(expected.getStatus(), actual.getStatus());
    }

    private static void waitMessageDelivering()
            throws InterruptedException {
        SECONDS.sleep(WAIT_MESSAGE_DELIVERING_SECONDS_AMOUNT);
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
