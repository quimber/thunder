package com.melitest.simianchecker.service;

import java.util.List;

import com.melitest.simianchecker.dto.CheckDNARequest;
import com.melitest.simianchecker.dto.CheckDNAResponse;
import com.melitest.simianchecker.dto.StatsResponse;
import com.melitest.simianchecker.exception.IllegalDNARequest;

import reactor.core.publisher.Mono;

public interface SimianDNAService {
	
	Mono<CheckDNAResponse> createSimianDNA (CheckDNARequest dnaRequest);
	
	Mono<StatsResponse> getDNAStats ();
	
	public default boolean isSimian(List<String> dnaSquence) {		
		char[][] dnaMatrix = toCharArray(dnaSquence);
		boolean isSimianDNA = isSimianDNA(dnaMatrix);
		return isSimianDNA;
	}
	
	public default void validateDNARequest (CheckDNARequest request) throws IllegalArgumentException
	{
		List<String> dna = request.getDna();
		int dnaSize = dna.size();
		if (dnaSize > 0)
		{
			for (String dnaSequence : dna)
			{
				if (dnaSequence.length() != dnaSize)
				{
					throw new IllegalDNARequest("Invalid DNA sequence "+dnaSequence+", it must have the same legth of number of sequences = "+dnaSize);
				}
				if (!dnaSequence.matches("[ATCG]+"))
				{
					throw new IllegalDNARequest("Invalid DNA sequence "+dnaSequence+", it must contain only A, T, C or G.");
				}
			}
		}
		else
		{
			throw new IllegalDNARequest("DNA cannot be empty.");
		}
	}
	
	private boolean isSimianDNA (char[][] dnaMatrix)
	{
		int resultCount = 0;
		for (int i = 0; i < dnaMatrix.length; i++)
		{
			for (int j = 0; j < dnaMatrix[i].length; j++)
			{
				if (i + 3 < dnaMatrix[i].length)
					resultCount += checkLineMatch(dnaMatrix, i, j)? 1 : 0;
				if (j + 3 < dnaMatrix[i].length)
					resultCount += checkCollumnMatch(dnaMatrix, i, j)? 1 : 0;
				if (i + 3 < dnaMatrix[i].length && j + 3 < dnaMatrix[i].length)
					resultCount += checkDiagonalMatch(dnaMatrix, i, j)? 1 : 0;
				if (i - 3 >= 0 && j + 3 < dnaMatrix[i].length)
					resultCount += checkInverseDiagonalMatch(dnaMatrix, i, j)? 1 : 0;
				
				if (resultCount >= 2)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	private char[][] toCharArray (List<String> dnaSquence)
	{
		char[][] dnaMatrix = new char[dnaSquence.size()][dnaSquence.size()];
		int i = 0;
		for (String dnaLine : dnaSquence)
		{
			dnaMatrix[i++] = dnaLine.toCharArray();
		}
		return dnaMatrix;
	}
	
	private boolean checkCollumnMatch(char[][] dnaMatrix, int i, int j)
	{
		return dnaMatrix[i][j] == dnaMatrix[i][j+1] && dnaMatrix[i][j+2] == dnaMatrix[i][j+3] && dnaMatrix[i][j+3] == dnaMatrix[i][j];
	}
	
	private boolean checkLineMatch(char[][] dnaMatrix, int i, int j)
	{
		return dnaMatrix[i][j] == dnaMatrix[i+1][j] && dnaMatrix[i+2][j] == dnaMatrix[i+3][j] && dnaMatrix[i+3][j] == dnaMatrix[i][j];
	}
	
	private boolean checkDiagonalMatch(char[][] dnaMatrix, int i, int j)
	{
		return dnaMatrix[i][j] == dnaMatrix[i+1][j+1] && dnaMatrix[i+2][j+2] == dnaMatrix[i+3][j+3] && dnaMatrix[i+3][j+3] == dnaMatrix[i][j];
	}
	
	private boolean checkInverseDiagonalMatch(char[][] dnaMatrix, int i, int j)
	{
		return dnaMatrix[i][j] == dnaMatrix[i-1][j+1] && dnaMatrix[i-2][j+2] == dnaMatrix[i-3][j+3] && dnaMatrix[i-3][j+3] == dnaMatrix[i][j];
	}

}
