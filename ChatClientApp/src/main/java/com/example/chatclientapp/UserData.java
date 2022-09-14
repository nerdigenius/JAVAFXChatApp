package com.example.chatclientapp;

public class UserData {
    String userName;
    NetworkUtil networkUtil;

    public String getUserName() {
        return userName;
    }

    public NetworkUtil getNetworkUtil() {
        return networkUtil;
    }

    public UserData(String userName, NetworkUtil networkUtil) {
        this.userName = userName;
        this.networkUtil = networkUtil;
    }
}
