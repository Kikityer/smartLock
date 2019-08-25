package com.ahu.lock.controller;

import com.ahu.lock.domain.Manager;
import com.ahu.lock.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName ManagerController
 * @Description TODO
 * @Author Kikityer
 * @Date 2018/9/12 20:34
 * @Version 1.0.0
 **/
@RestController
@RequestMapping(value = "/manage")
public class ManagerController{

    private final ManagerRepository managerRepository;
    @Autowired
    public ManagerController(ManagerRepository managerRepository){
        this.managerRepository = managerRepository;
    }

    /**
     * 登陆验证
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "/login", method= RequestMethod.POST)
    public String[] checkUser(@RequestParam("username") String username,
                              @RequestParam("password") String password){
        String[] a = new String[3];

        if (managerRepository.findByUsername(username) == null){
          a[0] = "name error";
        }
        else {
            Manager manager = managerRepository.findByUsername(username);
            if (password.equals(manager.getPassword())){
                a[0] = username;
                a[1] = manager.getIdentity();
                a[2] = manager.getPassword();
            }
        }

        return a;
    }

    /**
     * 拿到管理员的信息在前台页面中以表格的形式显示
     * @return
     */
    @RequestMapping(value = "/getManagers")
    public List<Manager> getManagers(){

         return managerRepository.findAll();

    }


    /**
     * 单行删除管理员的信息
     */
    @Transactional
    @RequestMapping(value = "/deleteOne")
    public void deleteOne(@RequestParam("id") Integer id){

        managerRepository.deleteById(id);
    }


    /**
     * 新增一个管理员账号
     * @param name
     * @param school
     * @param pass
     * @param condi
     * @param ident
     * @return
     */
    @Transactional
    @RequestMapping(value = "/subNewManagers", method= RequestMethod.POST)
    public String subManagers(@RequestParam("name") String name,
                            @RequestParam("school") String school,
                            @RequestParam("pass") String pass,
                            @RequestParam("condi") String condi,
                            @RequestParam("identi") String ident){

        Manager manager = new Manager();
        manager.setUsername(name);
        manager.setCorrelation(school);
        manager.setPassword(pass);
        manager.setCondition(condi);
        manager.setIdentity(ident);
        managerRepository.save(manager);
        return "success";
    }




    /**
     * 批量删除管理员账号信息
     * @param ids
     */
    @Transactional
    @RequestMapping("/deleteMany")
    public void deleteMany(Integer ids[]){

        for (int i = 0; i < ids.length; i++) {
            managerRepository.deleteById(ids[i]);
        }
    }


    /**
     * 更改当前账户的信息（账户名和密码）
     * @param oldname
     * @param oldpass
     * @param newname
     * @param newpass
     */
    @Transactional
    @RequestMapping(value = "/changeManager" , method = RequestMethod.POST)
    public String changeManager(@RequestParam("oldname") String oldname,
                               @RequestParam("oldpass") String oldpass,
                               @RequestParam("newname") String newname,
                               @RequestParam("newpass") String newpass) {

        Manager manager = managerRepository.findByUsername(oldname); //通过前端传入的旧的用户名找到一个对象

        if (!manager.getPassword().equals(oldpass)) {

                return "旧密输入有误";
        }
        else {
                manager.setUsername(newname);
                manager.setPassword(newpass);
                managerRepository.save(manager);
            }
        return "success";
    }


    /**
     * 编辑账户信息
     * @param id
     * @param username
     * @param correlation
     * @param condition
     */
    @Transactional
    @RequestMapping(value = "/managerOne")
    public void changeInfo(@RequestParam("id") Integer id,
                           @RequestParam("username") String username,
                           @RequestParam("correlation") String correlation,
                           @RequestParam("condition") String condition){

          Manager manager = managerRepository.findById(id).orElse(null);
          manager.setUsername(username);
          manager.setCorrelation(correlation);
          manager.setCondition(condition);
          managerRepository.save(manager);

    }


    /**
     * 提供远程搜索建议
     */
    @RequestMapping(value = "/searchSuggestion")
    public ArrayList search(){
        ArrayList<HashMap<String,String>> hashMapArrayList = new ArrayList<>();

        List<Manager> list = managerRepository.findAll();

        for (Manager manager : list){
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("value", manager.getUsername());
            hashMapArrayList.add(hashMap);
        }
        return hashMapArrayList;
    }

}
