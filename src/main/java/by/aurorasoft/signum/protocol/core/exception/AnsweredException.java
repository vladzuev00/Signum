package by.aurorasoft.signum.protocol.core.exception;

import lombok.Getter;

public final class AnsweredException extends RuntimeException {

    @Getter
    private final String answer;

    public AnsweredException(String answer) {
        this.answer = answer;
    }

    public AnsweredException(String answer, String description) {
        super(description);
        this.answer = answer;
    }

    public AnsweredException(String answer, Exception cause) {
        super(cause);
        this.answer = answer;
    }

    public AnsweredException(String answer, String description, Exception cause) {
        super(description, cause);
        this.answer = answer;
    }
}
