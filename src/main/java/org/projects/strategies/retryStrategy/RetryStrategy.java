package org.projects.strategies.retryStrategy;

import org.projects.exception.RetryLimitExhaustedException;
import org.projects.models.Message;
import org.projects.models.Subscription;

public interface RetryStrategy {

    void push(Subscription subscription, Message message, int maxAttempts) throws RetryLimitExhaustedException, InterruptedException;
}
