package com.example.anuswa.letssplit;

public class AmountInfo
{
    String personname;
    String pricee;


    public AmountInfo() {
    }

    public AmountInfo(String personname, String pricee) {
        this.personname = personname;
        this.pricee = pricee;
    }

    public String getPersonname() {
        return personname;
    }

    public void setPersonname(String personname) {
        this.personname = personname;
    }

    public String getPricee() {
        return pricee;
    }

    public void setPricee(String pricee) {
        this.pricee = pricee;
    }
}
