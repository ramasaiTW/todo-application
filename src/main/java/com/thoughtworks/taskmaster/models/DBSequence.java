package com.thoughtworks.taskmaster.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "database_sequences")
@Getter
@Setter
@NoArgsConstructor
public class DBSequence {
    @Id
    private String id;
    private long seq;
}
