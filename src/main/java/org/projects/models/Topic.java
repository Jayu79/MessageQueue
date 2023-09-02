package org.projects.models;

import lombok.Data;

import java.util.List;

@Data
public class Topic {

    private Long id;
    private String name;
    private List<Message> messages;
}
