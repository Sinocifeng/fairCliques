package tools.impl;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import staticData.DataPara;
import tools.ExcelTools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ExcelToolsImpl implements ExcelTools {

    int sheetNo;
    int location;
    static WritableWorkbook book;
    WritableSheet sheet;

    @Override
    public void prepareWriteWork() {
        try {
            sheetNo = 0;
            createNewSheet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void appendLine(ArrayList<Object> conData) {
        try {
            for (int i = 0; i < conData.size(); i++) {
                Object data = conData.get(i);
                if (data instanceof Integer) {
                    jxl.write.Number number = new jxl.write.Number(i, location, (int) data);
                    sheet.addCell(number);
                } else if (data instanceof Double) {
                    jxl.write.Number number = new jxl.write.Number(i, location, (Double) data);
                    sheet.addCell(number);
                } else if (data instanceof String) {
                    Label label = new Label(i, location, (String) data);
                    sheet.addCell(label);
                }
            }
            location++;

            if (DataPara.conNO % 60000 == 0){
                book.write();
                sheetNo++;
                createNewSheet();
            }
            DataPara.conNO++;
        }  catch (WriteException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createNewSheet() throws WriteException, IOException {
        System.out.println("sheetNo =" + sheetNo);
        if (book != null) {
            closeWriteWork();
        }
        book = Workbook.createWorkbook(new File("data/xlsData_IoT/" + DataPara.fileName + ".xls"));
        sheet = book.createSheet("NO" + (sheetNo + 1) + "Page", sheetNo);
        String[] headers = {"概念编号", "概念外延", "概念内涵", "子图", "稳定性", "公平性", "fair Dis", "Result"};
        for (int i = 0; i < headers.length; i++) {
            Label label = new Label(i, 0, headers[i]);
            sheet.addCell(label);
        }
        location = 1;
    }

    @Override
    public void closeWriteWork(){
        try {
            book.write();
            book.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }
}
