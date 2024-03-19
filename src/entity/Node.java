package entity;

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
