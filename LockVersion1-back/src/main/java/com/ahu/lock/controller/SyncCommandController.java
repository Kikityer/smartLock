package com.ahu.lock.controller;

import com.ahu.lock.repository.SyncCommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName SyncCommandController
 * @Description TODO
 * @Author Kikityer
 * @Date 2018/10/23 22:05
 * @Version 1.0.0
 **/
@RestController
@RequestMapping(value = "/deletSync")
public class SyncCommandController {

    private final SyncCommandRepository syncCommandRepository;
    @Autowired
    public SyncCommandController(SyncCommandRepository syncCommandRepository){
        this.syncCommandRepository = syncCommandRepository;
    }


}
