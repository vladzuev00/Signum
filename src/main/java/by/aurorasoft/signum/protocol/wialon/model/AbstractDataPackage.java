package by.aurorasoft.signum.protocol.wialon.model;

import by.aurorasoft.signum.entity.MessageEntity;
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
    private final List<MessageEntity> messages;
}
