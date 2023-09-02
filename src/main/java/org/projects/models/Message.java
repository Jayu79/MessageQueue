package org.projects.models;

import lombok.Data;

import java.util.Map;

@Data
public class Message {

    private Long Id;
    private Map<String,String> properties;


}
