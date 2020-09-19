package com.melitest.simianchecker.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.melitest.simianchecker.dto.CheckDNARequest;
import com.melitest.simianchecker.dto.CheckDNAResponse;
import com.melitest.simianchecker.dto.StatsResponse;
import com.melitest.simianchecker.service.SimianDNAService;

import reactor.core.publisher.Mono;

@RestController
public class SimianDNAController {
	
	@Autowired
	private SimianDNAService simianDNAService;
	
	@PostMapping(path = "/simian", consumes = "application/json", produces = "application/json")
    public Mono<ResponseEntity<?>> createDNA (@Valid @RequestBody CheckDNARequest dnaRequest)
    {
    	return simianDNAService.createSimianDNA(dnaRequest)    			
    			.map(this::mapCheckDNAResponse);
    }
	
	@GetMapping("/stats")
	public Mono<StatsResponse> getDNAStats ()
	{
		return simianDNAService.getDNAStats();
	}
	
	private ResponseEntity<?> mapCheckDNAResponse (CheckDNAResponse response) {
		if (response.isNewDNA())
		{
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
		return ResponseEntity.ok().body(response);
	}

}
