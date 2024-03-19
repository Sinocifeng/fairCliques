package tools;

import java.util.ArrayList;

public interface ExcelTools {

    public void prepareWriteWork();

    public void appendLine(ArrayList<Object> conData);

    public void closeWriteWork();
}
