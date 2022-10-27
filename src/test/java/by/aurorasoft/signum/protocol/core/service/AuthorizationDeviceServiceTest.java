package by.aurorasoft.signum.protocol.core.service;

import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.crud.service.UnitService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class AuthorizationDeviceServiceTest {

    @Mock
    private UnitService mockedUnitService;

    private AuthorizationDeviceService authorizationDeviceService;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Before
    public void initializeAuthorizationDeviceService() {
        this.authorizationDeviceService = new AuthorizationDeviceService(this.mockedUnitService);
    }

    @Test
    public void deviceShouldAuthorize() {
        final Unit givenUnit = mock(Unit.class);
        when(this.mockedUnitService.findByTrackerImei(anyString())).thenReturn(Optional.of(givenUnit));

        final String givenImei = "11111222223333344444";
        final Optional<Unit> optionalUnit = this.authorizationDeviceService.authorize(givenImei);
        assertTrue(optionalUnit.isPresent());
        final Unit actual = optionalUnit.get();
        assertSame(givenUnit, actual);

        verify(this.mockedUnitService, times(1))
                .findByTrackerImei(this.stringArgumentCaptor.capture());

        assertEquals(givenImei, this.stringArgumentCaptor.getValue());
    }

    @Test
    public void deviceShouldNotAuthorize() {
        when(this.mockedUnitService.findByTrackerImei(anyString())).thenReturn(empty());

        final String givenImei = "11111222223333344444";
        final Optional<Unit> optionalUnit = this.authorizationDeviceService.authorize(givenImei);
        assertTrue(optionalUnit.isEmpty());
    }
}
