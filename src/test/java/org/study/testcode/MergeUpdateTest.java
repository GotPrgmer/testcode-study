package org.study.testcode;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.study.testcode.repositroy.TestEntityGeneratedValuedRepository;
import org.study.testcode.repositroy.TestEntityOnlyIdRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class MergeUpdateTest {

    @Autowired
    TestEntityOnlyIdRepository repository;

    @Autowired
    TestEntityGeneratedValuedRepository generatedRepository;

    @Autowired
    EntityManager em;

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


    @Test
    @Rollback(value = false)
    void update시_부분적으로_업데이트가_아니라_전체필드_업데이트쿼리가_나간다를_테스트합니다() {
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

    @Test
    @Transactional
    void save_동일_트랜잭션에서_persist와_merge의_input_contains를_비교한다() {
        // 1) persist 경로 유도 (GeneratedValue -> 저장 전 id=null -> isNew=true)
        TestEntityGeneratedValue persistInput = new TestEntityGeneratedValue();
        persistInput.setName("persist 대상");

        var gvInfo = JpaEntityInformationSupport.getEntityInformation(TestEntityGeneratedValue.class, em);
        assertTrue(gvInfo.isNew(persistInput), "GeneratedValue 엔티티는 저장 전 isNew=true 이어야 합니다.");
        assertFalse(em.contains(persistInput));

        TestEntityGeneratedValue persistOutput = generatedRepository.save(persistInput);

        // persist: input 인스턴스 자체가 managed, 보통 input==output
        assertTrue(em.contains(persistInput), "persist 경로에서는 input이 managed여야 합니다.");
        assertSame(persistInput, persistOutput, "persist 경로에서는 보통 input==output 입니다.");
        assertTrue(em.contains(persistOutput), "persist 경로에서는 output이 managed여야 합니다.");

        // 2) merge 경로 유도 (ID 선할당 -> isNew=false)
        TestEntityOnlyId mergeInput = new TestEntityOnlyId();
        mergeInput.setId(UUID.randomUUID().toString());
        mergeInput.setName("merge 대상");
        mergeInput.setAge(10);

        var onlyIdInfo = JpaEntityInformationSupport.getEntityInformation(TestEntityOnlyId.class, em);
        assertFalse(onlyIdInfo.isNew(mergeInput), "ID 선할당 엔티티는 기본 판단에서 isNew=false(merge)이어야 합니다.");
        assertFalse(em.contains(mergeInput));

        TestEntityOnlyId mergeOutput = repository.save(mergeInput);

        // merge: input은 managed가 아니고, 반환값만 managed
        assertFalse(em.contains(mergeInput), "merge 경로에서는 input이 managed가 아니어야 합니다.");
        assertTrue(em.contains(mergeOutput), "merge 경로에서는 반환값이 managed여야 합니다.");
        assertNotSame(mergeInput, mergeOutput, "merge 경로에서는 보통 input!=output 입니다.");

        // 3) 비교 결과를 명확히 출력(선택)
        System.out.println("[persist] contains(input)=" + em.contains(persistInput)
                + ", contains(output)=" + em.contains(persistOutput)
                + ", input==output=" + (persistInput == persistOutput));

        System.out.println("[merge]   contains(input)=" + em.contains(mergeInput)
                + ", contains(output)=" + em.contains(mergeOutput)
                + ", input==output=" + (mergeInput == mergeOutput));

        // SQL까지 눈으로 확인하고 싶으면 flush (선택)
        em.flush();
    }
}
