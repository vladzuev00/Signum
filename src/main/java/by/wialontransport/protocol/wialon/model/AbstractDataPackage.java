package by.wialontransport.protocol.wialon.model;

import by.wialontransport.entity.Message;
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
    private final List<Message> messages;
}
