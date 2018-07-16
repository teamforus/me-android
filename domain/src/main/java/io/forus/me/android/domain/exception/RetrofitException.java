package io.forus.me.android.domain.exception;

import java.io.IOException;

public abstract class RetrofitException extends RuntimeException{

    /** Identifies the event kind which triggered a {@link RetrofitException}. */
    public enum Kind {
        /** An {@link IOException} occurred while communicating to the server. */
        NETWORK,
        /** A non-200 HTTP status code was received from the server. */
        HTTP,
        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }

    protected final String url;
    protected final Kind kind;

    protected RetrofitException(String message, Throwable exception, String url, Kind kind){
        super(message, exception);
        this.url = url;
        this.kind = kind;
    }

    /** The request URL which produced the error. */
    public String getUrl() {
        return url;
    }

    /** The event kind which triggered this error. */
    public Kind getKind() {
        return kind;
    }

    /**
     * HTTP response body converted to specified {@code type}. {@code null} if there is no
     * response.
     *
     * @throws IOException if unable to convert the body to the specified {@code type}.
     */
    abstract public <T> T getErrorBodyAs(Class<T> type) throws IOException;
}
