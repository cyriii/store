package com.project.system.storemanagement.bean;

public class UserBean {

    /**
     * userId : a34e66238d9c46f58cea72a0e4b07233
     * nickName : 1234
     * sex : null
     * telNumber : null
     * address : null
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NTg4NDQwNzYsInVzZXJOYW1lIjoiMTIzIiwidXNlcklkIjoiYTM0ZTY2MjM4ZDljNDZmNThjZWE3MmEwZTRiMDcyMzMiLCJ1dWlkIjoiYjU4MTNmNDk2NWY1NGY2NzlhM2ExNzQ4NzJjMzE3NzkifQ.Y819aV60nzmOLUoF8RquvaNFyYh7OOEKi77e5-8Qb3E
     */

    private String userId;
    private String nickName;
    private String sex;
    private String telNumber;
    private String address;
    private String token;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
