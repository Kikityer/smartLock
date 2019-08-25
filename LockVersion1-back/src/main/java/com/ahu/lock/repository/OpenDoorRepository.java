package com.ahu.lock.repository;

import com.ahu.lock.domain.OPenDoor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName OpenDoorRepository
 * @Description TODO
 * @Author Kikityer
 * @Date 2018/11/22 11:26
 * @Version 1.0.0
 **/
public interface OpenDoorRepository extends JpaRepository<OPenDoor,Integer>{

}
