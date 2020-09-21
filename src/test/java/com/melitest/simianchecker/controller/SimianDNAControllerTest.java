package com.melitest.simianchecker.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.melitest.simianchecker.dto.CheckDNARequest;
import com.melitest.simianchecker.dto.CheckDNAResponse;
import com.melitest.simianchecker.dto.StatsResponse;
import com.melitest.simianchecker.exception.IllegalDNARequest;
import com.melitest.simianchecker.service.SimianDNAService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations="classpath:application-test.properties")
@TestMethodOrder(OrderAnnotation.class)
class SimianDNAControllerTest {
	
	@Autowired
	private WebTestClient webTestClient;
	
	@Autowired
	private ReactiveMongoTemplate mongoTemplate;
	
	@Autowired
	private SimianDNAService simianDNAService;
	
	private StatsResponse dnaStats;
	
	private StatsResponse dnaEmptyStats;
	
	private CheckDNARequest simianDNA;
	
	private CheckDNARequest invalidCharDNA;
	
	private CheckDNARequest invalidSizeDNA;
	
	private CheckDNARequest invalidEmptyDNA;
	
	private CheckDNARequest invalidNullDNA;
	
	private CheckDNAResponse simianDNAResponse;
	
	private CheckDNAResponse humanDNAResponse;
	
	private List<CheckDNARequest> requestList;
	
	private List<CheckDNARequest> smallRequestList;
	
	@BeforeAll	
	public void setup ()
	{
		mongoTemplate.getMongoDatabase()
		.flatMapIterable(db -> List.of(db.getCollection("dna"), db.getCollection("simianDNA")))
		.flatMap(collection -> collection.drop())
		.buffer()
		.blockLast();
		
		dnaStats = new StatsResponse();
		dnaStats.setCountHumanDna(0);
		dnaStats.setCountSimianDna(1);
		
		dnaEmptyStats = new StatsResponse();
		dnaEmptyStats.setCountHumanDna(0);
		dnaEmptyStats.setCountSimianDna(0);
		
		simianDNA = new CheckDNARequest();
		simianDNA.setDna(List.of("CTGAGA", "CTATGC", "TATTGT", "AGAGGG", "CCCCTA", "TCACTG"));
		
		invalidCharDNA = new CheckDNARequest();
		invalidCharDNA.setDna(List.of("CTGAGA", "CTATGC", "TATTGT", "AGXGGG", "CCCCTA", "TCACTG"));
		
		invalidSizeDNA = new CheckDNARequest();
		invalidSizeDNA.setDna(List.of("CTGAGA", "CTATGC", "TATTGT", "AGXGGG", "CCCCTA", "TCACTG", "TCACTG"));
		
		invalidEmptyDNA = new CheckDNARequest();
		invalidEmptyDNA.setDna(List.of());
		
		invalidNullDNA = new CheckDNARequest();
		
		simianDNAResponse = new CheckDNAResponse();
		simianDNAResponse.setSimian(true);
		
		humanDNAResponse = new CheckDNAResponse();
		humanDNAResponse.setSimian(false);
		
		requestList = createRandomRequests (50);
		smallRequestList = createRandomRequests (10);
	}
	
	private List<CheckDNARequest> createRandomRequests (int bound)
	{
		int dnaSize = new Random().nextInt(bound) + 1;
		int requestCount = 100;
		List<CheckDNARequest> requests = new ArrayList<>(requestCount);
		for (int i = 0; i < requestCount; i++)
		{
			CheckDNARequest request = new CheckDNARequest();
			List<String> dna = new ArrayList<>(dnaSize);			
			for (int j = 0; j < dnaSize; j++)
			{
				String dnaSequence = "";
				for (int k = 0; k < dnaSize; k++)
				{
					int dnaType = new Random().nextInt(4);
					String type = "";
					switch (dnaType)
					{
					case 0:
						type = "A";
						break;
					case 1:
						type = "C";
						break;
					case 2:
						type = "G";
						break;
					case 3:
						type = "T";
						break;
					}		
					dnaSequence += type;
				}
				dna.add(dnaSequence);
			}
			request.setDna(dna);
			requests.add(request);
		}
		return requests;
	}
	
	private CheckDNAResponse checkCheckDNAResponse (List<String> dnaSequence)
	{
		CheckDNAResponse response = new CheckDNAResponse();
		response.setSimian(simianDNAService.isSimian(dnaSequence));
		return response;
	}
	
