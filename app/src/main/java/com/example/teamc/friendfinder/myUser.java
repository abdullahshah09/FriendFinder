package com.example.teamc.friendfinder;

import java.io.Serializable;

/**
 * Created by shahkhan on 08/04/2018.
 */

public class myUser {

    private int userAge;
    private String userGender;
    private String userId;
    private String userName;
//    private String url_user;

    public myUser() {

    }
    //Default constructor will have/set the default info for the users such as:
    //(auto generated) id name, age, and gender initialised at creation time


    public myUser(int userAge, String userGender, String userId, String userName ) {

        this.userAge = userAge;
        this.userGender = userGender;
        this.userId = userId;
        this.userName = userName;

    }


    //Getters
    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserAge() {
        return userAge;
    }

    public String getUserGender() {
        return userGender;
    }

}
