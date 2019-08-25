package com.ahu.lock.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * @ClassName UnlockRecord
 * @Description TODO
 * @Author Kikityer
 * @Date 2018/10/11 9:29
 * @Version 1.0.0
 **/
@Entity
@Table(name = "unlock_record")
public class UnlockRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id")
    private Integer id;

    @Column(name = "building_name")
    private String buildingName;

    @Column(name = "dorm_number")
    private String dormNumber;

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "student_number")
    private String studentNumber;

    @Column(name = "unlock_measure")
    private String unlockMeasure;

    @Column(name = "unlock_time")
    private Date unlockTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getUnlockMeasure() {
        return unlockMeasure;
    }

    public void setUnlockMeasure(String unlockMeasure) {
        this.unlockMeasure = unlockMeasure;
    }

    public Date getUnlockTime() {
        return unlockTime;
    }

    public void setUnlockTime(Date unlockTime) {
        this.unlockTime = unlockTime;
    }
}
