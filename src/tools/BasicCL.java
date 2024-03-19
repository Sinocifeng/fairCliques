package tools;

import entity.BPCliques;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public interface BasicCL {

    public BPCliques getBPCliqueAttr(int[][] adjMat, ArrayList<String> obj, ArrayList<String> attr, Integer numObj, Integer numAttr);

    public HashMap<String,ArrayList<String>> buildDict(int[][] adjMat, ArrayList<String> obj, ArrayList<String> attr, Integer numObj, Integer numAttr);

    public HashSet<ArrayList<String>> objResult(ArrayList<String> attr, ArrayList<String> obj, BPCliques bpcAttr, HashMap<String, ArrayList<String>> dictAll);

    public BPCliques FinalBpcAll(HashSet<ArrayList<String>> objResult, HashMap<String, ArrayList<String>> dictAll);

    public List<String> shangYS(String pow , HashMap<String, ArrayList<String>> totalDictAll);

    public int Supp(String pow ,HashMap<String, ArrayList<String>> totalDictAll);

    public int Supp(List<String> pow ,HashMap<String, ArrayList<String>> totalDictAll);
}
