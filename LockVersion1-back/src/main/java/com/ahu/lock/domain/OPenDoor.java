package com.ahu.lock.domain;

import javax.persistence.*;

/**
 * @ClassName OPenDoor
 * @Description TODO
 * @Author Kikityer
 * @Date 2018/11/22 11:20
 * @Version 1.0.0
 **/

@Entity
@Table(name = "open_door_command")
public class OPenDoor {

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "lock_id")
    private String lockID;

    @Column(name = "open_command")
    private String command;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLockID() {
        return lockID;
    }

    public void setLockID(String lockID) {
        this.lockID = lockID;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
