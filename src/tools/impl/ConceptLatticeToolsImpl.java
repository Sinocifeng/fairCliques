package tools.impl;

import entity.Node;
import entity.Pair;
import staticData.DataPara;
import tools.BasicCL;
import tools.ConceptLatticeTools;

import java.util.ArrayList;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.IntStream;

import static staticData.DataPara.hs;

public class ConceptLatticeToolsImpl implements ConceptLatticeTools {
    @Override
    public void generateConceptLattice(int[][] adjMat, Map<Integer, Node> nodes) {
        BasicCL basicCL = new BasicCLImpl();
        DataPara.bpcAttr = basicCL.getBPCliqueAttr(adjMat, DataPara.obj, DataPara.attr, DataPara.numObj, DataPara.numObj); //属性-对象pair
        DataPara.dictAll = basicCL.buildDict(adjMat, DataPara.obj, DataPara.attr, DataPara.numObj, DataPara.numObj);       //对象-属性pair&&属性-对象pair
        DataPara.objResult = basicCL.objResult(DataPara.attr, DataPara.obj, DataPara.bpcAttr, DataPara.dictAll);   //计算当前属性对应对象集以及与之前其他属性对应的所有对象集合之间的交集
        DataPara.CL = basicCL.FinalBpcAll(DataPara.objResult, DataPara.dictAll);

        for (Pair<ArrayList<String>, ArrayList<String>> pair : DataPara.CL) {
            ArrayList<String> newA = pair.getL();
            ArrayList<String> newB = pair.getR();

            if (newA.size() == newB.size() && newA.size() >= hs.size() &&
                    IntStream.range(0, newA.size()).allMatch(j -> newA.get(j).equals(newB.get(j).substring(1)))) {

                DataPara.objSet.add(newA);
                DataPara.itemSet.add(newB);

                String content = AtoS(newA) + "#" + AtoS(newB);
                DataPara.bpcAllCLHashSet.add(content);
            }
        }
    }
    private String AtoS(ArrayList<String> input) {
        StringJoiner joiner = new StringJoiner(" ");
        for (String s : input) {
            joiner.add(s);
        }
        return joiner.toString();
    }

}
