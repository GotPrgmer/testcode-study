package org.study.testcode;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.study.testcode.repositroy.TestEntityOnlyIdRepository;

import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
public class MergeUpdateTest {

    @Autowired
    TestEntityOnlyIdRepository repository;

    @Test
    @Transactional
    @Rollback(value = false)
    void testEntity와_testEntity2를_둬서_영속상태와_비영속상태를_테스트한다() {
        String id = UUID.randomUUID().toString();

        TestEntityOnlyId testEntity = new TestEntityOnlyId();
        testEntity.setId(id);
        testEntity.setName("테스트");
        testEntity.setAge(10);
        // 트랜잭션 시작
        repository.save(testEntity);
        // 트랜잭션 종료
        System.out.println("testEntity = " + testEntity);

        TestEntityOnlyId testEntity2 = new TestEntityOnlyId();
        testEntity2.setId(id);
        testEntity2.setName("테스트2");
        // 트랜잭션 시작
        repository.save(testEntity2);
        // 트랜잭션 종료
        System.out.println("testEntity2 = " + testEntity2);
    }
}
