package org.study.testcode;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class TestEntityOnlyId {
    @Id
    private String id;
    private String name;
    private int age;
}
