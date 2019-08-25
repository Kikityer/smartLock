package com.ahu.lock.repository;

import com.ahu.lock.domain.BuildingInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @ClassName BuildingInfoRepository
 * @Description TODO
 * @Author Kikityer
 * @Date 2018/9/25 13:17
 * @Version 1.0.0
 **/
public interface BuildingInfoRepository extends JpaRepository<BuildingInfo,Integer> {

    @Override
    List<BuildingInfo> findAll();
}
