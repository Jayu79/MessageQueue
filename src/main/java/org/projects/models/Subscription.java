package org.projects.models;

import lombok.Data;
import org.projects.utils.CallbackMethod;

import java.util.concurrent.atomic.AtomicLong;

@Data
public class Subscription {

    private Long id;
    private Topic topic;
    private Server server;
    private AtomicLong offset;
    private CallbackMethod callback;

}
