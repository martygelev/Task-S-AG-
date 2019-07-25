import Exceptions.FileException;
import Exceptions.FileTraverserException;
import common.Writable;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class Tests {

    @Test
    public void correctTextCheck() {
        Writable writer = new StringBuilderWriter();
        String testText = "bla";

        writer.write(testText);

        assertEquals(testText, writer.getResult());
    }

    @Test
    public void incorrectTextCheck() {
        Writable writer = new StringBuilderWriter();
        String testText = "bla";
        String otherText = "bla";

        writer.write(testText);

        assertNotEquals(otherText, writer.getResult());
    }


    @Test(expected=FileException.class)
    public void wrongPathMustThrowAnExcepriton() throws FileException, IOException, FileTraverserException {
        Writable writer = new StringBuilderWriter();
        FileTraverser fileTraverser = new FileTraverser(writer);

        String anyInvalidPath = "Macintosh-HD/Users/Invalid/invalid/bla/bla/bla/bla/";
        String searchWord = "bla";

        fileTraverser.traverseDirectory(anyInvalidPath, searchWord);
    }

    @Test
    public void correctPathMustPassTest() throws FileException, IOException {
        Writable writer = new StringBuilderWriter();
        FileTraverser fileTraverser = new FileTraverser(writer);
        StringBuilder sb = new StringBuilder();
        sb.append("File name: task.txt, size: 55 bytes").append(System.lineSeparator());
        sb.append("File name: task2.txt, size: 66 bytes").append(System.lineSeparator());
        sb.append("File name: zip.txt, size: 15 bytes").append(System.lineSeparator());

        String validPath = "Macintosh-HD/Users/martygelev/dev/SoftwareAG-task/src/test/java/";
        String searchWord = "task";

        String result = fileTraverser.traverseDirectory(validPath, searchWord);

        assertEquals(sb.toString(), result);
    }
}
