package prepare.impl;

import staticData.DataPara;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class GenAttrValues {
	public static String filepath = "data/rawData/" + DataPara.fileName;
	protected int genAttrs_Random(int attrNum){
		return new Random().nextInt(attrNum)+ 1;
	}

	protected int genAttrs_method(int modeNo){
		File file = new File(filepath);
        int nums = 0;

        try (Scanner scc = new Scanner(file)) {
            while (scc.hasNext()) {
                String readdata = scc.nextLine();
                String[] res = readdata.trim().split("[ |\t]+");
                if (Integer.parseInt(res[0]) == modeNo || Integer.parseInt(res[1]) == modeNo) {
                    nums += Integer.parseInt(res[2]);
                }
            }
            System.out.println(modeNo + " , " + nums);

        }catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return (int) Math.min(Math.log10(nums) + 1, 3);
	}
}
