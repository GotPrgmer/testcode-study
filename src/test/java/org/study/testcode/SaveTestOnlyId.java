package org.study.testcode;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.study.testcode.repositroy.TestEntityOnlyIdRepository;

import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
public class SaveTestOnlyId {
    @Autowired
    TestEntityOnlyIdRepository repository;

    @Autowired
    EntityManager em;

    @Test
    @Transactional
    @Rollback(value = false)
    void GeneratedValue를_사용하지_않을_때_더티체킹이_발생하는_지_확인하는_테스트() {

        String id = UUID.randomUUID().toString();

        TestEntityOnlyId entity = new TestEntityOnlyId();
        entity.setId(id);
        entity.setName("반가워!");

        // 1) Spring Data JPA가 판단하는 isNew() 확인
        var info = JpaEntityInformationSupport.getEntityInformation(TestEntityOnlyId.class, em);
        System.out.println("[isNew(entity)] = " + info.isNew(entity));

        // 2) save 전/후 영속 상태 확인
        System.out.println("[before save] contains(entity) = " + em.contains(entity)); // false 예상

        TestEntityOnlyId newEntity = repository.save(entity); // isNew=false면 내부적으로 merge
        System.out.println("[after save] entity == newEntity ? " + (entity == newEntity)); // merge면 보통 false
        System.out.println("[after save] contains(entity)    = " + em.contains(entity));    // merge면 false(원본은 준영속/비관리)
        System.out.println("[after save] contains(newEntity) = " + em.contains(newEntity)); // true(반환값이 영속 상태)

        // entity는 영속상태가 아니다.
        entity.setName("entity는 영속상태 아니라서 변경감지가 안된다.!");
        // newEntity는 영속상태
        newEntity.setName("newEntity는 변경감지가 된다!");

        // 3) SQL을 눈에 보이게 강제
        em.flush(); // 여기서 INSERT/UPDATE가 실제로 나갑니다.
    }
}
