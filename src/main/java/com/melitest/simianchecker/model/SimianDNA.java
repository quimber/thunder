package com.melitest.simianchecker.model;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@CompoundIndexes({
    @CompoundIndex(name = "dnaId", def = "{'dnaId' : 1}", unique = true),
    @CompoundIndex(name = "isSimian", def = "{'isSimian' : 1}")
})
public class SimianDNA {

	@Id
    private ObjectId id;
	
	@CreatedDate
    private LocalDateTime createdDate;
	
	@LastModifiedDate
    private LocalDateTime updatedDate;
	
	private Long dnaId;
	
	@Transient
	private DNA dna;
	
	@Transient
	private boolean newDNA = false;
	
	private Boolean isSimian;

	/**
	 * @return Id
	 */
	public ObjectId getId() {
		return id;
	}

	/**
	 * @return the creation date
	 */
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	/**
	 * @return the date of the last time when the object was update
	 */
	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * @return DNA id of a simian
	 */
	public Long getDnaId() {
		return dnaId;
	}

	/**
	 * Sets DNA id
	 * 
	 * @param dnaId
	 */
	public void setDnaId(Long dnaId) {
		this.dnaId = dnaId;
	}

	/**
	 * @return dna
	 */
	public DNA getDnaObject() {
		return dna;
	}

	/**
	 * 
	 * set dna
	 * 
	 * @param dna
	 */
	public void setDnaObject(DNA dna) {
		this.dna = dna;
	}

	/**
	 * @return true when DNA is from simian
	 */
	public Boolean isSimian() {
		return isSimian;
	}

	/**
	 * Sets simian flag
	 * 
	 * @param isSimian
	 */
	public void setSimian(Boolean isSimian) {
		this.isSimian = isSimian;
	}

	/**
	 * @return the newDNA
	 */
	public boolean isNewDNA() {
		return newDNA;
	}

	/**
	 * @param newDNA the newDNA to set
	 */
	public void setNewDNA(boolean newDNA) {
		this.newDNA = newDNA;
	}	
	
}
