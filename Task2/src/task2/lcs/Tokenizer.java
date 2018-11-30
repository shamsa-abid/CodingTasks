package task2.lcs;

import java.util.ArrayList;

import exceptions.AnalyzerException;
import lexer.Lexer;
import token.Token;

public class Tokenizer {

	public static void main(String args[])
	{
		String sourceCode = "for (int alpha=0;";
		ArrayList<String> tokens = tokenize(sourceCode);
	}

	private static ArrayList<String> tokenize(String sourceCode) {
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
				}
			}
			
		} catch (AnalyzerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
