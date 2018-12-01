package task2.lcs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharSequenceNodeFactory;
import com.googlecode.concurrenttrees.solver.LCSubstringSolver;

import ca.pfv.spmf.algorithms.sequentialpatterns.prefixspan.AlgoBIDEPlus;
import exceptions.AnalyzerException;
import lexer.Lexer;
import token.Token;

public class Tokenizer {

	public static void main(String args[]) throws FileNotFoundException, IOException
	{
		LCSubstringSolver solver = new LCSubstringSolver(new DefaultCharSequenceNodeFactory());
		//read a file filesList.txt containing a list of source code files
		String inputFile = "F:/Task1/Task2/src/task2/lcs/filesList.txt";
		ArrayList<String> fileNames = getSourceFileNames(inputFile);
		
		File file = new File("LCS_output.txt");
		FileWriter fr = new FileWriter(file, true);
		BufferedWriter bwr = new BufferedWriter(fr);
		
		//iterate over files, tokenize and add tokens of the file to the solver as a string document
		for(String fileName: fileNames)
		{
			BufferedReader br = new BufferedReader(new FileReader(fileName));		 
			String statement;
			
			ArrayList<String> completeFileTokens = new ArrayList<String>();
			
			while ((statement = br.readLine()) != null)
			{
				if(!statement.contentEquals(""))
				{
					completeFileTokens.addAll(tokenize(statement));		
				
				
				}
			
			}
			bwr.close();
			fr.close();
			//add a document to the solver where each java file is a document
			solver.add(completeFileTokens.toString());
			
		}
		
		List<String> longestCommonSubstrings=solver.getLongestCommonSubstrings(solver.getLongestCommonSubstring().toString());
		System.out.println("Number of Longest Common Token Subsequences: "+longestCommonSubstrings.size());
		for(String lcs: longestCommonSubstrings)
		{			
			int tokenCount=lcs.toString().split(" ").length;	
			System.out.println(lcs);
		}				
		
	
		
	}

	private static ArrayList<String> getSourceFileNames(String inputFile) throws FileNotFoundException, IOException {
		ArrayList<String> fileNames = new ArrayList<String>();
		ArrayList<String> relatedFeaturesList = new ArrayList<String> ();
			 
		BufferedReader br = new BufferedReader(new FileReader(inputFile));		 
		String st;
		while ((st = br.readLine()) != null)
		{
			fileNames.add(st);
		}
		return fileNames;
	}

	private static ArrayList<String> tokenize(String sourceCode) {
		
		ArrayList<String> tokens = new ArrayList<String>();
		Lexer lexer = new Lexer();		
		
		try {
			lexer.tokenize(sourceCode);
			for (Token token : lexer.getTokens()) {
				if (token.getTokenType().isAuxiliary())
				{
					//do nothing
				}
					//System.out.println("   " + token.getTokenString());
				else {
					
					System.out.println("   " + token.getTokenString() );
					tokens.add(token.getTokenString());
				}
			}
			
		} catch (AnalyzerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tokens;
	}
}
