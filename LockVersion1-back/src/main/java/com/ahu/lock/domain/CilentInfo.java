package com.ahu.lock.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * @ClassName CilentInfo
 * @Description TODO
 * @Author Kikityer
 * @Date 2018/9/14 15:53
 * @Version 1.0.0
 **/
@Entity
@Table(name = "client_info")
public class CilentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id")
    private Integer id;

    @Column(name = "school_name")
    private String schoolName;

    @Column(name = "purchase_date")
    private String purchaseDate;

    @Column(name = "school_address")
    private String schoolAddress;

    @Column(name = "lock_number")
    private String lockNumber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getSchoolAddress() {
        return schoolAddress;
    }

    public String getLockNumber() {
        return lockNumber;
    }

    public void setLockNumber(String lockNumber) {
        this.lockNumber = lockNumber;
    }

    public void setSchoolAddress(String schoolAddress) {
        this.schoolAddress = schoolAddress;
    }
}
