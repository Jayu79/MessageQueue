package org.projects.strategies.retryStrategy;

import org.projects.exception.RetryLimitExhaustedException;
import org.projects.models.Message;
import org.projects.models.Subscription;

public class ExponentialBackoffRetryStrategy implements RetryStrategy{

    @Override
    public void push(Subscription subscription, Message message, int maxAttempts) throws RetryLimitExhaustedException, InterruptedException {

        int currentAttempt = 0;
        int previousSleepMs = 500;
        while(currentAttempt<maxAttempts){
            try{
                currentAttempt += 1;
                subscription.getCallback().call(subscription,message);
                return;
            } catch (Exception e) {
                Thread.sleep(previousSleepMs*2);
            }
        }
    }
}
