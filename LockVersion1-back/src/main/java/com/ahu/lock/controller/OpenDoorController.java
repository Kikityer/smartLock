package com.ahu.lock.controller;

import com.ahu.lock.domain.OPenDoor;
import com.ahu.lock.repository.OpenDoorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName OpenDoorController
 * @Description TODO
 * @Author Kikityer
 * @Date 2018/11/22 11:34
 * @Version 1.0.0
 **/
@RestController
@RequestMapping(value = "/openDoor")
public class OpenDoorController {

    private final OpenDoorRepository openDoorRepository;
    @Autowired
    public OpenDoorController(OpenDoorRepository openDoorRepository){
        this.openDoorRepository = openDoorRepository;
    }

    @RequestMapping(value = "/openImmediately",method = RequestMethod.POST)
    public void opendoor(@RequestParam("lockId") String lockId){

        OPenDoor oPenDoor = new OPenDoor();
        oPenDoor.setLockID(lockId);
        oPenDoor.setCommand(lockId+"03"+"ffff");
        openDoorRepository.save(oPenDoor);

    }
}
