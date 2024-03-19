import prepare.PrepareData;
import prepare.impl.PrepareDataImpl;
import staticData.DataPara;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class DataPreprocessing {

    public static void main(String[] args) {
        PrepareData prepare = new PrepareDataImpl();
        try {
            prepare.prepareWork();
            int cols = 2;

            prepare.wirteNodes(cols);
            prepare.writeEdges();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                prepare.closeWork();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
