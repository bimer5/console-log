import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class ConsoleLogAction extends AnAction {
    private List<String> acceptableFiles = Arrays.asList("js", "ts", "jsx", "tsx");

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        final Editor editor = anActionEvent.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = anActionEvent.getRequiredData(CommonDataKeys.PROJECT);
        generateConsoleLog(project, editor);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        if (psiFile == null) {
            e.getPresentation().setEnabledAndVisible(false);
            return;
        }
        String fileType = psiFile.getFileType().getDefaultExtension();
        boolean isAcceptableFileType = acceptableFiles.contains(fileType.toLowerCase());
        e.getPresentation().setEnabledAndVisible(isAcceptableFileType);
    }

    private void generateConsoleLog(Project project, Editor editor ){
        Document document = editor.getDocument();
        Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();

        primaryCaret.selectWordAtCaret(true);
        String selectedText = primaryCaret.getSelectedText();

        primaryCaret.removeSelection();

        int visualLineEnd = primaryCaret.getVisualLineEnd();
        WriteCommandAction.runWriteCommandAction(project, () ->
                document.insertString(visualLineEnd, "console.log('" + selectedText + "', " + selectedText + ");\n"));
    }
}
