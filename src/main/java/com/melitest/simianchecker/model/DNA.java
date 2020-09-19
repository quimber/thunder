package com.melitest.simianchecker.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "dna")
public class DNA {
	
	@Id
	private Long _id;
	
	private List<String> dna;	

	/**
	 * @return _id
	 */
	public Long getId()
	{
		return _id;
	}
	
	/**
	 * @return DNA sequence of a simian
	 */
	public List<String> getDna() {
		return dna;
	}

	/**
	 * @param dna sequence
	 */
	public void setDna(List<String> dna) {
		_id = (long) dna.hashCode();
		this.dna = dna;
	}	

}
