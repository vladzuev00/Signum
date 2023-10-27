package by.aurorasoft.signum.protocol.wialon.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class AbstractResponseDataPackage implements Package {
    private final int countFixedMessages;
}
