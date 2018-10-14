package com.example.anuswa.letssplit;

public class User {


    private String name,pass,emailid,mobileno;
    public User(){

    }
    public User(String mobileno, String name,String emailid,String pass) {
        this.name=name;
        this.pass=pass;
        this.emailid=emailid;
        this.mobileno=mobileno;
    }


        public String getName () {
            return name;
        }

        public void setName (String name){
            this.name = name;
        }

        public String getPass () {
            return pass;
        }
        public void setPass (String pass){
            this.pass = pass;
        }


        public String getMobileno () {
            return mobileno;
        }

        public void setMobileno ( String mobileno){
            this.mobileno = mobileno;
        }


        public String getEmailid () {
            return emailid;
        }

        public void setEmailid (String emailid){
            this.emailid = emailid;
        }
};
