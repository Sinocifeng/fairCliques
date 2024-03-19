package tools.impl;

import entity.BPCliques;
import entity.Pair;
import staticData.DataPara;
import tools.BasicCL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

public class BasicCLImpl implements BasicCL {
    public HashSet<ArrayList<String>> attrResult = new HashSet<>();

    @Override
    public BPCliques getBPCliqueAttr(int[][] adjMat, ArrayList<String> obj, ArrayList<String> attr, Integer numObj, Integer numAttr) {
        BPCliques tmpBpc = new BPCliques();

        Pair<ArrayList<String>, ArrayList<String>> tmpPair;

        for (int i = 0; i < numAttr; i++) {
            ArrayList<String> tmpAttr = new ArrayList<>();
            ArrayList<String> tmpList = new ArrayList<>();

            tmpAttr.add(attr.get(i));

            for (int j = 0; j < numObj; j++) {
                if (adjMat[j][i] == 1) {
                    tmpList.add(obj.get(j));
                }
            }
            tmpBpc.add(new Pair<>(new ArrayList<>(tmpAttr), new ArrayList<>(tmpList)));
        }
        return tmpBpc;
    }

    @Override
    public HashMap<String, ArrayList<String>> buildDict(int[][] adjMat, ArrayList<String> obj, ArrayList<String> attr, Integer numObj, Integer numAttr) {
        HashMap<String, ArrayList<String>> dictTemp = new HashMap<>();

        // 构建对象-属性的映射关系
        IntStream.range(0, numObj).forEach(i -> {
            ArrayList<String> relatedAttr = new ArrayList<>();
            IntStream.range(0, numAttr)
                    .filter(j -> adjMat[i][j] == 1)
                    .forEach(j -> {
                        relatedAttr.add(attr.get(j));
                        dictTemp.computeIfAbsent(attr.get(j), k -> new ArrayList<>()).add(obj.get(i));
                    });
            dictTemp.put(obj.get(i), relatedAttr);
        });
        // 构建属性-对象的映射关系
        IntStream.range(0, numAttr).forEach(i -> {
            ArrayList<String> relatedObj = new ArrayList<>();
            IntStream.range(0, numObj)
                    .filter(j -> adjMat[j][i] == 1)
                    .forEach(j -> {
                        relatedObj.add(obj.get(j));
                        dictTemp.computeIfAbsent(attr.get(i), k -> new ArrayList<>()).add(obj.get(j));
                    });
            dictTemp.put(attr.get(i), relatedObj);
        });
        return dictTemp;
    }

    @Override
    public HashSet<ArrayList<String>> objResult(ArrayList<String> attr, ArrayList<String> obj, BPCliques bpcAttr, HashMap<String, ArrayList<String>> dictAll) {
        if (DataPara.objResult == null || DataPara.objResult.size() == 0) {
            DataPara.objResult.add(new ArrayList<>(obj));
        }
        for (int i = 0; i < attr.size(); i++) {
            ArrayList<ArrayList<String>> objTemp = new ArrayList<>(DataPara.objResult);
            ArrayList<String> oneObj = bpcAttr.get(i).getR(); // 获取属性i对应的所有对象
            objTemp.forEach(temp -> temp.retainAll(oneObj)); // 保留在指定集合oneObj中也存在的元素
            DataPara.objResult.addAll(objTemp);
        }

        DataPara.objResult.removeIf(ArrayList::isEmpty); // 移除空内容的集合
        return DataPara.objResult;
    }

    @Override
    public BPCliques FinalBpcAll(HashSet<ArrayList<String>> objResult, HashMap<String, ArrayList<String>> dictAll) {
        BPCliques finalBpCliques = new BPCliques();
        for(ArrayList<String> obj : objResult){
            Pair pair = intersectForObject(obj,dictAll);
            if (pair != null) {
                finalBpCliques.add(pair);
            }
        }
        return finalBpCliques;
    }

    private Pair intersectForObject(ArrayList<String> obj,HashMap<String, ArrayList<String>> dictAll){
        ArrayList<String> intersection = null;

        for (String obt : obj) {
            ArrayList<String> attr = dictAll.get(obt);
            if (attr != null) {
                if (intersection == null) {
                    intersection = new ArrayList<>(attr);
                } else {
                    intersection.retainAll(attr);
                }
            }
        }
        if (intersection != null) {
            return new Pair<>(obj, intersection);
        }
        return null;
    }


    @Override
    public List<String> shangYS(String pow, HashMap<String, ArrayList<String>> totalDictAll) {
        List<String> AttList = new ArrayList<>();
        if (pow == null) {
            return AttList;
        }

        String[] ss = pow.split("/");
        for (int i = 0; i < ss.length; i++) {
            String obt = ss[i];
            if (i == 0) {
                ArrayList<String> attr = totalDictAll.get(obt);
                if (attr != null) {
                    AttList.addAll(attr);
                }
            } else {
                List<String> currentList = totalDictAll.get(obt);
                if (currentList != null) {
                    AttList.retainAll(currentList);
                } else {
                    AttList.clear();
                    break;
                }
            }
        }
        return AttList;
    }

    @Override
    public int Supp(String pow, HashMap<String, ArrayList<String>> totalDictAll) {
        List<String> AttList = new ArrayList<>();
        if (pow == null || pow.isEmpty()) {
            return 0;
        }

        String[] ss = pow.split("/");
        for (int i = 0; i < ss.length; i++) {
            String obt = ss[i];
            ArrayList<String> attr = totalDictAll.get(obt);
            if (attr != null) {
                if (AttList.isEmpty()) {
                    AttList.addAll(attr);
                } else {
                    AttList.retainAll(attr);
                }
            } else {
                return 0;
            }
        }
        return AttList.size();
    }

    @Override
    public int Supp(List<String> pow, HashMap<String, ArrayList<String>> totalDictAll) {
        List<String> AttList = new ArrayList<>();
        if (pow == null || pow.isEmpty()) {
            return 0;
        }

        for (String obt : pow) {
            obt = obt.substring(0, obt.length() - 1);
            ArrayList<String> attr = totalDictAll.get(obt);
            if (attr != null) {
                if (AttList.isEmpty()) {
                    AttList.addAll(attr);
                } else {
                    AttList.retainAll(attr);
                }
            } else {
                return 0;
            }
        }
        return AttList.size();
    }



}
