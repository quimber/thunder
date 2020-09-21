package com.melitest.simianchecker.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "dna")
public class DNA {
	
	@Id
	private Long id;
	
	private List<String> dnaSequence;	

	/**
	 * @return _id
	 */
	public Long getId()
	{
		return id;
	}
	
	/**
	 * @return DNA sequence of a simian
	 */
	public List<String> getDnaSequence() {
		return dnaSequence;
	}

	/**
	 * @param dna sequence
	 */
	public void setDnaSequence(List<String> dna) {
		id = (long) dna.hashCode();
		this.dnaSequence = dna;
	}	

}
