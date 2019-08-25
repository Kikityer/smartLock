package com.ahu.lock.repository;

import com.ahu.lock.domain.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName ManagerRepository
 * @Description TODO
 * @Author Kikityer
 * @Date 2018/9/12 16:45
 * @Version 1.0.0
 **/
public interface ManagerRepository extends JpaRepository<Manager, Integer>{

    Manager findByUsername(String username);

}
