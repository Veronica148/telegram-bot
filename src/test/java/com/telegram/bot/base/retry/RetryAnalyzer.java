package com.telegram.bot.base.retry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.util.RetryAnalyzerCount;

public class RetryAnalyzer extends RetryAnalyzerCount {

    private static final Logger LOG = LogManager.getRootLogger();
    private static final int MAX_RETRY_ATTEMPTS = 1;

    public RetryAnalyzer() {
        setCount(MAX_RETRY_ATTEMPTS);
    }

    @Override
    public boolean retryMethod(ITestResult testResult) {
        boolean willRetry = !testResult.isSuccess();
        if (willRetry) {
            LOG.info("Retrying {}, attempts left: {}", testResult.getMethod().getDescription(), getCount());
        }
        return willRetry;
    }
}