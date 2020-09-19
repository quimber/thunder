package com.melitest.simianchecker.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckDNARequest {
	
	@JsonProperty(value = "dna", required = true)
	private List<String> dna;

	/**
	 * @return DNA sequence of a simian
	 */
	public List<String> getDna() {
		return dna;
	}

	/**
	 * 
	 * Sets the array of DNAs
	 * 
	 * @param dna
	 */
	public void setDna(List<String> dna) {
		this.dna = dna;
	} 

}
