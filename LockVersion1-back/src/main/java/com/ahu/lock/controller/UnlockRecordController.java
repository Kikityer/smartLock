package com.ahu.lock.controller;


import com.ahu.lock.domain.UnlockRecord;
import com.ahu.lock.repository.UnlockRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName UnlockRecordController
 * @Description TODO
 * @Author Kikityer
 * @Date 2018/10/11 9:38
 * @Version 1.0.0
 **/
@RestController
@RequestMapping(value = "/unlockRecord")
public class UnlockRecordController {

    private final UnlockRecordRepository unlockRecordRepository;

    @Autowired
    public UnlockRecordController(UnlockRecordRepository unlockRecordRepository){ //构造器注入仓储

        this.unlockRecordRepository = unlockRecordRepository;
    }


    /**
     * 获取开门记录
     * 提供可获取一周内、半个月内、一个月内、三个月内、六个月内的开锁记录查询的五种方式（默认向前台提供一周内数据）
     */
    @RequestMapping(value = "/getRecord",method = RequestMethod.GET)
    public List<UnlockRecord> getRecord(@RequestParam("mark") String mark){
        List<UnlockRecord> arrayList = new ArrayList<>();

        if (mark.equals("七天内开锁记录")){
            getDifferentRecord(7,arrayList);

        }else if(mark.equals("半个月内开锁记录")){
            getDifferentRecord(15,arrayList);

        }else if(mark.equals("一个月内开锁记录")){
            getDifferentRecord(30,arrayList);

        }else if(mark.equals("三个月内开锁记录")){
            getDifferentRecord(90,arrayList);

        }else{
            getDifferentRecord(180,arrayList);
        }
        return arrayList;
    }


    private void getDifferentRecord(int days,List arrayList){

            Date currentDate = new Date();
            Date temp = new Date(currentDate.getTime() - (long)days * 24 * 60 * 60 * 1000);

            List<UnlockRecord> hen = unlockRecordRepository.findByDate(currentDate,temp);

            for (int f = hen.size() ; f > 0  ; f--){
                arrayList.add(hen.get(f - 1));
            }

    }


    /**
     * 单行删除开锁记录的操作
     */
    @RequestMapping(value = "/deleteOne",method = RequestMethod.GET)
    public void deleteOne(@RequestParam("id") Integer id){

        unlockRecordRepository.deleteById(id);
    }


    /**
     * 批量删除操作
     */
    @RequestMapping(value = "/deleteMany",method = RequestMethod.GET)
    public void deleteMany(Integer ids[]){

        for (int i = 0; i < ids.length; i++){
            unlockRecordRepository.deleteById(ids[i]);
        }
    }

}
