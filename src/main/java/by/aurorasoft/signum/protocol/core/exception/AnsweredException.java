package by.aurorasoft.signum.protocol.core.exception;

public final class AnsweredException extends RuntimeException {
    private final String answerToClient;

    public AnsweredException(String answerToClient) {
        this.answerToClient = answerToClient;
    }

    public AnsweredException(String answerToClient, String description) {
        super(description);
        this.answerToClient = answerToClient;
    }

    public AnsweredException(String answerToClient, Exception cause) {
        super(cause);
        this.answerToClient = answerToClient;
    }

    public AnsweredException(String answerToClient, String description, Exception cause) {
        super(description, cause);
        this.answerToClient = answerToClient;
    }

    public String getAnswerToClient() {
        return this.answerToClient;
    }
}
