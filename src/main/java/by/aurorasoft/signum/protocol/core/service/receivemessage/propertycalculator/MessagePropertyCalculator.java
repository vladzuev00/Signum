package by.aurorasoft.signum.protocol.core.service.receivemessage.propertycalculator;

import by.aurorasoft.signum.crud.model.dto.Message;

public interface MessagePropertyCalculator {
    void calculate(Message current);
    void calculate(Message current, Message previous);
}
