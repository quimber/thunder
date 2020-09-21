package com.melitest.simianchecker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatsResponse {		
	
	@JsonProperty(value = "count_simian_dna")
	private int countSimianDna = 0;
	
	@JsonProperty(value = "count_human_dna")
	private int countHumanDna = 0;
	
	private float ratio = 0;

	/**
	 * @return Count number of simian DNAs 
	 */
	public int getCountSimianDna() {
		return countSimianDna;
	}

	/**
	 * 
	 * Sets the number of simian DNAs 
	 * 
	 * @param countSimianDna
	 */
	public void setCountSimianDna(int countSimianDna) {
		this.countSimianDna = countSimianDna;
	}

	/**
	 * @return Count number of human DNAs 
	 */
	public int getCountHumanDna() {
		return countHumanDna;
	}

	/**
	 * 
	 * Sets the count number of human DNAs 
	 * 
	 * @param countHumanDna
	 */
	public void setCountHumanDna(int countHumanDna) {
		this.countHumanDna = countHumanDna;
	}

	/**
	 * @return Ratio of between simian DNA and human DNA -> simian/human  
	 */
	public float getRatio() {
		return ratio;
	}

	/**
	 * 
	 * Sets the ratio of between simian DNA and human DNA
	 * 
	 * @param ratio
	 */
	public void setRatio(float ratio) {
		this.ratio = ratio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + countHumanDna;
		result = prime * result + countSimianDna;
		result = prime * result + Float.floatToIntBits(ratio);
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
		StatsResponse other = (StatsResponse) obj;
		if (countHumanDna != other.countHumanDna)
			return false;
		if (countSimianDna != other.countSimianDna)
			return false;
		
		return Float.floatToIntBits(ratio) == Float.floatToIntBits(other.ratio);			
	}	

}
