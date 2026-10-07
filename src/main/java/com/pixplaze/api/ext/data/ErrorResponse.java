package com.pixplaze.api.ext.data;

/// Error body of every Pixplaze API endpoint except the OAuth ones
/// (those answer with [com.pixplaze.api.ext.data.oauth.OAuthErrorResponse]).
///
/// @param status    HTTP status code
/// @param timestamp ISO-8601 instant of the error
/// @param message   outside the development environment — only the HTTP status phrase
/// @param trace     stack trace; only in the development environment
/// @param path      request path
public record ErrorResponse(
        int status,
        String timestamp,
        String message,
        String trace,
        String path
) {
    /// Copy of this response with another HTTP status code.
    public ErrorResponse withStatus(int status) {
        return new ErrorResponse(status, timestamp, message, trace, path);
    }
}
