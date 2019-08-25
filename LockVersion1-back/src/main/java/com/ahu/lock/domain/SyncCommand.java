package com.ahu.lock.domain;

import javax.persistence.*;

/**
 * @ClassName SyncCommand
 * @Description TODO
 * @Author Kikityer
 * @Date 2018/10/23 21:39
 * @Version 1.0.0
 **/
@Entity
@Table(name = "sync_command")
public class SyncCommand {

    @Id  //将lockId作为主键
//    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "lock_id")
    private String lockId;

    @Column(name = "command")
    private String command;

    @Column(name = "info")
    private String info;

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
