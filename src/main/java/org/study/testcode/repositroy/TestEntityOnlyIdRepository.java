package org.study.testcode.repositroy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.study.testcode.TestEntityOnlyId;

public interface TestEntityOnlyIdRepository extends JpaRepository<TestEntityOnlyId, String> {

}
