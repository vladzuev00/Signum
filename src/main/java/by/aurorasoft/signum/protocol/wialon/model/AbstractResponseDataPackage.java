package by.aurorasoft.signum.protocol.wialon.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public abstract class AbstractResponseDataPackage implements Package {
    private final int countFixedMessages;
}
