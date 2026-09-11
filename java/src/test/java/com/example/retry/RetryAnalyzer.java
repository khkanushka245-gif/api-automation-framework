package com.example.retry;

import com.example.config.EnvConfig;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.ConnectException;

/**
 * Retries a test ONLY when it failed for a transient/infrastructure reason —
 * never for an assertion failure.
 *
 * Why this matters: blindly retrying every failure hides real bugs. A genuine
 * assertion failure (wrong status code, missing field) must fail immediately.
 * Only flaky causes — dropped connections, socket timeouts, gateway 5xx — are
 * worth a bounded retry, because those are environmental, not defects.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private final int maxRetries = EnvConfig.maxRetries();
    private int attempts = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (attempts >= maxRetries) {
            return false;
        }
        if (isTransient(result.getThrowable())) {
            attempts++;
            return true;
        }
        // AssertionError and everything else: do not retry — fail fast.
        return false;
    }

    private boolean isTransient(Throwable t) {
        while (t != null) {
            if (t instanceof SocketTimeoutException
                    || t instanceof ConnectException
                    || t instanceof IOException) {
                return true;
            }
            String msg = t.getMessage();
            if (msg != null) {
                String m = msg.toLowerCase();
                // Rest Assured surfaces status expectation misses as text; only
                // treat 5xx / gateway / timeout wording as transient.
                if (m.contains("timed out") || m.contains("timeout")
                        || m.contains("connection reset")
                        || m.contains("502") || m.contains("503") || m.contains("504")
                        || m.contains("gateway") || m.contains("service unavailable")) {
                    return true;
                }
            }
            t = t.getCause();
        }
        return false;
    }
}
