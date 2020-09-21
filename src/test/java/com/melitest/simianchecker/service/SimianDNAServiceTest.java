package com.melitest.simianchecker.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.melitest.simianchecker.dto.CheckDNARequest;
import com.melitest.simianchecker.exception.IllegalDNARequest;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations="classpath:application-test.properties")
class SimianDNAServiceTest {
	
	@Autowired
	private SimianDNAService simianDNAService;
	
	private CheckDNARequest simianDNA;
	
	private CheckDNARequest humanDNA;
	
	private CheckDNARequest invalidSizeDNA;
	
	private CheckDNARequest invalidCharDNA;
	
	private CheckDNARequest invalidEmptyDNA;
	
	private CheckDNARequest invalidNullDNA;
	
	@BeforeAll
	private void setup ()
	{
		simianDNA = new CheckDNARequest();
		simianDNA.setDna(List.of("CTGAGA",
				                 "CTATGC",
				                 "TATTGT",
				                 "AGAGGG",
				                 "CCCCTA",
				                 "TCACTG"));
		
		humanDNA = new CheckDNARequest();
		humanDNA.setDna(List.of("CTGAGA",
				                "CTATGC",
				                "TATTAT",
				                "AGAGGG",
				                "CCTCTA",
				                "TCACTG"));
		
		invalidSizeDNA = new CheckDNARequest();
		invalidSizeDNA.setDna(List.of("CTGAGA",
                "CTATGC",
                "TATTAT",
                "AGAGGG",
                "CCTCTA",
                "TCACTG",
                "TCACTG"));
		
		invalidCharDNA = new CheckDNARequest();
		invalidSizeDNA.setDna(List.of("CTGAGA",
                "CTATGC",
                "TATTAT",
                "AGAGGG",
                "CCTCTA",
                "TCXCTG",
                "TCACTG"));
		
		invalidEmptyDNA = new CheckDNARequest();
		invalidEmptyDNA.setDna(List.of());
		
		invalidNullDNA = new CheckDNARequest();
	}
	
	@Test
	void isSimian_valid_checkSimianDNA ()
	{
		Assertions.assertTrue(simianDNAService.isSimian(simianDNA.getDna()));
	}
	
	@Test
	void isSimian_valid_checkHumanDNA ()
	{
		Assertions.assertFalse(simianDNAService.isSimian(humanDNA.getDna()));
	}
	
	@Test
	void validateDNARequest_valid_simianDna ()
	{
		Assertions.assertAll(() -> simianDNAService.validateDNARequest(simianDNA));
	}
	
	@Test
	void validateDNARequest_valid_humanDna ()
	{
		Assertions.assertAll(() -> simianDNAService.validateDNARequest(humanDNA));
	}
	
	@Test
	void validateDNARequest_notValid_dnaWrongSize ()
	{
		Assertions.assertThrows(IllegalDNARequest.class,
				() -> simianDNAService.validateDNARequest(invalidSizeDNA),
				"Invalid DNA sequence CTGAGA, it must have the same legth of number of sequences = "+7);
	}
	
	@Test
	void validateDNARequest_notValid_dnaCharSize ()
	{
		Assertions.assertThrows(IllegalDNARequest.class,
				() -> simianDNAService.validateDNARequest(invalidCharDNA),
				"Invalid DNA sequence TCXCTG, it must contain only A, T, C or G.");
	}
	
	@Test
	void validateDNARequest_notValid_emptyDna()
	{
		Assertions.assertThrows(IllegalDNARequest.class,
				() -> simianDNAService.validateDNARequest(invalidEmptyDNA),
				"DNA cannot be empty.");
	}
	
	@Test
	void validateDNARequest_notValid_nullDna()
	{
		Assertions.assertThrows(IllegalDNARequest.class,
				() -> simianDNAService.validateDNARequest(invalidNullDNA),
				"DNA cannot be empty.");
	}
	
}
