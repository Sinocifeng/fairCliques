package tools.impl;

import entity.Node;
import tools.DataTools;
import staticData.DataPara;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

import static staticData.DataPara.hs;
import static staticData.DataPara.nodes;

public class DataToolsImpl implements DataTools {
    private static int adjuPara = 1;  //针对数据源中节点id必须从1还是0开始编号，在构造邻接矩阵时的偏移量
    private static int[][] adjMat;

    @Override
    public int[][] inputData() {
        try {
            getNodes(nodes);
            getAdjMat(nodes.size());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return adjMat;
    }

    private String readParameter(String line, String paramName) {
        String[] parts = line.split(" ");
        if (parts.length == 2 && parts[0].equals(paramName)) {
            return parts[1];
        }
        return null;
    }

    private void getNodes(Map<Integer, Node> nodes) throws FileNotFoundException {
        File file = new File(DataPara.readFileName);
        Scanner sc = new Scanner(file);

        while (sc.hasNextLine()) {
            StringBuilder textContent = new StringBuilder(sc.nextLine().trim());
            if (textContent.equals("node")) {
                Node node = new Node();
                textContent = new StringBuilder(sc.nextLine().trim());

                if (textContent.equals("[")) {
                    String nodeId = readParameter(sc.nextLine().trim(), "id");
                    if (nodeId != null) {
                        int id = Integer.parseInt(nodeId);
                        if (id == 0) adjuPara = 0;
                        node.setId(id);
                    }

                    String nodeValue = readParameter(sc.nextLine().trim(), "value");
                    if (nodeValue != null) {
                        int value = Integer.parseInt(nodeValue);
                        hs.add(String.valueOf(value));
                        node.setValue(value);
                    }

                    String closingBracket = sc.nextLine().trim();
                    if (closingBracket.equals("]")) {
                        nodes.put(node.getId(), node);
                    }
                } else {
                    System.err.println("File format error, please check it!");
                }
            }
        }

        DataPara.numObj = nodes.size();
        for (int i = 0; i < DataPara.numObj; i++) {
            DataPara.obj.add(String.valueOf((i + 1)));
            DataPara.attr.add("a" + (i + 1));
        }

        sc.close();
    }

    private static void getAdjMat(int size) throws FileNotFoundException {
        File file = new File(DataPara.readFileName);
        Scanner sc = new Scanner(file);

        adjMat = new int[size][size];
        while (sc.hasNextLine()) {
            String temp = sc.nextLine().trim();
            if (temp.equals("edge")) {
                checkNextLine(sc, "[", "The file input format is wrong, the line after edge is not '['");
                String[] res1 = checkAndSplit(sc.nextLine().trim(), "source", "File input format error, lack of 'source' keyword in edge");
                String[] res2 = checkAndSplit(sc.nextLine().trim(), "target", "File input format error, lack of 'target' keyword in edge");
                checkNextLine(sc, "]", "The file input format is incorrect and the end character of the edge data block is not ']'");

                int i = Integer.parseInt(res1[1]) - adjuPara;
                int j = Integer.parseInt(res2[1]) - adjuPara;

                adjMat[i][j] = 1;
                adjMat[j][i] = 1;
            }
        }

        // 对主对角线全部赋值为1
        for (int i = 0; i < adjMat.length; i++) {
            adjMat[i][i] = 1;
        }

        sc.close();
    }

    private static void checkNextLine(Scanner sc, String expected, String errorMessage) {
        String temp = sc.nextLine().trim();
        if (!temp.equals(expected)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private static String[] checkAndSplit(String line, String expected, String errorMessage) {
        String[] parts = line.split(" ");
        if (parts.length < 2 || !parts[0].equals(expected)) {
            throw new IllegalArgumentException(errorMessage);
        }
        return parts;
    }
}
