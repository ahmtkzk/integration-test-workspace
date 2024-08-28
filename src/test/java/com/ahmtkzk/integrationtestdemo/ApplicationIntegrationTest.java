package com.ahmtkzk.integrationtestdemo;

import com.ahmtkzk.integrationtestdemo.model.TestEntity;
import com.ahmtkzk.integrationtestdemo.repository.TestRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

@SpringBootTest
@Testcontainers
class ApplicationIntegrationTest {

    @Autowired
    private TestRepository repository;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("user")
            .withPassword("pass");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Test
    void testSave() {
        // Given
        TestEntity entity = new TestEntity(null, "testTopic", "testDescription", 20);

        // When
        repository.save(entity);
        TestEntity result = repository.findById(entity.getId()).get();

        // Then
        Assertions.assertEquals(entity.getId(), result.getId());
        Assertions.assertEquals(entity.getNumber(), result.getNumber());
        Assertions.assertEquals(entity.getTopic(), result.getTopic());
    }

    @Test
    void testDelete() {
        // Given
        TestEntity entity = new TestEntity(null, "testTopic", "testDescription", 20);
        TestEntity entity2 = new TestEntity(null, "testTopic2", "testDescription2", 30);
        repository.save(entity);
        repository.save(entity2);

        // When
        repository.delete(entity);
        Optional<TestEntity> result = repository.findById(entity.getId());
        Optional<TestEntity> result2 = repository.findById(entity2.getId());

        // Then
        Assertions.assertFalse(result.isPresent());
        Assertions.assertTrue(result2.isPresent());
    }

    @Test
    void testFindById() {
        // Given
        TestEntity entity = new TestEntity(null, "testTopic", "testDescription", 20);
        TestEntity entity2 = new TestEntity(null, "testTopic2", "testDescription2", 30);
        repository.save(entity);
        repository.save(entity2);

        // When
        TestEntity result = repository.findById(entity.getId()).get();
        TestEntity result2 = repository.findById(entity2.getId()).get();

        // Then
        Assertions.assertEquals(entity.getId(), result.getId());
        Assertions.assertEquals(entity.getNumber(), result.getNumber());
        Assertions.assertEquals(entity.getTopic(), result.getTopic());
        Assertions.assertEquals(entity2.getId(), result2.getId());
        Assertions.assertEquals(entity2.getNumber(), result2.getNumber());
        Assertions.assertEquals(entity2.getTopic(), result2.getTopic());
    }

    @Test
    void testFindAll() {
        // Given
        TestEntity entity = new TestEntity(null, "testTopic", "testDescription", 20);
        TestEntity entity2 = new TestEntity(null, "testTopic2", "testDescription2", 30);
        repository.save(entity);
        repository.save(entity2);

        // When & Then
        Assertions.assertEquals(2, repository.count());
    }

}
