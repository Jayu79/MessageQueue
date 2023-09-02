package org.projects.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StripedExecutor {

    private List<ExecutorService> executorServices;

    public StripedExecutor(int numberOfThreads){
        executorServices = new ArrayList<>();

        for(int i=0;i<numberOfThreads;++i){
            executorServices.add(Executors.newSingleThreadExecutor());
        }
    }

    public Future<Void> submit(int id, Runnable task){

        return CompletableFuture.runAsync(task,executorServices.get(id));

    }

}
