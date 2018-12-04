## CodingTasks

This repository contains two coding tasks.

### Task 1: Refactoring detection across commits

#### How to execute and test the code

1. Download or clone the git repo
2. Navigate to the folder Task1 in command prompt
3. If a repo is already cloned to your machine, execute the command 
```
java -jar AddParameterRefactoringDetector.jar <directory name of cloned repo> <name of output.csv file> cloned
```

For example:
```
java -jar AddParameterRefactoringDetector.jar F:/tmp/repoClone output.csv cloned
```

4. If a repo needs to be cloned then mention the repo URL and execute the command 
```
java -jar AddParameterRefactoringDetector.jar <directory name to clone repo> <name of output.csv file> <repo URL> 
```

For example:
```
java -jar AddParameterRefactoringDetector.jar F:/tmp/repoClone output.csv https://github.com/mikaelhg/openblocks.git
```
4. Your output file will be generated in the same Task1 folder and you will see some output display in the command prompt as well

#### Deliverables
To check my code please refer to the main file Task1\src\main\java\refdiff\task1\App.java
I executed my code on the following two repositories
1. https://github.com/mikaelhg/openblocks.git
2. https://github.com/danilofes/refactoring-toy-example.git

The results from these repositories can be found in the following path Task1/output



#### References
[1] https://github.com/aserg-ufmg/RefDiff
[2] https://github.com/tsantalis/RefactoringMiner
[3] https://github.com/SEAL-UCLA/Ref-Finder


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
To check my code please refer to the main file Task2\src\_1_Task2\LongestCommmonTokenSequenceDetector.java
1. There are three test folders available namely 3TestFiles, 10TestFiles and 100TestFiles containing java files for testing
2. The output files generated on each of these test folders are 3outputTestFiles.csv, 10outputTestFiles.csv and 100outputTestFiles.csv
3. The source code that tokenizes the input source code is reused and not written from scratch. Although I have written a C lexer back in my undergrad days, I decided to save time and reuse code.
4. I had to think a lot of how to go about detecting LCS over tokens and even thought of it as a clone detection problem and a sequential pattern mining problem. My previous code commits included trials of those but they failed because they do not consider consecitive tokens/itemsets. Finally I came across the LCS Solver from google and even though it is not giving perfect results, it works for the test data you provided.


#### Overcoming Limitations
The Lexer didnt tokenize square brackets. I added the code to recognize the square brackets.

#### References
https://github.com/npgall/concurrent-trees/blob/master/documentation/LCSubstringSolverUsage.md
https://github.com/IraKorshunova/JavaCompiler

