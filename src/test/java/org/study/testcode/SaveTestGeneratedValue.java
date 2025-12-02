package org.study.testcode;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.study.testcode.repositroy.TestEntityGeneratedValuedRepository;
import org.study.testcode.repositroy.TestEntityOnlyIdRepository;

import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
public class SaveTestGeneratedValue {
    @Autowired
    TestEntityGeneratedValuedRepository repository;

    @Autowired
    EntityManager em;

    @Test
    @Transactional
    @Rollback(value = false)
    void flush시_더티체킹이_실제로_발생해서_update쿼리가_발생하는지_테스트(){
        TestEntityGeneratedValue entity = new TestEntityGeneratedValue();
        entity.setName("안녕!");

        repository.save(entity);

        em.flush(); // INSERT 쿼리 강제

        entity.setName("변경감지");
        System.out.println("------------flush발생 before");
        em.flush(); // UPDATE 쿼리 강제 (dirty checking 반영)

        em.clear(); // DB에서 다시 읽어 확인(선택)
        TestEntityGeneratedValue reloaded = repository.findById(entity.getId()).orElseThrow();
    }
}

