package _1_Task2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharSequenceNodeFactory;
import com.googlecode.concurrenttrees.solver.LCSubstringSolver;
import exceptions.AnalyzerException;
import lexer.Lexer;
import token.Token;

public class LongestCommmonTokenSequenceDetector {

	public static void main(String args[]) throws FileNotFoundException, IOException
	{

		String outfileName = "LCS_output.csv";
		ArrayList<String> fileNames = new ArrayList<String>();
		File dir;


		if(args.length > 0)
		{
			dir = new File(args[0]);
			outfileName = args[1];
		}
		else
		{
			dir = new File("F:/Task1/Task2/10TestFiles");

		}
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) { 
			for (File f : directoryListing) {
				fileNames.add(f.getAbsolutePath());
			} 
		}

		LCSubstringSolver solver = new LCSubstringSolver(new DefaultCharSequenceNodeFactory());
		//read a file filesList.txt containing a list of source code files
		//URL url = LongestCommmonTokenSequenceDetector.class.getResource(infileName);
		//File inputFile = new File(url.getPath());

		//String inputFile = "F:/Task1/Task2/src/task2/lcs/filesList.txt";
		//ArrayList<String> fileNames = getSourceFileNames(inputFile.getAbsolutePath());

		File file = new File(outfileName);
		FileWriter fr = new FileWriter(file);
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

			String tokensString = String.join(" ", completeFileTokens);			
			solver.add(tokensString);
			//System.out.println(completeFileTokens);
			allFilesTokens.add(tokensString);
		}


		List<String> longestCommonSubstrings = solver.getLongestCommonSubstrings(solver.getLongestCommonSubstring().toString());
		
		System.out.println("No. of Longest Common Subsequences: "+longestCommonSubstrings.size());
		System.out.println();

		for(String lcs: longestCommonSubstrings)
		{			

			String sourceCode = replaceTokenNameWithSymbols(lcs);
			System.out.println("Repeating sequence: " + sourceCode);
			int countOfOccurrences = getCountOfOccurrences(lcs, allFilesTokens);
			System.out.println("No of occurrences: " + countOfOccurrences);
			int tokenCount = lcs.toString().split(" ").length;
			System.out.println("TokenCount: " + tokenCount);
			double score = ((Math.log(tokenCount)/Math.log(2))*(Math.log(countOfOccurrences)/Math.log(2)));
			double roundOff = Math.round(score * 100.0) / 100.0;
			System.out.println("Score: " + roundOff);
			bwr.write(roundOff + ","+ tokenCount + ","+ countOfOccurrences + "," + lcs +"\n");
			System.out.println();
		}				

		bwr.close();
		fr.close();

	}

	private static String replaceTokenNameWithSymbols(String longestCommonSubstring) {
		
			if (longestCommonSubstring.contains("OpeningSquareBrace"))
				longestCommonSubstring = longestCommonSubstring.replace( "OpeningSquareBrace", "[");
			if (longestCommonSubstring.contains("ClosingSquareBrace"))
				longestCommonSubstring = longestCommonSubstring.replace("ClosingSquareBrace", "]");
			
	
		return longestCommonSubstring;
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
					if(//token.getTokenString().contentEquals("{")||
						//	token.getTokenString().contentEquals("}")||
							token.getTokenString().contentEquals("[")||
							token.getTokenString().contentEquals("]")
							//token.getTokenString().contentEquals("+")||
							//token.getTokenString().contentEquals("*")||
							//token.getTokenString().contentEquals("<")||
							//token.getTokenString().contentEquals(">")||
							//token.getTokenString().contentEquals("!=")||
							//token.getTokenString().contentEquals("==")||
							//token.getTokenString().contentEquals("=")
							)
					{
						tokens.add(token.getTokenType().toString());
					}
					else
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
