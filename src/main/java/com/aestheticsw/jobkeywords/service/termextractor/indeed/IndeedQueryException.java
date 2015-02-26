package com.aestheticsw.jobkeywords.service.termextractor.indeed;

/**
 * Checked exception to handle invalid query expressions differently from IOExceptions or other
 * severe exceptions.
 */
public class IndeedQueryException extends Exception {

    private static final long serialVersionUID = -3312431007385262417L;

    public IndeedQueryException() {
        super();
    }

    public IndeedQueryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public IndeedQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    public IndeedQueryException(String message) {
        super(message);
    }

    public IndeedQueryException(Throwable cause) {
        super(cause);
    }

}
