package refdiff.task1;

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
        RefDiff refDiff = new RefDiff();
        GitService gitService = new GitServiceImpl();
        try (Repository repository = gitService.cloneIfNotExists("F:/tmp/dp", "https://github.com/iluwatar/java-design-patterns.git")) {
        	
           
            
            Collection<Ref> allRefs = repository.getAllRefs().values();

	         // a RevWalk allows to walk over commits based on some filtering that is defined
	         try (RevWalk revWalk = new RevWalk( repository )) {
	             for( Ref ref : allRefs ) {
	                 revWalk.markStart( revWalk.parseCommit( ref.getObjectId() ));
	             }
	             System.out.println("Walking all commits starting with " + allRefs.size() + " refs: " + allRefs);
	             int count = 0;
	             for( RevCommit commit : revWalk ) {
	                 System.out.println("Commit: " + commit);
	                 
	                 List<SDRefactoring> refactorings = refDiff.detectAtCommit(repository, commit.name());
	                 for (SDRefactoring r : refactorings) {
	                     System.out.printf("%s\t%s\t%s\n", r.getRefactoringType().getDisplayName(), r.getEntityBefore().key(), r.getEntityAfter().key());
	                 }
	                 
	                 count++;
	             }
	             System.out.println("Had " + count + " commits");
	         }
        }
    }
}
