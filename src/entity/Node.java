package entity;


/**
 * @function: 对节点信息进行保存
 * @entity: 节点编号num，属性值value
 * @time:20220907
 * @author: tao
 * */

public class Node {

    int id;
    int value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }
}
