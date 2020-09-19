package com.melitest.simianchecker.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melitest.simianchecker.model.DNA;
import com.melitest.simianchecker.repository.DNARepository;
import com.melitest.simianchecker.service.DNAService;

import reactor.core.publisher.Mono;

@Service
public class DNAServiceImpl implements DNAService {

	@Autowired
	private DNARepository dnaRepository;

	@Override
	public Mono<DNA> createDNA(DNA dna) {
		return dnaRepository.save(dna);
	}
	
}
