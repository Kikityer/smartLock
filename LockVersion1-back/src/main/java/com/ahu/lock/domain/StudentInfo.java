package com.ahu.lock.domain;

import javax.persistence.*;

/**
 * @ClassName StudentInfo
 * @Description TODO
 * @Author Kikityer
 * @Date 2018/9/26 16:06
 * @Version 1.0.0
 **/
@Entity
@Table(name = "student_info")
public class StudentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;            //id号

    @Column(name = "building_name")
    private String buildingName;   //楼宇名称

    @Column(name = "dorm_number")
    private String dormNumber;     //寝室号

    @Column(name = "lock_id")
    private String lockId;         //锁的id

    @Column(name = "user_number")
    private String userNumber;     //学生名字代号（01 02 03 04 ）

    @Column(name = "student_name")
    private String studentName;    //学生名字

    @Column(name = "student_number")
    private String studentNumber;  //学号

    @Column(name = "student_card_id")
    private String studentCardId;  //学生卡id

    @Column(name = "identification_card_id")
    private String identificationCardId; //学生身份证id

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getDormNumber() {
        return dormNumber;
    }

    public void setDormNumber(String dormNumber) {
        this.dormNumber = dormNumber;
    }

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getStudentCardId() {
        return studentCardId;
    }

    public void setStudentCardId(String studentCardId) {
        this.studentCardId = studentCardId;
    }

    public String getIdentificationCardId() {
        return identificationCardId;
    }

    public void setIdentificationCardId(String identificationCardId) {
        this.identificationCardId = identificationCardId;
    }
}
