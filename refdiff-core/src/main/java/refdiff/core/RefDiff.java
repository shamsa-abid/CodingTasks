package refdiff.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import refdiff.core.api.GitRefactoringDetector;
import refdiff.core.api.GitService;
import refdiff.core.api.Refactoring;
import refdiff.core.api.RefactoringHandler;
import refdiff.core.rm2.analysis.GitHistoryStructuralDiffAnalyzer;
import refdiff.core.rm2.analysis.RefDiffConfig;
import refdiff.core.rm2.analysis.RefDiffConfigImpl;
import refdiff.core.rm2.analysis.StructuralDiffHandler;
import refdiff.core.rm2.model.SDModel;
import refdiff.core.rm2.model.refactoring.SDRefactoring;
import refdiff.core.util.GitServiceImpl;

public class RefDiff implements GitRefactoringDetector {

   /* public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            throw new IllegalArgumentException("Usage: RefDiff <git-repo-folder> <commit-SHA1>");
        }
        final String folder = args[0];
        final String commitId = args[1];
        
        GitService gitService = new GitServiceImpl(); 
        try (Repository repo = gitService.openRepository(folder)) {
            GitRefactoringDetector detector = new RefDiff();
            detector.detectAtCommit(repo, commitId, new RefactoringHandler() {
                @Override
                public void handle(RevCommit commitData, List<? extends Refactoring> refactorings) {
                    if (refactorings.isEmpty()) {
                        System.out.println("No refactorings found in commit " + commitId);
                    } else {
                        System.out.println(refactorings.size() + " refactorings found in commit " + commitId + ": ");
                        for (Refactoring ref : refactorings) {
                            System.out.println("  " + ref);
                        }
                    }
                }
                @Override
                public void handleException(String commit, Exception e) {
                    System.err.println("Error processing commit " + commitId);
                    e.printStackTrace(System.err);
                }
            });
        }
    }*/
	
	public static void main(String[] args) throws Exception {
		
		//String repoURL = "https://github.com/mikaelhg/openblocks.git";
		//String outputFile = "output/openblocks.csv";
		//String cloneDir = "F:/mitopenblocks/openblocks";
		
		
		String repoURL = "https://github.com/jfree/jfreechart.git";
		String outputFile = "output/jfreechart.csv";
		String cloneDir = "F:/jfreechartclone/jfreecharts";
		
		
		//String repoURL = "https://github.com/shamsa-abid/CodingTasks.git";		
		//String outputFile = "output/outputRenameMethodRefactorings.csv";		
		//String repoURL = "https://github.com/jfree/jfreechart.git";
		
		//String repoURL = "https://github.com/danilofes/refactoring-toy-example.git";
		//String outputFile = "output/toy.csv";
		//String cloneDir = "tmp/toy";
				
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
					
					//String refName = ref.getName();
					//System.out.println(refName);
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

	/**
     * Detect refactorings performed in the specified commit. 
     * 
     * @param repository A git repository (from JGit library).
     * @param commitId The SHA key that identifies the commit.
     * @return A list with the detected refactorings. 
     */
    public List<SDRefactoring> detectAtCommit(Repository repository, String commitId) {
        List<SDRefactoring> result = new ArrayList<>();
        GitHistoryStructuralDiffAnalyzer sda = new GitHistoryStructuralDiffAnalyzer(config);
        sda.detectAtCommit(repository, commitId, new StructuralDiffHandler() {
            @Override
            public void handle(RevCommit commitData, SDModel sdModel) {
                result.addAll(sdModel.getRefactorings());
            }
        });
        return result;
    }

    private RefDiffConfig config;

    public RefDiff() {
        this(new RefDiffConfigImpl());
    }

    public RefDiff(RefDiffConfig config) {
        this.config = config;
    }

    private final class HandlerAdpater extends StructuralDiffHandler {
        private final RefactoringHandler handler;

        private HandlerAdpater(RefactoringHandler handler) {
            this.handler = handler;
        }

        @Override
        public void handle(RevCommit commitData, SDModel sdModel) {
            handler.handle(commitData, sdModel.getRefactorings());
        }

        @Override
        public void handleException(String commitId, Exception e) {
            handler.handleException(commitId, e);
        }
        
    }

    @Override
    public void detectAtCommit(Repository repository, String commitId, RefactoringHandler handler) {
        GitHistoryStructuralDiffAnalyzer sda = new GitHistoryStructuralDiffAnalyzer(config);
        sda.detectAtCommit(repository, commitId, new HandlerAdpater(handler));
    }

    @Override
    public String getConfigId() {
        return config.getId();
    }
}
