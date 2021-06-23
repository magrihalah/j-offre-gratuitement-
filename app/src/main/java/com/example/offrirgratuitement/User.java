package com.example.offrirgratuitement;

public class User {
    public String fullName , phoneNumber , email ;

    public User (){

    }
    public User (String fullName, String phoneNumber , String email){
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }
}
