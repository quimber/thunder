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

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import reactor.core.publisher.Mono;

@RestController
public class SimianDNAController {
	
	@Autowired
	private SimianDNAService simianDNAService;
	
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "Return the requested DNA, when the DNA sequence has already been created."),
		    @ApiResponse(code = 201, message = "Create the requested DNA"),
		    @ApiResponse(code = 422, message = "The input is not correct"),
		    @ApiResponse(code = 500, message = "An error occured"),
		})
	@PostMapping(path = "/simian", consumes = "application/json", produces = "application/json")
    public Mono<ResponseEntity<CheckDNAResponse>> createDNA (@Valid @RequestBody CheckDNARequest dnaRequest)
    {
    	return simianDNAService.createSimianDNA(dnaRequest)    			
    			.map(this::mapCheckDNAResponse);
    }
	
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "Return the stats of the DNAs, number of human/simian DNA and its ratio"),
		    @ApiResponse(code = 500, message = "An error occured"),
		})
	@GetMapping(path = "/stats", produces = "application/json")
	public Mono<StatsResponse> getDNAStats ()
	{
		return simianDNAService.getDNAStats();
	}
	
	private ResponseEntity<CheckDNAResponse> mapCheckDNAResponse (CheckDNAResponse response) {
		if (response.isNewDNA())
		{
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
		return ResponseEntity.ok().body(response);
	}

}
