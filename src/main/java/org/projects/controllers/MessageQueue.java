package org.projects.controllers;

import org.projects.models.Message;
import org.projects.models.Server;
import org.projects.models.Subscription;
import org.projects.models.Topic;
import org.projects.services.MessageQueueService;

import java.util.concurrent.Future;

public class MessageQueue {

    private MessageQueueService messageQueueService;

    public MessageQueue(MessageQueueService messageQueueService){
        this.messageQueueService = messageQueueService;
    }
    public Future<Void> publish(Topic topic, Message message){
        return messageQueueService.publish(topic,message);
    }

    public Future<Void> subscribe(Subscription subscription, Topic topic){
        return messageQueueService.subscribe(subscription,topic);
    }

}
