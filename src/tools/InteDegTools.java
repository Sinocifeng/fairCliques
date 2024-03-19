package tools;

import entity.ConceptPair;

import java.util.ArrayList;
import java.util.HashMap;

public interface InteDegTools {
    // 进行概念兴趣度计算
    public double calStab(ConceptPair concept, int[][] adjMat);

    public HashMap<String, ArrayList<String>> buildDict(int[][] adjMat);
}
