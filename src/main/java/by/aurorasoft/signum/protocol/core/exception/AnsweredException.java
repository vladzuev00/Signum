package by.aurorasoft.signum.protocol.core.exception;

import by.aurorasoft.signum.protocol.wialon.model.Package;
import lombok.Getter;

public final class AnsweredException extends RuntimeException {

    @Getter
    private final Package answer;

    public AnsweredException(Package answer) {
        this.answer = answer;
    }

    public AnsweredException(Package answer, String description) {
        super(description);
        this.answer = answer;
    }

    public AnsweredException(Package answer, Exception cause) {
        super(cause);
        this.answer = answer;
    }

    public AnsweredException(Package answer, String description, Exception cause) {
        super(description, cause);
        this.answer = answer;
    }
}
