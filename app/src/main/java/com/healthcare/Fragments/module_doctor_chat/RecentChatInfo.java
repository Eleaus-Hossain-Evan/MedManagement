package com.healthcare.Fragments.module_doctor_chat;



public class RecentChatInfo {
    public String userName;

    public String phone;

    public RecentChatInfo(String userName,String phone){
        this.userName=userName;

        this.phone=phone;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhone() {
        return phone;
    }


}
