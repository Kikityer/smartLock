package com.ahu.lock.repository;

import com.ahu.lock.domain.UnlockRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * @ClassName UnlockRecordRepository
 * @Description TODO
 * @Author Kikityer
 * @Date 2018/10/11 9:37
 * @Version 1.0.0
 **/
public interface UnlockRecordRepository extends JpaRepository<UnlockRecord,Integer> {


        @Query(value = "SELECT * FROM unlock_record WHERE unlock_time BETWEEN :oldTime AND  :nowTime",nativeQuery = true)
        List<UnlockRecord> findByDate(@Param("nowTime") Date nowTime, @Param("oldTime")Date oldTime);
}
