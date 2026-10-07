package com.pixplaze.api.ext;

import com.pixplaze.api.ext.data.auth.AuthorizationToken;
import com.pixplaze.api.ext.data.oauth.DeviceAuthorizationResponse;
import com.pixplaze.api.ext.data.oauth.OAuthError;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/// Started device flow: remembers its `client_id` and `device_code`, so the caller only shows the
/// codes and waits for the tokens.
///
/// @param <T> token pair the flow issues
public interface PendingAuthorization<T extends AuthorizationToken> {

    /// Polling interval when the server sends none, seconds (RFC 8628 §3.2).
    long DEFAULT_INTERVAL = 5;
    /// Interval increase on [OAuthError#SLOW_DOWN], seconds (RFC 8628 §3.5).
    long SLOW_DOWN_STEP = 5;

    /// Codes to show to the user: [DeviceAuthorizationResponse#userCode] and
    /// [DeviceAuthorizationResponse#verificationUriComplete].
    DeviceAuthorizationResponse response();

    /// Polls the token endpoint once. Blocking: never call it from the server main thread.
    ///
    /// @return the tokens, or empty while the user has not decided ([OAuthError#AUTHORIZATION_PENDING])
    /// @throws OAuthException on any other error, including [OAuthError#SLOW_DOWN]
    Optional<T> poll();

    /// [#await(Executor)] on the common pool.
    default CompletableFuture<T> await() {
        return await(ForkJoinPool.commonPool());
    }

    /// Polls every [DeviceAuthorizationResponse#interval] seconds until the tokens are issued.
    /// [OAuthError#SLOW_DOWN] increases the interval; any other error, e.g. [OAuthError#ACCESS_DENIED]
    /// or [OAuthError#EXPIRED_TOKEN], completes the future exceptionally with [OAuthException].
    /// Cancelling the future stops polling.
    ///
    /// @param executor runs the blocking polls
    default CompletableFuture<T> await(Executor executor) {
        final var result = new CompletableFuture<T>();
        final var interval = response().interval();
        pollLater(result, interval == null ? DEFAULT_INTERVAL : interval, executor);
        return result;
    }

    private void pollLater(CompletableFuture<T> result, long intervalSeconds, Executor executor) {
        CompletableFuture.runAsync(() -> {
            if (result.isDone()) {
                return;
            }
            try {
                poll().ifPresentOrElse(result::complete, () -> pollLater(result, intervalSeconds, executor));
            } catch (OAuthException e) {
                if (e.error() == OAuthError.SLOW_DOWN) {
                    pollLater(result, intervalSeconds + SLOW_DOWN_STEP, executor);
                } else {
                    result.completeExceptionally(e);
                }
            } catch (RuntimeException e) {
                result.completeExceptionally(e);
            }
        }, CompletableFuture.delayedExecutor(intervalSeconds, TimeUnit.SECONDS, executor));
    }
}
