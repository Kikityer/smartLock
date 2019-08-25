package com.ahu.lock.repository;


import com.ahu.lock.domain.LockState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * @ClassName LockStateRepository
 * @Description TODO
 * @Author Kikityer
 * @Date 2018/10/9 9:04
 * @Version 1.0.0
 **/
public interface LockStateRepository extends JpaRepository<LockState,Integer> {

    /*@Override
    List<LockState> findAll();*/

    @Query(value = "SELECT * FROM lock_state WHERE state_time BETWEEN :old AND :now ", nativeQuery = true)
    List<LockState> findDifferentDate(@Param("now") Date old,@Param("old") Date now);
}
