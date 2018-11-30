package refdiff.task1;

/**
 * Hello world!
 *
 */
import java.util.List;

import org.eclipse.jgit.lib.Repository;

import refdiff.core.RefDiff;
import refdiff.core.api.GitService;
import refdiff.core.rm2.model.refactoring.SDRefactoring;
import refdiff.core.util.GitServiceImpl;

public class App {
    public static void main(String[] args) throws Exception {
        RefDiff refDiff = new RefDiff();
        GitService gitService = new GitServiceImpl();
        try (Repository repository = gitService.cloneIfNotExists("F:/tmp/jersey", "https://github.com/jersey/jersey.git")) {
            List<SDRefactoring> refactorings = refDiff.detectAtCommit(repository, "d94ca2b27c9e8a5fa9fe19483d58d2f2ef024606");
            for (SDRefactoring r : refactorings) {
                System.out.printf("%s\t%s\t%s\n", r.getRefactoringType().getDisplayName(), r.getEntityBefore().key(), r.getEntityAfter().key());
            }
        }
    }
}
