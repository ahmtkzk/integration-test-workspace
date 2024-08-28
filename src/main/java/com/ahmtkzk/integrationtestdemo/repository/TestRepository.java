package com.ahmtkzk.integrationtestdemo.repository;

import com.ahmtkzk.integrationtestdemo.model.TestEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends CrudRepository<TestEntity, Long> {

}
