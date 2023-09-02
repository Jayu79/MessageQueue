package org.projects.services;

import org.projects.controllers.MessageQueue;
import org.projects.exception.RetryLimitExhaustedException;
import org.projects.models.Message;
import org.projects.models.Server;
import org.projects.models.Subscription;
import org.projects.models.Topic;
import org.projects.strategies.retryStrategy.ExponentialBackoffRetryStrategy;
import org.projects.strategies.retryStrategy.RetryStrategy;
import org.projects.utils.StripedExecutor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

public class MessageQueueService {

    private StripedExecutor stripedExecutor;
    private int numberOfThreads = 16;
    private int maxRetryAttempts = 4;
    private Map<Topic, List<Subscription>> subscriptionMap;
    private RetryStrategy retryStrategy;
    private MessageQueue dlq;
    private Topic dlqErrorTopic;

    public MessageQueueService(){
        stripedExecutor = new StripedExecutor(numberOfThreads);
        subscriptionMap = new ConcurrentHashMap<>();
        this.retryStrategy = new ExponentialBackoffRetryStrategy();
        this.dlq = new MessageQueue(new MessageQueueService());
        this.dlqErrorTopic = new Topic();
    }

    private void publishMessageToQueue(Topic topic, Message message){
        topic.getMessages().add(message);
        for(Subscription subscription: subscriptionMap.get(topic)){

            try {
                retryStrategy.push(subscription,message,maxRetryAttempts);
            }
            catch (RetryLimitExhaustedException | InterruptedException exception){
                dlq.publish(dlqErrorTopic,message);
            } finally {
                subscription.getOffset().incrementAndGet();
            }
//
        }
    }

    public Future<Void> publish(Topic topic, Message message){

        return stripedExecutor.submit(topic.getId().hashCode()%numberOfThreads,
                ()-> publishMessageToQueue(topic,message)
                );

    }

    private void handleSubscription(Subscription subscription,Topic topic){

        subscriptionMap.get(topic).add(subscription);
        for(Message message: topic.getMessages()){
            subscription.getCallback().call(
                    subscription,message
            );
            subscription.getOffset().incrementAndGet();
        }

    }

    public Future<Void> subscribe(Subscription subscription, Topic topic){
        return stripedExecutor.submit(
                topic.getId().hashCode()%numberOfThreads,
                ()-> handleSubscription(subscription,topic)
        );
    }
}
