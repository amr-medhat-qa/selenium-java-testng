package com.amrmedhat.qa.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Re-runs a failed test ONCE. The tests run against a public practice site that now and then drops a
 * connection or a session (seen: ERR_TIMED_OUT, lost login session). A real defect fails again on the
 * second attempt and is still reported — and every retry is visible in the Allure report ("Retries" tab),
 * so flaky behaviour is tracked, not hidden. Change the limit with -DretryCount=N (0 turns it off).
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRIES = Integer.getInteger("retryCount", 1);

    private int attempts;

    @Override
    public boolean retry(ITestResult result) {
        if (attempts < MAX_RETRIES) {
            attempts++;
            System.out.printf("RETRY %d/%d: %s.%s - %s%n", attempts, MAX_RETRIES,
                    result.getTestClass().getRealClass().getSimpleName(), result.getMethod().getMethodName(),
                    result.getThrowable() == null ? "" : result.getThrowable().getClass().getSimpleName());
            return true;
        }
        return false;
    }
}
