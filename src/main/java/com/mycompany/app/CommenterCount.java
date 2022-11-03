package com.mycompany.app;

public class CommenterCount {
    // commenter's id
    private String id;

    // number of comments per story by commenter
    private int Count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    @Override
    public String toString() {
        return "CommenterCount{" +
                "id='" + id + '\'' +
                ", Count=" + Count +
                '}';
    }
}
