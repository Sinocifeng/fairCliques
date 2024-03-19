package tools.impl;

import entity.ConceptPair;
import staticData.DataPara;
import tools.InteDegTools;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InteDegToolsImpl implements InteDegTools {


    @Override
    public double calStab(ConceptPair concept, int[][] adjMat){

        DataPara.dictAll = buildDict(adjMat);

        List<Integer> A = new ArrayList<>(concept.getL());
        List<Integer> B = new ArrayList<>(concept.getR());


        ArrayList<ArrayList<Integer>> MINGEN = new ArrayList<>();
        double stab = 0;
        double aSize = A.size();

        for (Integer a : new ArrayList<>(A)) {
            ArrayList<Integer> alist = new ArrayList<>(Arrays.asList(a));
            shangYS(alist, DataPara.dictAll);
            if (Supp(alist, DataPara.dictAll) == B.size()) {
                MINGEN.add(alist);
            }
        }

        if (!MINGEN.isEmpty()) {
            stab = 1 - 1 / Math.pow(2, MINGEN.size());
        }

        A.removeAll(MINGEN.stream().flatMap(List::stream).toList());
        ArrayList<Integer> MaxNonGen = BUILD_MAX_NONGEN(A, B.size());
        A.removeAll(MaxNonGen);

        ArrayList<Integer> α = new ArrayList<>(A);
        ArrayList<Integer> β = new ArrayList<>(MaxNonGen);
        ArrayList<Integer> WUCHA = new ArrayList<>();

        for (Integer a : α) {
            WUCHA.add(a);
            stab += HANDLE_CURRENT_ELEMENT(MINGEN, new ArrayList<>(Arrays.asList(a)), α, β, WUCHA, B.size()) / Math.pow(2, aSize);
        }
        return stab;
    }


    private ArrayList<Integer> BUILD_MAX_NONGEN(List<Integer> A, int size) {

        ArrayList<Integer> MaxNonGen1 = new ArrayList<Integer>();
        for (int a : A) {
            MaxNonGen1.add(a);
            if (Supp(MaxNonGen1, DataPara.dictAll) == size){
                MaxNonGen1.remove(Integer.valueOf(a));
            }else {
                //System.out.println(a +"并入集合MaxNonGen1");
            }
        }
        return MaxNonGen1;
    }

    private double HANDLE_CURRENT_ELEMENT(ArrayList<ArrayList<Integer>> MINGEN, ArrayList<Integer> a, ArrayList<Integer> α, ArrayList<Integer> β, ArrayList<Integer> WUCHA, int size) {


        double Nbgen = 0;
        while( !α.isEmpty() ) {
            MINGEN = GET_MINGEN(MINGEN, a, α, β, size, WUCHA);

            Nbgen = Nbgen + COMPUTE_GEN( a, α, β, size, WUCHA );

            ArrayList<ArrayList<Integer>> NonGenSet = RETURN_NonGenSet(a, α, β, size, WUCHA );

            α = GET_α(NonGenSet, size);
            β = GET_β(NonGenSet, size);
            a = GET_a(a, NonGenSet, size);
        }
        return Nbgen;
    }

    private ArrayList<ArrayList<Integer>> GET_MINGEN(ArrayList<ArrayList<Integer>> MINGEN, ArrayList<Integer> a, ArrayList<Integer> α, ArrayList<Integer> β, int size, ArrayList<Integer> WUCHA) {
        ArrayList<Integer> TempSetβ = new ArrayList<>(β);
        TempSetβ.addAll(α);

        for (Integer aValue : a) {
            TempSetβ.removeIf(b -> b == aValue);
        }


        TempSetβ.removeAll(WUCHA);
        for (int b : TempSetβ) {
            ArrayList<Integer> abs = new ArrayList<>(a);
            abs.add(b);
            if (Supp(abs, DataPara.dictAll) == size && abs.size()!=0) {
                MINGEN.add(abs);
            }
        }
        return MINGEN;
    }

    private double COMPUTE_GEN(ArrayList<Integer> a, ArrayList<Integer> α, ArrayList<Integer> β, int size, ArrayList<Integer> WUCHA) {
        double Nbgen = 0;
        ArrayList<ArrayList<Integer>> NonGenSet = new ArrayList<>();
        ArrayList<ArrayList<Integer>> MinGenSet = new ArrayList<>();
        ArrayList<Integer> TempSetβ = new ArrayList<>(β);
        TempSetβ.addAll(α);

        for (Integer aValue : a) {
            TempSetβ.removeIf(b -> b == aValue);
        }

        TempSetβ.removeAll(WUCHA);

        for (Integer b : TempSetβ) {
            ArrayList<Integer> ab = new ArrayList<>(a);
            ab.add(b);

            if (Supp(ab, DataPara.dictAll) == size) {
                MinGenSet.add(ab);
            }else {
                NonGenSet.add(ab);
            }
        }

        for(int i = 1; i <= MinGenSet.size(); i++) {
            Nbgen += Math.pow(2, NonGenSet.size() + MinGenSet.size() - i);
        }
        return Nbgen;
    }

    private ArrayList<ArrayList<Integer>> RETURN_NonGenSet(ArrayList<Integer> a, ArrayList<Integer> α, ArrayList<Integer> β, int size, ArrayList<Integer> WUCHA) {
        ArrayList<ArrayList<Integer>> NonGenSet = new ArrayList<>();
        ArrayList<Integer> TempSetβ = new ArrayList<>(β);
        TempSetβ.addAll(α);

        for (Integer aValue : a) {
            TempSetβ.removeIf(b -> b == aValue);
        }
        TempSetβ.removeAll(WUCHA);

        for (int b : TempSetβ) {
            ArrayList<Integer> ab = new ArrayList<>(a);
            ab.add(b);
            if (Supp(ab, DataPara.dictAll) != size) {
                NonGenSet.add(ab);
            }
        }
        return NonGenSet;
    }

    private ArrayList<Integer> GET_a(ArrayList<Integer> a, ArrayList<ArrayList<Integer>> NonGenSet, int size) {
        ArrayList<Integer> α = new ArrayList<Integer>();
        ArrayList<Integer> β = new ArrayList<Integer>();

        for (ArrayList<Integer> list : NonGenSet) {
            for(int i = 0; i < list.size(); i++) {
                if( !β.contains(list.get(i)) ) {
                    β.add(list.get(i));
                }
            }
            if(Supp(β, DataPara.dictAll) == size) {
                for(int i=0; i< list.size(); i++) {
                    if( !α.contains(list.get(i)) ) {
                        α.add(list.get(i));
                        β.remove(list.get(i));
                    }
                }
                a = list ;
            }
        }
        return a;
    }

    private ArrayList<Integer> GET_β(ArrayList<ArrayList<Integer>> NonGenSet, int size) {
        ArrayList<Integer> α = new ArrayList<Integer>();
        ArrayList<Integer> β = new ArrayList<Integer>();
        for (ArrayList<Integer> list : NonGenSet) {
            for(int i = 0; i < list.size(); i++) {
                if( !β.contains(list.get(i)) ) {
                    β.add(list.get(i));
                }
            }
            if(Supp(β, DataPara.dictAll) == size) {
                for(int i=0; i< list.size(); i++) {
                    if( !α.contains(list.get(i)) ) {
                        α.add(list.get(i));
                        β.remove(list.get(i));
                    }
                }
            }
        }
        return β;
    }

    private ArrayList<Integer> GET_α(ArrayList<ArrayList<Integer>> NonGenSet, int size) {
        ArrayList<Integer> α = new ArrayList<Integer>();
        ArrayList<Integer> β = new ArrayList<Integer>();
        for (ArrayList<Integer> list : NonGenSet) {

            for(int i = 0; i < list.size(); i++) {
                if( !β.contains(list.get(i))) {
                    β.add(list.get(i));
                }
            }
            if(Supp(β, DataPara.dictAll) == size) {
                for(int i=0; i< list.size(); i++) {
                    if( !α.contains(list.get(i)) ) {
                        α.add(list.get(i) );
                        β.remove(list.get(i));
                    }
                }
            }
        }
        return α;
    }

    private int Supp(ArrayList<Integer> pow, HashMap<String, ArrayList<String>> dictAll) {
        List<Integer> AttList = new ArrayList<>();

        if (pow == null || pow.isEmpty()) {
            return 0;
        }

        for (Integer obt : pow) {
            ArrayList<Integer> attr = dictAll.getOrDefault(String.valueOf(obt), new ArrayList<>())
                    .stream()
                    .map(Integer::parseInt)
                    .collect(Collectors.toCollection(ArrayList::new));

            if (AttList.isEmpty()) {
                AttList.addAll(attr);
            } else {
                AttList.retainAll(attr);
            }
        }

        return AttList.size();
    }

    private List<Integer> shangYS(ArrayList<Integer> pow, HashMap<String, ArrayList<String>> dictAll) {
        List<Integer> AttList = new ArrayList<>();

        if (pow == null) {
            return AttList;
        }

        for (Integer obt : pow) {
            ArrayList<Integer> attr = dictAll.getOrDefault(String.valueOf(obt), new ArrayList<>())
                    .stream()
                    .map(Integer::parseInt)
                    .collect(Collectors.toCollection(ArrayList::new));

            if (AttList.isEmpty()) {
                AttList.addAll(attr);
            } else {
                AttList.retainAll(attr);
            }
        }

        return AttList;
    }


    @Override
    public HashMap<String, ArrayList<String>> buildDict(int[][] adjMat) {
        int nodenum = adjMat.length;
        HashMap<String, ArrayList<String>> dictTem = new HashMap<>();

        IntStream.range(0, nodenum)
                .forEach(i -> {
                    ArrayList<String> relatedNodes = IntStream.range(0, nodenum)
                            .filter(j -> adjMat[i][j] == 1)
                            .mapToObj(j -> String.valueOf(j + 1))
                            .collect(Collectors.toCollection(ArrayList::new));
                    dictTem.put(String.valueOf(i + 1), relatedNodes);
                });

        return dictTem;
    }



}
