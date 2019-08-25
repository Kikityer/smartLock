package com.ahu.lock.controller;

import com.ahu.lock.domain.BuildingInfo;
import com.ahu.lock.repository.BuildingInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.NamingEnumeration;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @ClassName BuildingInfoController
 * @Description TODO
 * @Author Kikityer
 * @Date 2018/9/25 13:19
 * @Version 1.0.0
 **/

@RestController
@RequestMapping(value = "/buildingInfo")
public class BuildingInfoController {

    private final BuildingInfoRepository buildingInfoRepository;
    @Autowired
    public BuildingInfoController(BuildingInfoRepository buildingInfoRepository){

        this.buildingInfoRepository = buildingInfoRepository;
    }


    /**
     * 返回楼宇的所有相关信息到前台以表格的形式展示出来
     * @return
     */
    @RequestMapping(value = "/getBuildingInfo")
    public List<BuildingInfo> get(){

        return buildingInfoRepository.findAll();

    }


    /**
     * 实现了楼宇信息的修改操作
     * @param id
     * @param buildingName
     * @param peopleLive
     * @param managerName
     * @param managerPhone
     */
    @Transactional
    @RequestMapping(value = "/submitBuildingInfo")
    public void changeBuildingInfo(@RequestParam("id") Integer id,
                                   @RequestParam("buildingName") String buildingName,
                                   @RequestParam("peopleLive") String peopleLive,
                                   @RequestParam("managerName") String managerName,
                                   @RequestParam("managerPhone") String managerPhone){

        BuildingInfo buildingInfo = buildingInfoRepository.findById(id).orElse(null);

        buildingInfo.setBuildingName(buildingName);
        buildingInfo.setPeopleLive(peopleLive);
        buildingInfo.setManagerName(managerName);
        buildingInfo.setManagerPhone(managerPhone);
        buildingInfoRepository.save(buildingInfo);



    }

    /**
     * 新增一个楼宇信息
     * @param buildingName
     * @param peopleLive
     * @param managerName
     * @param managerPhone
     */
    @Transactional
    @RequestMapping(value = "/createBuildingInfo")
    public String createBuilding(@RequestParam("buildingName") String buildingName,
                               @RequestParam("peopleLive") String peopleLive,
                               @RequestParam("managerName") String managerName,
                               @RequestParam("managerPhone") String managerPhone){

        BuildingInfo buildingInfo = new BuildingInfo();
        buildingInfo.setBuildingName(buildingName);
        buildingInfo.setPeopleLive(peopleLive);
        buildingInfo.setManagerName(managerName);
        buildingInfo.setManagerPhone(managerPhone);
        buildingInfoRepository.save(buildingInfo);

        return "success";

    }


    /**
     * 单行删除一个楼宇信息
     * @param id
     */
    @Transactional
    @RequestMapping(value = "/deleteOne")
    public void deleteOne(@RequestParam("id") Integer id){

        buildingInfoRepository.deleteById(id);

    }


    /**
     * 批量删除客户的具体信息的操作
     */
    @Transactional
    @RequestMapping(value = "/deleteMany")
    public void deleteMany(Integer ids[]) {

        for (int i = 0; i < ids.length; i++) {
            buildingInfoRepository.deleteById(ids[i]);
        }
    }
}
