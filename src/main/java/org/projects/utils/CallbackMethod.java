package org.projects.utils;

import org.projects.models.Message;
import org.projects.models.Subscription;

public interface CallbackMethod {

    void call(Subscription subscription, Message message);

}
