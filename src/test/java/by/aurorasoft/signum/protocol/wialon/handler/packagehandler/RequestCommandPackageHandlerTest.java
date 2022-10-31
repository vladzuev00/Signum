package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.crud.model.entity.CommandEntity.Status;
import by.aurorasoft.signum.crud.model.entity.CommandEntity.Type;
import by.aurorasoft.signum.crud.service.CommandService;
import by.aurorasoft.signum.protocol.core.contextmanager.ContextManager;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.protocol.wialon.model.RequestCommandPackage;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static by.aurorasoft.signum.crud.model.entity.CommandEntity.Status.SUCCESS;
import static by.aurorasoft.signum.crud.model.entity.CommandEntity.Type.ANSWER;
import static by.aurorasoft.signum.crud.model.entity.DeviceEntity.Type.TRACKER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class RequestCommandPackageHandlerTest {

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

    @Captor
    private ArgumentCaptor<Type> commandTypeArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Before
    public void initializePackageHandler() {
        this.packageHandler = new RequestCommandPackageHandler(null, this.mockedCommandService, this.mockedContextManager);
    }

    @Test
    public void packageShouldBeHandled() {
        final Unit givenUnit = mock(Unit.class);
        final Device givenDevice = new Device(255L, "11112222333344445555", "448446945", TRACKER);
//        when(givenUnit.getDevice()).thenReturn(givenDevice);
//        when(this.mockedContextManager.findUnit(any(ChannelHandlerContext.class)))
//                .thenReturn(givenUnit);

        final RequestCommandPackage givenPackage = new RequestCommandPackage("message");
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        this.packageHandler.doHandle(givenPackage, givenContext);

//        verify(this.mockedContextManager, times(1))
//                .findUnit(this.contextArgumentCaptor.capture());
        verify(this.mockedCommandService, times(1)).save(
                this.commandArgumentCaptor.capture(),
                this.commandStatusArgumentCaptor.capture(),
                this.commandTypeArgumentCaptor.capture());
        verify(givenContext, times(1)).writeAndFlush(this.stringArgumentCaptor.capture());

        assertSame(givenContext, this.contextArgumentCaptor.getValue());
//        assertEquals(new Command(givenPackage.getMessage(), givenDevice), this.commandArgumentCaptor.getValue());
        assertSame(SUCCESS, this.commandStatusArgumentCaptor.getValue());
        assertSame(ANSWER, this.commandTypeArgumentCaptor.getValue());
    }

    @Test(expected = ClassCastException.class)
    public void packageShouldNotBeHandledBecauseOfType() {
        final Package givenPackage = new Package() {
        };
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        this.packageHandler.doHandle(givenPackage, givenContext);
    }
}
