package staticData;

import entity.BPCliques;
import entity.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class DataPara {

    public static String fileName = "case_iot";
    public static String readFileName = "data/resultData/" + fileName + ".gml";

    public static int maxindex = 0;
    public static int conNO = 1;


    public static int[][] adjMat;
    public static HashSet<String> hs = new HashSet<>();

    public static Map<Integer, Node> nodes = new HashMap<>();

    public static BPCliques bpcAttr;
    public static ArrayList<String> obj = new ArrayList<String>();
    public static ArrayList<String> attr = new ArrayList<String>();
    public static Integer numObj;
    public static HashMap<String, ArrayList<String>> dictAll = new HashMap<>();
    public static HashSet<ArrayList<String>> objResult = new HashSet<>();
    public static BPCliques CL;
    public static ArrayList<ArrayList<String>> objSet = new ArrayList<>();
    public static ArrayList<ArrayList<String>> itemSet = new ArrayList<>();

    public static HashSet<String> bpcAllCLHashSet = new HashSet<String>();
}
