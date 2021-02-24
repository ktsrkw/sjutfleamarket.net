package com.wt.pojo;

import java.util.Date;

public class User {
    private int userid;
    private String username;
    private String password;
    private Date birthday;
    private String university;
    private String email;
    private String tele;
    private String oci;
    //性别：男、女、保密
    private String gender;

    public User() {
    }

    public User(int userid, String username, String password, Date birthday, String university, String email, String tele, String oci, String gender) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        this.university = university;
        this.email = email;
        this.tele = tele;
        this.oci = oci;
        this.gender = gender;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTele() {
        return tele;
    }

    public void setTele(String tele) {
        this.tele = tele;
    }

    public String getOci() {
        return oci;
    }

    public void setOci(String oci) {
        this.oci = oci;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "user{" +
                "userid=" + userid +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", birthday=" + birthday +
                ", university='" + university + '\'' +
                ", email='" + email + '\'' +
                ", tele='" + tele + '\'' +
                ", oci='" + oci + '\'' +
                ", gender=" + gender +
                '}';
    }
}
