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
		
		File file = new File("LCS_output.csv");
		FileWriter fr = new FileWriter(file, true);
		BufferedWriter bwr = new BufferedWriter(fr);
		bwr.write("score, tokens, count, source code\n");
		
		ArrayList<String> allFilesTokens = new ArrayList<String>();
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
					if(!statement.startsWith("import"))
						
					completeFileTokens.addAll(tokenize(statement));			
					//System.out.println(completeFileTokens);
					
				
				}
			
			}
			
			
			//add a document to the solver where each java file is a document
			String tokensString = completeFileTokens.toString();
			tokensString = tokensString.replace("[", "");
			tokensString = tokensString.replace("]", "");
			tokensString = tokensString.replace(",", "");
			solver.add(tokensString);
			//System.out.println(completeFileTokens);
			allFilesTokens.add(tokensString);
		}
		
		
		List<String> longestCommonSubstrings=solver.getLongestCommonSubstrings(solver.getLongestCommonSubstring().toString());
		System.out.println("No. of Longest Common Subsequences: "+longestCommonSubstrings.size());
		
		
		for(String lcs: longestCommonSubstrings)
		{			
			
			System.out.println(lcs);
			int countOfOccurrences = getCountOfOccurrences(lcs, allFilesTokens);
			System.out.println("No of occurrences: " + countOfOccurrences);
			int tokenCount = lcs.toString().split(" ").length;
			System.out.println("TokenCount: " + tokenCount);
			double score = ((Math.log(tokenCount)/Math.log(2))*(Math.log(countOfOccurrences)/Math.log(2)));
			System.out.println("Score: " + score);
			bwr.write(score + ","+ tokenCount + ","+ countOfOccurrences + "," + lcs +"\n");
		}				
		
		bwr.close();
		fr.close();
		
	}

	private static int getCountOfOccurrences(String lcs, ArrayList<String> allFilesTokens) {
		int result = 0;

		//String searchString = lcs.trim().replaceAll(" ", "");
		for(String s : allFilesTokens)
		{
			int i = 0;
			while ((i = s.indexOf(lcs, i)) != -1)
			{
				i++;
				result++;
			}
		}
		return result;
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
					
					//System.out.println("   " + token.getTokenString() );
					tokens.add(token.getTokenString());
				}
			}
			
		} catch (AnalyzerException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return tokens;
	}
}
