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
import org.eclipse.jgit.revwalk.RevWalk;

import refdiff.core.RefDiff;
import refdiff.core.api.GitService;
import refdiff.core.rm2.model.refactoring.SDRefactoring;
import refdiff.core.util.GitServiceImpl;

public class App {
	public static void main(String[] args) throws Exception {
		
		String repoURL = "https://github.com/shamsa-abid/CodingTasks.git";
		//String repoURL = "https://github.com/danilofes/refactoring-toy-example.git";
		//String outputFile = "output/outputRenameMethodRefactorings.csv";
		String outputFile = "output/outputChangeSignatureRefactoringsCodingTasks.csv";
		String cloneDir = "tmp/codingtaskexample";
				
		if(args.length > 0)
		{
			repoURL = args[0];
			outputFile = args[1];
			cloneDir = args[2];
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
						
						if(r.getRefactoringType().name() == "CHANGE_METHOD_SIGNATURE" )
						{
							addParamPefactoringCount ++;
							System.out.printf("%s\t%s\t%s\n", r.getRefactoringType().getDisplayName(), r.getEntityBefore().key(), r.getEntityAfter().key());
							String before = r.getEntityBefore().key().toString();
							String after = r.getEntityAfter().key().toString();
							String javaFile = before.substring(0, before.indexOf("#"));
							String oldSign = before.substring(before.indexOf("#") + 1);
							String newSign = after.substring(after.indexOf("#") + 1);
							br.write(commit.name() + "," + javaFile + "," + oldSign + "," + newSign+ "\n");
						}
						//else
							//System.out.println("Method signature change refactoring not detected");
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
}
