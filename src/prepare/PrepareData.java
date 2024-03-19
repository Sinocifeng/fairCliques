package prepare;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface PrepareData {
    public void prepareWork() throws Exception;

    public void wirteNodes(int cols) throws IOException;

    public void writeEdges() throws IOException;

    public void closeWork() throws IOException;

}
