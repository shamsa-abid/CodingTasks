package refdiff.task1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Collection;
/**
 * Hello world!
 *
 */
import java.util.List;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import refdiff.core.RefDiff;
import refdiff.core.api.GitService;
import refdiff.core.rm2.model.refactoring.SDRefactoring;
import refdiff.core.util.GitServiceImpl;

public class App {
	public static void main(String[] args) throws Exception {

		String repoURL = "https://github.com/mikaelhg/openblocks.git";
		String outputFile = "output/openblocks.csv";
		String cloneDir = "F:/mitopenblocks/openblocks";


		//String repoURL = "https://github.com/jfree/jfreechart.git";
		//String outputFile = "output/jfreechart.csv";
		//String cloneDir = "F:/jfreechartclone/jfreecharts";	


		//String repoURL = "https://github.com/danilofes/refactoring-toy-example.git";
		//String outputFile = "output/toy.csv";
		//String cloneDir = "tmp/toy";

		if(args.length > 0)
		{
						
			cloneDir = args[0];
			outputFile = args[1];
			repoURL = args[2];
		}

		RefDiff refDiff = new RefDiff();
		GitService gitService = new GitServiceImpl();


		File file = new File(outputFile);
		FileWriter fr = new FileWriter(file);
		BufferedWriter br = new BufferedWriter(fr);
		br.write("Commit SHA, Java File, Old function signature, New function signature\n");

		try (Repository repository = gitService.cloneIfNotExists(cloneDir, repoURL)) {			
			
			Collection<Ref> allRefs = repository.getAllRefs().values();

			// a RevWalk allows to walk over commits based on some filtering that is defined
			try (RevWalk revWalk = new RevWalk( repository )) {
				for( Ref ref : allRefs ) {
					revWalk.markStart( revWalk.parseCommit( ref.getObjectId() ));

				}
				//System.out.println("Walking all commits starting with " + allRefs.size() + " refs: " + allRefs);
				int count = 0;
				int refactoringCount = 0;
				int addParamPefactoringCount = 0;

				for( RevCommit commit : revWalk ) {
					//System.out.println("Commit: " + commit);	    
				       
					
					List<SDRefactoring> refactorings = refDiff.detectAtCommit(repository, commit.name());
					for (SDRefactoring r : refactorings) {
						refactoringCount ++;

						
						if(r.getRefactoringType().name() == "RENAME_METHOD" )
						{
							if(isAddParameterRefactoring(r))
							{
								/*RevTree tree = commit.getTree();								
								TreeWalk treeWalk = new TreeWalk(repository);
								treeWalk.addTree(tree);
								 treeWalk.setRecursive(true);
							        while (treeWalk.next()) {
							            System.out.println("found: " + treeWalk.getPathString());							          
							        } */
								
								addParamPefactoringCount ++;
								String before = r.getEntityBefore().key().toString();
								String after = r.getEntityAfter().key().toString();
								String javaFile = before.substring(0, before.indexOf("#"));
								String className = javaFile.substring(javaFile.lastIndexOf(".")+1);

								String oldSign = before.substring(before.indexOf("#") + 1);
								String newSign = after.substring(after.indexOf("#") + 1);

								if(oldSign.indexOf("(")==0)
								{
									oldSign = className.concat(oldSign);
									newSign = className.concat(newSign);

								}
								System.out.printf("%s\t%s\t%s\n","Parameter Added ", oldSign, newSign);

								br.write(commit.name() + "," + javaFile + "," + oldSign + "," + newSign+ "\n");
							}
						}

					}

					count++;
				}
				br.close();
				fr.close();
				System.out.println("Had " + count + " commits");
				System.out.println("Had " + refactoringCount + " various refactorings");
				System.out.println(addParamPefactoringCount + " add parameter refactorings detected");
			}
		}
	}

	private static boolean isAddParameterRefactoring(SDRefactoring r) {

		boolean result=false;
		String before = r.getEntityBefore().simpleName();
		String after = r.getEntityAfter().simpleName();
		String betweenBracketsBefore = before.substring(before.indexOf("(")+1, before.indexOf(")"));
		String betweenBracketsAfter = after.substring(after.indexOf("(")+1, after.indexOf(")"));

		if(!betweenBracketsBefore.contentEquals(betweenBracketsAfter))
		{
			if(betweenBracketsBefore.trim().contentEquals("") && !betweenBracketsAfter.trim().contentEquals(""))
			{
				result = true;
			}
			else
			{
				long commaCountBefore = betweenBracketsBefore.chars().filter(ch -> ch == ',').count();
				long commaCountAfter = betweenBracketsAfter.chars().filter(ch -> ch == ',').count();
				if(commaCountAfter > commaCountBefore)
				{
					result = true;
				}
			}

		}
		return result;
	}
}
