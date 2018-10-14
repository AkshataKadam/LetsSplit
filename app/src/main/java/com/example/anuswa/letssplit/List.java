package com.example.anuswa.letssplit;

public class List {
    public List()
    {}
    public List(String person,int cost) {
        this.person = person;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    String person ;
    int cost;
}
