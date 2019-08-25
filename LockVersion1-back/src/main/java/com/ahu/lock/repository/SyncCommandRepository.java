package com.ahu.lock.repository;

import com.ahu.lock.domain.SyncCommand;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName SyncCommandRepository
 * @Description TODO
 * @Author Kikityer
 * @Date 2018/10/23 21:57
 * @Version 1.0.0
 **/
public interface SyncCommandRepository extends JpaRepository<SyncCommand,String> {

        SyncCommand findByLockId(String lockId);

}
