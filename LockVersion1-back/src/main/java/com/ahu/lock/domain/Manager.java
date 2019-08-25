package com.ahu.lock.domain;

import javax.persistence.*;


/**
 * @ClassName Manager
 * @Description TODO
 * @Author Kikityer
 * @Date 2018/9/12 16:38
 * @Version 1.0.0
 **/
@Entity
@Table(name = "manager_info")
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id")
    private Integer id;

    @Column(name = "manager_name")
    private String username;

    @Column(name = "school_name")
    private String correlation;

    @Column(name = "manager_password")
    private String password;

    @Column(name = "used_condition")
    private String condition;

    @Column(name = "manager_identity")
    private String identity;

    public String getCorrelation() {
        return correlation;
    }

    public void setCorrelation(String correlation) {
        this.correlation = correlation;
    }

    public String getCondition() {

        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
