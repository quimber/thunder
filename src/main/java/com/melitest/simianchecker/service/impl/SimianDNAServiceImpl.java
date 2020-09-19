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
				.flatMap(this::checkDNA)
				.map(this::mapResponse);
	}

	@Override
	public Mono<StatsResponse> getDNAStats() {		
		GroupOperation countGroup = Aggregation.group("isSimian").count().as("count");
		Aggregation aggregation = Aggregation.newAggregation(countGroup);

		return reactiveMongoTemplate.aggregate(aggregation, SimianDNA.class, Map.class)
				.switchIfEmpty(Flux.just(new HashMap()))
				.buffer().last()
		.map(this::mapGroupCountToStatsResponse);
	}
	
	private StatsResponse mapGroupCountToStatsResponse (@SuppressWarnings("rawtypes") List<Map> resultList) {
		StatsResponse response = new StatsResponse();
		for (int i = 0; i < resultList.size() && i < 2; i++)
		{
			Map<?,?> countGroupMap = resultList.get(i);
			Object isSimian = countGroupMap.get("_id");		
			if (isSimian instanceof Boolean)
			{
				Object count = countGroupMap.get("count");
				if ((Boolean) isSimian)
				{
					response.setCountSimianDna((Integer) count);
				}
				else 
				{
					response.setCountHumanDna((Integer) count);
				}
			}							
		}
		if (response.getCountHumanDna() != 0)
		{
		    response.setRatio(((float) response.getCountSimianDna() / response.getCountHumanDna()));
		}
		return response;
	}
	
	private DNA mapCheckDNARequest (CheckDNARequest dnaRequest)
	{
		DNA dna = new DNA();
		dna.setDna(dnaRequest.getDna());
		return dna;
	}
	
	private Mono<SimianDNA> createSimianDNA (DNA dna)
	{
		return simianDNARepository.findByDnaId(dna.getId())
		.switchIfEmpty(Mono.just(dna)
				.map(this::mapSimianDNA)
				.flatMap(simianDNARepository::save).doOnNext(simianDna -> simianDna.setNewDNA(true)))
		.doOnNext(simianDna -> simianDna.setDnaObject(dna));				
	}
	
	private SimianDNA mapSimianDNA (DNA dna)
	{
		SimianDNA simianDNA = new SimianDNA();
        simianDNA.setDnaId(dna.getId());        
        return simianDNA;
	}
	
	private Mono<SimianDNA> checkDNA (SimianDNA simianDNA)
	{
		if (simianDNA.isSimian() == null)
		{
			List<String> dnaSquence = simianDNA.getDnaObject().getDna();
			simianDNA.setSimian(isSimian(dnaSquence));
			return simianDNARepository.save(simianDNA);
		}
		return Mono.just(simianDNA);
	}
	
	private CheckDNAResponse mapResponse (SimianDNA simianDNA)
	{
		CheckDNAResponse response = new CheckDNAResponse();
		response.setSimian(simianDNA.isSimian());
		response.setNewDNA(simianDNA.isNewDNA());
		return response;
	}

}
