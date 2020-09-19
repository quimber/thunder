package com.melitest.simianchecker.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.melitest.simianchecker.model.DNA;

@Repository
public interface DNARepository extends ReactiveMongoRepository<DNA, Long> {

}
