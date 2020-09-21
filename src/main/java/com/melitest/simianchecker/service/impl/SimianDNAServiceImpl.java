package com.melitest.simianchecker.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.stereotype.Service;

import com.melitest.simianchecker.dto.CheckDNARequest;
import com.melitest.simianchecker.dto.CheckDNAResponse;
import com.melitest.simianchecker.dto.StatsResponse;
import com.melitest.simianchecker.model.DNA;
import com.melitest.simianchecker.model.SimianDNA;
import com.melitest.simianchecker.repository.SimianDNARepository;
import com.melitest.simianchecker.service.DNAService;
import com.melitest.simianchecker.service.SimianDNAService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SimianDNAServiceImpl implements SimianDNAService {
	
	private static final String COUNT = "count";

	@Autowired
	private SimianDNARepository simianDNARepository;
	
	@Autowired
	private ReactiveMongoTemplate reactiveMongoTemplate;
	
	@Autowired
	private DNAService dnaService;

	@Override
	public Mono<CheckDNAResponse> createSimianDNA(CheckDNARequest dnaRequest) {
		return Mono.just(dnaRequest)
				.doOnNext(this::validateDNARequest)
				.map(this::mapCheckDNARequest)
				.flatMap(dnaService::createDNA)
				.flatMap(this::createSimianDNA)
				.map(this::mapResponse);
	}
	
	@Override
	@SuppressWarnings({ "unchecked" })
	public Mono<StatsResponse> getDNAStats() {		
		GroupOperation countGroup = Aggregation.group("isSimian").count().as(COUNT);
		Aggregation aggregation = Aggregation.newAggregation(countGroup);

		return reactiveMongoTemplate.aggregate(aggregation, SimianDNA.class, Map.class)
				.map(countGroupMap -> (Map<String, Object>) countGroupMap)
				.switchIfEmpty(Flux.just(new HashMap<String, Object>()))
				.buffer().last()
		.map(this::mapGroupCount);
	}
	
	private StatsResponse mapGroupCount (List<Map<String, Object>> resultList) {
		StatsResponse response = new StatsResponse();		
		
		resultList.stream()
		.filter(countGroupMap -> countGroupMap.get("_id") instanceof Boolean && (Boolean) countGroupMap.get("_id"))
		.map(countGroupMap -> (Integer) countGroupMap.get(COUNT))
		.forEach(response::setCountSimianDna);
		
		resultList.stream()
		.filter(countGroupMap -> countGroupMap.get("_id") instanceof Boolean && !(Boolean) countGroupMap.get("_id"))
		.map(countGroupMap -> (Integer) countGroupMap.get(COUNT))
		.forEach(response::setCountHumanDna);
				
		if (response.getCountHumanDna() != 0)
		{
		    response.setRatio(((float) response.getCountSimianDna() / response.getCountHumanDna()));
		}
		return response;
	}
	
	private DNA mapCheckDNARequest (CheckDNARequest dnaRequest)
	{
		DNA dna = new DNA();
		dna.setDnaSequence(dnaRequest.getDna());
		return dna;
	}
	
	private Mono<SimianDNA> createSimianDNA (DNA dna)
	{
		return simianDNARepository.findByDnaId(dna.getId())
		.switchIfEmpty(Mono.just(dna)
				.map(this::mapSimianDNA)
				.doOnNext(simianDna -> simianDna.setDnaObject(dna))
				.doOnNext(this::checkDNA)
				.flatMap(simianDNARepository::save)
				.doOnNext(simianDna -> simianDna.setNewDNA(true)))
		.doOnNext(simianDna -> simianDna.setDnaObject(dna));				
	}
	
	private SimianDNA mapSimianDNA (DNA dna)
	{
		SimianDNA simianDNA = new SimianDNA();
        simianDNA.setDnaId(dna.getId());        
        return simianDNA;
	}
	
	private void checkDNA (SimianDNA simianDNA)
	{
		List<String> dnaSquence = simianDNA.getDnaObject().getDnaSequence();
		simianDNA.setSimian(isSimian(dnaSquence));		
	}
	
	private CheckDNAResponse mapResponse (SimianDNA simianDNA)
	{
		CheckDNAResponse response = new CheckDNAResponse();
		response.setSimian(simianDNA.isSimian());
		response.setNewDNA(simianDNA.isNewDNA());
		return response;
	}

}
