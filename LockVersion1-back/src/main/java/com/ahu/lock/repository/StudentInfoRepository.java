package com.ahu.lock.repository;

import com.ahu.lock.domain.StudentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @ClassName StudentInfoRepository
 * @Description TODO
 * @Author Kikityer
 * @Date 2018/9/26 16:13
 * @Version 1.0.0
 **/
public interface StudentInfoRepository extends JpaRepository<StudentInfo,Integer>{


    @Override
    List<StudentInfo> findAll();

    List<StudentInfo> findByLockId(String lockId);
}
