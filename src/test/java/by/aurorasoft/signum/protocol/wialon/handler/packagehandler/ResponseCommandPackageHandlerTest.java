package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.entity.CommandEntity;
import by.aurorasoft.signum.crud.model.entity.CommandEntity.Status;
import by.aurorasoft.signum.crud.service.CommandService;
import by.aurorasoft.signum.protocol.core.contextmanager.ContextManager;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.protocol.wialon.model.ResponseCommandPackage;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ResponseCommandPackageHandlerTest {

    @Mock
    private CommandService mockedCommandService;

    @Mock
    private ContextManager mockedContextManager;

    private PackageHandler packageHandler;

    @Captor
    private ArgumentCaptor<ChannelHandlerContext> contextArgumentCaptor;

    @Captor
    private ArgumentCaptor<Command> commandArgumentCaptor;

    @Captor
    private ArgumentCaptor<Status> commandStatusArgumentCaptor;

    @Before
    public void initializePackageHandler() {
        this.packageHandler = new ResponseCommandPackageHandler(null, this.mockedCommandService,
                this.mockedContextManager);
    }

    @Test
    public void packageWithSuccessStatusShouldBeHandled() {
        final Command givenCommand = mock(Command.class);
        when(this.mockedContextManager.findCommandWaitingResponse(any(ChannelHandlerContext.class)))
                .thenReturn(givenCommand);

        final ResponseCommandPackage givenPackage = new ResponseCommandPackage(ResponseCommandPackage.Status.SUCCESS);
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        this.packageHandler.doHandle(givenPackage, givenContext);

        verify(this.mockedContextManager, times(1))
                .findCommandWaitingResponse(this.contextArgumentCaptor.capture());
        verify(this.mockedContextManager, times(1))
                .onGetCommandResponse(this.contextArgumentCaptor.capture());
        verify(this.mockedCommandService, times(1))
                .updateByGivenStatus(
                        this.commandArgumentCaptor.capture(),
                        this.commandStatusArgumentCaptor.capture());

        assertEquals(List.of(givenContext, givenContext), this.contextArgumentCaptor.getAllValues());
        assertSame(givenCommand, this.commandArgumentCaptor.getValue());
        assertSame(CommandEntity.Status.SUCCESS, this.commandStatusArgumentCaptor.getValue());
    }

    @Test
    public void packageWithErrorStatusShouldBeHandled() {
        final Command givenCommand = mock(Command.class);
        when(this.mockedContextManager.findCommandWaitingResponse(any(ChannelHandlerContext.class)))
                .thenReturn(givenCommand);

        final ResponseCommandPackage givenPackage = new ResponseCommandPackage(ResponseCommandPackage.Status.ERROR);
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        this.packageHandler.doHandle(givenPackage, givenContext);

        verify(this.mockedContextManager, times(1))
                .findCommandWaitingResponse(this.contextArgumentCaptor.capture());
        verify(this.mockedContextManager, times(1))
                .onGetCommandResponse(this.contextArgumentCaptor.capture());
        verify(this.mockedCommandService, times(1))
                .updateByGivenStatus(
                        this.commandArgumentCaptor.capture(),
                        this.commandStatusArgumentCaptor.capture());

        assertEquals(List.of(givenContext, givenContext), this.contextArgumentCaptor.getAllValues());
        assertSame(givenCommand, this.commandArgumentCaptor.getValue());
        assertSame(CommandEntity.Status.ERROR, this.commandStatusArgumentCaptor.getValue());
    }

    @Test(expected = ClassCastException.class)
    public void packageShouldNotBeHandleBecauseOfNotSuitableType() {
        final Package givenPackage = new Package() {
        };
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        this.packageHandler.doHandle(givenPackage, givenContext);
    }
}
