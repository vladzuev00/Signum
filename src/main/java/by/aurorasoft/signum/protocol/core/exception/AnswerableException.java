package by.aurorasoft.signum.protocol.core.exception;

public final class AnswerableException extends RuntimeException {
    private final String answerToClient;

    public AnswerableException(String answerToClient) {
        this.answerToClient = answerToClient;
    }

    public AnswerableException(String answerToClient, String description) {
        super(description);
        this.answerToClient = answerToClient;
    }

    public AnswerableException(String answerToClient, Exception cause) {
        super(cause);
        this.answerToClient = answerToClient;
    }

    public AnswerableException(String answerToClient, String description, Exception cause) {
        super(description, cause);
        this.answerToClient = answerToClient;
    }

    public String getAnswerToClient() {
        return this.answerToClient;
    }
}
