package com.ahu.lock.domain;

import javax.persistence.*;

/**
 * @ClassName BuildingInfo
 * @Description TODO
 * @Author Kikityer
 * @Date 2018/9/25 13:10
 * @Version 1.0.0
 **/
@Entity
@Table(name = "building_info")
public class BuildingInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "building_name")
    private String buildingName;

    @Column(name = "for_whom")
    private String peopleLive;

    @Column(name = "manager_name")
    private String managerName;

    @Column(name = "manager_phone")
    private String managerPhone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getPeopleLive() {
        return peopleLive;
    }

    public void setPeopleLive(String peopleLive) {
        this.peopleLive = peopleLive;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }
}