	@Test
	@Order(1)
	void getDNAStats_OK_NoResponse ()
	{
		webTestClient.get().uri("/stats")
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isOk()
		.expectBody(StatsResponse.class).isEqualTo(dnaEmptyStats);
	}
	
	@Test
	@Order(2)
	void mapCheckDNAResponse_Created_ValidDNARequest ()
	{
		webTestClient.post().uri("/simian").accept(MediaType.APPLICATION_JSON)
		.bodyValue(simianDNA)
		.exchange()
		.expectStatus().isCreated()
		.expectBody(CheckDNAResponse.class).isEqualTo(simianDNAResponse);
	}
	
	@Test
	@Order(3)
	void getDNAStats_OK_ValidDNAStatsRequest ()
	{
		webTestClient.get().uri("/stats")
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isOk()
		.expectBody(StatsResponse.class).isEqualTo(dnaStats);
	}
	
	@Test
	@Order(4)
	void mapCheckDNAResponse_OK_ValidDNARequest ()
	{
		webTestClient.post().uri("/simian").accept(MediaType.APPLICATION_JSON)
		.bodyValue(simianDNA)
		.exchange()
		.expectStatus().isOk()
		.expectBody(CheckDNAResponse.class).isEqualTo(simianDNAResponse);
	}
	
	@Test
	@Order(5)
	void mapCheckDNAResponse_Created_Many ()
	{
		requestList.forEach(request -> {
			webTestClient.post().uri("/simian").accept(MediaType.APPLICATION_JSON)
			.bodyValue(request)
			.exchange()
			.expectBody(CheckDNAResponse.class).isEqualTo(checkCheckDNAResponse(request.getDna()));
		});
	}
	
	@Test
	@Order(5)
	void mapCheckDNAResponse_Created_SmallList ()
	{
		smallRequestList.forEach(request -> {
			webTestClient.post().uri("/simian").accept(MediaType.APPLICATION_JSON)
			.bodyValue(request)
			.exchange()
			.expectBody(CheckDNAResponse.class).isEqualTo(checkCheckDNAResponse(request.getDna()));
		});
	}
	
	@Test
	@Order(6)
	void mapCheckDNAResponse_Unprocessable_InvalidCharDNA ()
	{					
		webTestClient.post().uri("/simian").accept(MediaType.APPLICATION_JSON)
		.bodyValue(invalidCharDNA)
		.exchange()
		.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
		.expectBody(IllegalDNARequest.class).value(Matchers.hasProperty("message", Matchers.equalTo("Invalid DNA sequence AGXGGG, it must contain only A, T, C or G.")));	
	}
	
	@Test
	@Order(6)
	void mapCheckDNAResponse_Unprocessable_InvalidSizeDNA ()
	{					
		webTestClient.post().uri("/simian").accept(MediaType.APPLICATION_JSON)
		.bodyValue(invalidSizeDNA)
		.exchange()
		.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
		.expectBody(IllegalDNARequest.class).value(Matchers.hasProperty("message", Matchers.equalTo("Invalid DNA sequence CTGAGA, it must have the same legth of number of sequences = "+7)));	
	}
	
	@Test
	@Order(6)
	void mapCheckDNAResponse_Unprocessable_InvalidEmptyDNA ()
	{					
		webTestClient.post().uri("/simian").accept(MediaType.APPLICATION_JSON)
		.bodyValue(invalidEmptyDNA)
		.exchange()
		.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
		.expectBody(IllegalDNARequest.class).value(Matchers.hasProperty("message", Matchers.equalTo("DNA cannot be empty.")));	
	}
	
	@Test
	@Order(6)
	void mapCheckDNAResponse_Unprocessable_InvalidNullDNA ()
	{					
		webTestClient.post().uri("/simian").accept(MediaType.APPLICATION_JSON)
		.bodyValue(invalidNullDNA)
		.exchange()
		.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
		.expectBody(IllegalDNARequest.class).value(Matchers.hasProperty("message", Matchers.equalTo("DNA cannot be empty.")));	
	}
	
	@Test
	@Order(7)
	void getDNAStats_OK_Many ()
	{
		webTestClient.get().uri("/stats")
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isOk()
		.expectBody(StatsResponse.class);
	}

}
