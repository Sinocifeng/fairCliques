package prepare.impl;

import prepare.PrepareData;
import staticData.DataPara;

import java.io.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class PrepareDataImpl implements PrepareData {

    static Scanner sc;
    static FileWriter fw;
    static PrintWriter pw;
    private static String filepath = "data/rawData/" + DataPara.fileName;
    private static String writeFilepath = "data/resultData/" + DataPara.fileName + ".gml";
    @Override
    public void prepareWork() throws Exception {
        File file = new File(filepath);
        sc = new Scanner(file);
        //如果文件存在，则追加内容；如果文件不存在，则创建文件
        File f = new File(writeFilepath);
        fw = new FileWriter(f, true);
        pw = new PrintWriter(fw);
        pw.println("graph\n[");

        pw.flush();
        fw.flush();
    }

    @Override
    public void wirteNodes(int cols) throws IOException {
        int max = getMaxNodeNum(sc, cols);

        for (int i=0;i<max;i++){
            int nodeId = i + 1;
            int attrValue = getAttrValue(nodeId);

            pw.println("  node");
            pw.println("  [");
            pw.println("    id " + nodeId);
            pw.println("    value " + attrValue);
            pw.println("  ]");

            pw.flush();
            fw.flush();
        }
        System.out.println("节点个数为：" + max);
    }

    private static int getMaxNodeNum(Scanner sc, int cols) {
        Set<String> set = new HashSet<>();

        while (sc.hasNextLine()) {
            String readdata =  sc.nextLine();
            String res[] = readdata.trim().split("[\\s\t]+");

            if (res.length != cols){
                System.err.print(readdata);
                System.err.println("  The data in the currently read row is not in " + cols + " columns, please check it!");
            }
            set.add(res[0]);
            set.add(res[1]);
        }

        int max = set.stream()
                .map(Integer::parseInt)
                .max(Integer::compare)
                .orElse(0);
        return max;
    }

    private static int getAttrValue(int i){
        // Here you can override your method of generating attribute values
        return new Random().nextInt(4)+ 1;
    }

    @Override
    public void writeEdges() throws IOException {
        sc.close();
        sc = new Scanner(new File(filepath));

        while (sc.hasNextLine()) {
            String readdata =  sc.nextLine();
            String res[] = readdata.trim().split(" |\t");

            int from = Integer.parseInt(res[0]);
            int to = Integer.parseInt(res[1]);

            pw.println("  edge");
            pw.println("  [");
            pw.println("    source "+(from));
            pw.println("    target "+(to));
            pw.println("  ]");

            pw.flush();
            fw.flush();
        }
    }

    @Override
    public void closeWork() throws IOException {
        pw.println("]");
        pw.close();
        fw.close();
        sc.close();
    }
}
