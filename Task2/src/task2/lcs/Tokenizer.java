package task2.lcs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import exceptions.AnalyzerException;
import lexer.Lexer;
import token.Token;

public class Tokenizer {

	public static void main(String args[]) throws FileNotFoundException, IOException
	{
		//read a file filesList.txt containing a list of source code files
		String inputFile = "F:/Task1/Task2/src/task2/lcs/filesList.txt";
		ArrayList<String> fileNames = getSourceFileNames(inputFile);
		
		//read all java files and for every line tokenize it and assign a unique ID to each token and store in an arraylist
		ArrayList<String> tokensMap = new ArrayList<String>();
		for(String fileName: fileNames)
		{
			BufferedReader br = new BufferedReader(new FileReader(fileName));		 
			String statement;
			File file = new File("hyphenated_output.txt");
			FileWriter fr = new FileWriter(file, true);
			BufferedWriter bwr = new BufferedWriter(fr);
			
			while ((statement = br.readLine()) != null)
			{
				if(!statement.contentEquals(""))
				{
				ArrayList<String> tokens = tokenize(statement);
				//for each token parsed, before adding token in list check if exists otherwise add it to the list
				String hyphenated = "";
				for(String token: tokens)
				{
					
					if(tokensMap.contains(token))
					{
						//for each line parsed keep building a token ID hyphen separated sequence and write it to a file
						int tokenID = tokensMap.indexOf(token);
						hyphenated = hyphenated.concat(tokenID + " -");
					}
					else
					{
						tokensMap.add(token);
						int tokenID = tokensMap.size();
						hyphenated = hyphenated.concat(tokenID + " -");
					}
				}
				
				if(!hyphenated.contentEquals(""))
				{
				hyphenated = hyphenated.substring(0, hyphenated.lastIndexOf(" -"));
				
				bwr.write(hyphenated + "\n");
				}
				
				
			}
			
			}
			bwr.close();
			fr.close();
		}
		
		
		
		//when the file is ready put it through BIDE to obtain the frequent sequential patterns
		//parse the output of BIDE to recover the actual sequence of tokens, its count and depth and write to csv
		String sourceCode = "for (int alpha=0;";
		ArrayList<String> tokens = tokenize(sourceCode);
		ArrayList<ArrayList<String>> listOfTokenLists = new ArrayList<ArrayList<String>>();
		
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
