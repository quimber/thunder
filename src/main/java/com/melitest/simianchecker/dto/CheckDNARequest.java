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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((dna == null) ? 0 : dna.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CheckDNARequest other = (CheckDNARequest) obj;
		if (dna == null) {
			if (other.dna != null)
				return false;
		} else if (!dna.equals(other.dna))
			return false;
		return true;
	} 

}
