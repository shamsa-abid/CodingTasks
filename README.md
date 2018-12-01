## CodingTasks

This repository contains two coding tasks.

### Task 1: Refactoring detection across commits

### Task 2: Longest Common Subsequence detection across source code files
#### How to execute and test the code

1. Download or clone the git repo
2. Navigate to the folder Task2 in command prompt
3. Execute the command 
```
java -jar LongestCommonTokenSequenceDetector.jar <absolute path of directory conatining code files> <name of output.csv file>
```

For example:
```
java -jar LongestCommonTokenSequenceDetector.jar F:/Task1/Task2/3TestFiles LCS_output.csv
```
4. Your output file will be generated in the same Task2 folder and you will see some output display in the command prompt as well

#### Deliverables
1. There are three test folders available namely 3TestFiles, 10TestFiles and 100TestFiles containing java files for testing
2. The output files generated on each of these test folders are 3outputTestFiles.csv, 10outputTestFiles.csv and 100outputTestFiles.csv
3. The source code that tokenizes the input source code is reused and not written from scratch. Although I have written a C lexer back in my undergrad days, I decided to save time and reuse code.
4. I had to think a lot of how to go about detecting LCS over tokens and even thought of it as a clone detection problem and a sequential pattern mining problem. My previous code commits included trials of those but they failed because they do not consider consecitive tokens/itemsets. I also found a research paper which stated that this was an NP hard problem and they had provided a parallelized algorithm to mine LCS acrosss files. Finally I came across the LCS Solver from google and even though it is not giving perfect results, it works for the test data you provided.


#### Limitations
The Lexer doesnt tokenize square brackets. I added the code to recognize the square brackets but then the LCSSolver of Google wouldnt detect the sqaure brackets because it is primarily meant for word documents and not source code.

#### References
https://github.com/npgall/concurrent-trees/blob/master/documentation/LCSubstringSolverUsage.md
https://github.com/IraKorshunova/JavaCompiler

