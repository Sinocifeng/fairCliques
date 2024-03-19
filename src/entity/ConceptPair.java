package entity;

import java.util.List;

//每个ConceptPair为一个概念，继承自Pair，Pair中两个List分别表示外延和内涵。
public class ConceptPair extends Pair<List, List> {
    private double stability;               //概念的稳定度
    private double separate;                //概念的散度
    public ConceptPair(List list, List list2) {
        super(list, list2);
    }

    public double getStability() {
        return stability;
    }

    public void setStability(double stability) {
        this.stability = stability;
    }

    public double getSeparate() {
        return separate;
    }

    public void setSeparate(double separate) {
        this.separate = separate;
    }
}