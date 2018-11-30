package task2.lcs;

public class LCSFinder {
	public static void main(String args[])
	{
		System.out.println(longestCommonSubsequence("for (int i=0;","for (int j=0;"));
	}
	
	public static String longestCommonSubsequence(String a, String b) {
        int alength = a.length() - 1;
        int blength = b.length() - 1;
 
        if (alength < 0 || blength < 0)
            return ""; 
 
        if (a.substring(alength).equals(b.substring(blength))) {
            return longestCommonSubsequence(a.substring(0, alength), b.substring(0, blength))
                    + a.substring(alength);
        } else { 
            String first = longestCommonSubsequence(a, b.substring(0, blength));
            String second = longestCommonSubsequence(a.substring(0, alength), b);
            if (first.length() > second.length()) {
                return first;
            } else { 
                return second;
            } 
        } 
    } 
}