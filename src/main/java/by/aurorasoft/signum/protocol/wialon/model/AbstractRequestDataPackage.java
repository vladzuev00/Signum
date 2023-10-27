package by.aurorasoft.signum.protocol.wialon.model;

import by.aurorasoft.signum.crud.model.dto.Message;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public abstract class AbstractRequestDataPackage implements Package {
    private final List<Message> messages;
}
