package tools.impl;

import entity.ConceptPair;
import jxl.Workbook;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import staticData.DataPara;
import tools.DataCalTools;
import tools.ExcelTools;
import tools.InteDegTools;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import java.io.File;
import static staticData.DataPara.*;

public class DataCalToolsImpl implements DataCalTools {
    @Override
    public void calIdx() {
        ExcelTools excelTools = new ExcelToolsImpl();
        excelTools.prepareWriteWork();

        for(int i = 0; i < DataPara.objSet.size(); i++ ) {
            ArrayList<String> extend = new ArrayList<>(DataPara.objSet.get(i));
            ArrayList<String> intend = new ArrayList<>(DataPara.itemSet.get(i));

            double fair_idx = calFairidx(new ArrayList<>(extend), new ArrayList<>(intend));
            double stability = 0;

            ArrayList<Object> conData = new ArrayList<>(Arrays.asList(
                    i+1, arrayToString(extend), arrayToString(intend),
                    andTends(extend, intend), stability, fair_idx));
            excelTools.appendLine(conData);
        }
        excelTools.closeWriteWork();
    }

    @Override
    public void calculateDegree() {
        File file = new File("your_excel_file.xls");
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(file);
            WritableWorkbook copy = Workbook.createWorkbook(file, workbook);

            updateDegree(copy);

            copy.write();
            copy.close();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateDegree(WritableWorkbook copy) throws WriteException {
        for (int i = 0; i < copy.getNumberOfSheets(); i++) {
            WritableSheet sheet = copy.getSheet(i);
            for (int row = 1; row < sheet.getRows(); row++) {

                String extent = sheet.getCell(1, row).getContents();                    //外延
                String intent = sheet.getCell(2, row).getContents();
                double fairIdx = Double.parseDouble(sheet.getCell(5, row).getContents());


                double stab = calCStab(adjMat, getList(extent), getList(intent));
                double maxIndex = Math.pow(2, maxindex);
                double fair = Math.pow(2, fairIdx) / maxIndex;

                jxl.write.Number numStab = new jxl.write.Number(4, row, stab);
                jxl.write.Number numFair = new jxl.write.Number(5, row, fair);
                double fairDis =  (1 - stab) * fair;
                jxl.write.Number numDis = new jxl.write.Number(6, row, fairDis);
                Label isRes = new Label(7, row, fairDis <= 1.0 / maxIndex ? "Y" : "N");

                sheet.addCell(numStab);
                sheet.addCell(numFair);
                sheet.addCell(numDis);
                sheet.addCell(isRes);
            }
        }
    }

    private double calCStab(int[][] adjM, List<Integer> extentLst, List<Integer> intentLst){

        int[][] newAdjM = delAdjM(extentLst, intentLst, adjM);
        List<Integer> list1 = delList(extentLst,intentLst);
        List<Integer> list2 = delList(intentLst,extentLst);

        ConceptPair concept = new ConceptPair(list1,list2);
        InteDegTools tools = new InteDegToolsImpl();

        DataPara.dictAll = tools.buildDict(adjM);
        return tools.calStab(concept, newAdjM);
    }

    private int[][] delAdjM(List<Integer> list1, List<Integer> list2, int[][] adjM) {
        List<Integer> collect = Stream.concat(list1.stream(), list2.stream())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        int size = collect.size();
        int[][] newAdjM = new int[size][size];
        IntStream.range(0, size)
                .forEach(i -> IntStream.range(0, size)
                        .forEach(j -> newAdjM[i][j] = adjM[collect.get(i) - 1][collect.get(j) - 1]));

        return newAdjM;
    }

    private List<Integer> getList(String string) {
        String[] strs = string.replaceAll("[\\[\\]a,]", "").split("\\s+");

        List<Integer> list = Arrays.stream(strs)
                .filter(str -> !str.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        return list;
    }

    private List<Integer> delList(List<Integer> listA, List<Integer> listB) {
        //合并,排序
        List<Integer> collect = Stream.of(listA, listB)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        Collections.sort(collect);

        List<Integer> newList = new ArrayList<>();
        for (int i = 0; i < listA.size(); i++) {
            int index = collect.indexOf(listA.get(i)) + 1;
            newList.add(index);
        }
        return newList;
    }

    private double calFairidx(List<String> extend, List<String> intend){
        List<String> newintend = intend.stream()
                .map(s -> s.substring(1)) // 去除每个元素开头的'a'
                .collect(Collectors.toList());
        List<Integer> subGraphlist = Stream.concat(extend.stream(), newintend.stream() ) 
                .map(Integer::parseInt) 
                .collect(Collectors.toList()); 
        int[] subGraph = subGraphlist.stream().mapToInt(i -> i).toArray();

        int[] attrNum = new int[DataPara.hs.size()];     
        Arrays.stream(subGraph).forEach(i -> {
            int attr_j = nodes.get(i).getValue();
            attrNum[attr_j-1] ++;
        });

        Arrays.sort(attrNum);
        int index = 0;
        for (int i = 0; i < attrNum.length; i++) {
            index += attrNum[i] * (2 * i - attrNum.length + 1);
        }

        maxindex = maxindex < index ? index : maxindex;
        return index;
    }

    private String arrayToString(ArrayList<String> list){
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (String str : list) {
            joiner.add(str);
        }
        return joiner.toString();
    }

    private String andTends(ArrayList<String> extend, ArrayList<String> intend) {

        ArrayList<String> newextend = (ArrayList<String>) extend.clone();
        List<String> newintend = intend.stream()
                .map(s -> s.substring(1))
                .collect(Collectors.toList());

        newextend.addAll(newintend);
        newextend = (ArrayList<String>) newextend.stream().distinct().collect(Collectors.toList());
        return arrayToString(newextend);
    }

}
