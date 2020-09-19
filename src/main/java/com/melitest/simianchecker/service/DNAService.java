package com.melitest.simianchecker.service;

import com.melitest.simianchecker.model.DNA;

import reactor.core.publisher.Mono;

public interface DNAService {
	
	public Mono<DNA> createDNA (DNA dna);

}
