package by.aurorasoft.signum.crud.service.message;

import by.aurorasoft.signum.crud.model.entity.MessageEntity;

interface MessagePropertyCalculator {
    void calculate(MessageEntity current);
    void calculate(MessageEntity last, MessageEntity current);
}
