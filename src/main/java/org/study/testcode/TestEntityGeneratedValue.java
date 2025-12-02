package org.study.testcode;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class TestEntityGeneratedValue {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
