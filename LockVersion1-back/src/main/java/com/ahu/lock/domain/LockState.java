package com.ahu.lock.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * @ClassName LockState
 * @Description TODO
 * @Author Kikityer
 * @Date 2018/10/9 8:56
 * @Version 1.0.0
 **/
@Entity
@Table(name = "lock_state")
public class LockState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id")
    private Integer id;

    @Column(name = "building_name")
    private String buildingName;

    @Column(name = "dorm_number")
    private String dormNumber;

    @Column(name = "lock_id")
    private String lockId;

    @Column(name = "lock_battery")
    private Integer battery;

    @Column(name = "lock_signal")
    private String signal;

    @Column(name = "state_time")
    private Date currentTime;

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

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    public Integer getBattery() {
        return battery;
    }

    public void setBattery(Integer battery) {
        this.battery = battery;
    }

    public String getSignal() {
        return signal;
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }
}
