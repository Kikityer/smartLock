package com.ahu.lock.repository;

import com.ahu.lock.domain.CilentInfo;
import org.jboss.jandex.ClassInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @ClassName CilentInfoRepository
 * @Description TODO
 * @Author Kikityer
 * @Date 2018/9/14 18:48
 * @Version 1.0.0
 **/

public interface CilentInfoRepository extends JpaRepository<CilentInfo,Integer>{

    @Override
    List<CilentInfo> findAll();

//    CilentInfo findById(int id);




}
