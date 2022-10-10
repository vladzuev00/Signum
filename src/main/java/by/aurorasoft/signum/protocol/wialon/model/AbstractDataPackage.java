package by.aurorasoft.signum.protocol.wialon.model;

import by.aurorasoft.signum.dto.MessageDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public abstract class AbstractDataPackage implements Package {
    private final List<MessageDto> messages;
}
