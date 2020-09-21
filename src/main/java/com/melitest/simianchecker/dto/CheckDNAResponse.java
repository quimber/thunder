package com.melitest.simianchecker.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckDNAResponse {	
	
	@JsonProperty(value = "is_simian")
	private boolean isSimian;
	
	private boolean newDNA = false;

	/**
	 * @return true when inputed DNA is from simian
	 */
	@JsonIgnore
	public boolean isSimian() {
		return isSimian;
	}

	/**
	 * 
	 * sets the isSimian flag
	 * 
	 * @param isSimian
	 */
	public void setSimian(boolean isSimian) {
		this.isSimian = isSimian;
	}

	/**
	 * @return the newDNA
	 */
	@JsonIgnore
	public boolean isNewDNA() {
		return newDNA;
	}

	/**
	 * @param newDNA the newDNA to set
	 */
	public void setNewDNA(boolean newDNA) {
		this.newDNA = newDNA;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isSimian ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		CheckDNAResponse other = (CheckDNAResponse) obj;		
		return isSimian == other.isSimian;
	}

}
