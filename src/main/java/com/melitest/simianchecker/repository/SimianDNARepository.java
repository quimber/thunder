package com.melitest.simianchecker.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.melitest.simianchecker.model.SimianDNA;

import reactor.core.publisher.Mono;

@Repository
public interface SimianDNARepository extends ReactiveMongoRepository<SimianDNA, ObjectId> {
	
	Mono<SimianDNA> findByDnaId (Long dnaId);
	
}
