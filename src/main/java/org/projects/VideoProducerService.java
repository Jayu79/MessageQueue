package org.projects;

import org.projects.models.Subscription;

public class VideoProducerService {

    private void transformVideo(){}

    void videoUploaded(){

        Subscription subscription = new Subscription();
        subscription.setCallback(
                (subscriptionFromQueue, messageFromQueue) -> {
                    transformVideo();
                }
        );

    }
}
