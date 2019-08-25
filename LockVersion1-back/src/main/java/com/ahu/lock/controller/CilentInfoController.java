package com.ahu.lock.controller;

import com.ahu.lock.domain.CilentInfo;
import com.ahu.lock.repository.CilentInfoRepository;
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
 * @ClassName CilentInfoController
 * @Description TODO
 * @Author Kikityer
 * @Date 2018/9/17 14:10
 * @Version 1.0.0
 **/
@RestController
@RequestMapping(value = "/cilentInfo")
public class CilentInfoController {

    private final CilentInfoRepository cilentInfoRepository;

    @Autowired
    public CilentInfoController(CilentInfoRepository cilentInfoRepository){

        this.cilentInfoRepository = cilentInfoRepository;
    }

    /**
     * 获取购买方（各个高校）的具体信息
     * @return
     */
    @RequestMapping(value = "/getTable",method = RequestMethod.GET)
    public List<CilentInfo> getTable(){

        return cilentInfoRepository.findAll();
    }


    /**
     * 创建一个新的校区的信息
     */
    @Transactional
    @RequestMapping(value = "/newSchoolInfo",method = RequestMethod.POST)
    public String creatInfo(@RequestParam("name") String name,
                            @RequestParam("date") String date,
                            @RequestParam("add") String add,
                            @RequestParam("num") String num){

        CilentInfo cilentInfo = new CilentInfo();
        cilentInfo.setSchoolName(name);
        cilentInfo.setPurchaseDate(date);
        cilentInfo.setSchoolAddress(add);
        cilentInfo.setLockNumber(num);
        cilentInfoRepository.save(cilentInfo);
        return "1";

    }


    /**
     * 单行删除客户具体信息的操作
     */
    @Transactional
    @RequestMapping(value = "/deleteOne",method = RequestMethod.GET)
    public void deleteOne(@RequestParam("id") Integer id){

        cilentInfoRepository.deleteById(id);
    }


    /**
     * 批量删除客户的具体信息的操作
     */
    @Transactional
    @RequestMapping(value = "/deleteMany",method = RequestMethod.GET)
    public void deleteMany(Integer ids[]) {

        for (int i = 0; i < ids.length; i++) {
            cilentInfoRepository.deleteById(ids[i]);
        }
    }


    /**
     * 高校信息编辑操作
     */
    @Transactional
    @RequestMapping(value = "/editSchool",method = RequestMethod.POST)
    public void editSchool(@RequestParam("id") Integer id,
                           @RequestParam("name") String name,
                           @RequestParam("date") String date,
                           @RequestParam("add") String add,
                           @RequestParam("num") String num){

//        CilentInfo cilentInfo=cilentInfoRepository.findById(id).orElse(null);

        CilentInfo cilentInfo=cilentInfoRepository.findById(id).orElse(null);

        cilentInfo.setSchoolName(name);
        cilentInfo.setPurchaseDate(date);
        cilentInfo.setSchoolAddress(add);
        cilentInfo.setLockNumber(num);
        cilentInfoRepository.save(cilentInfo);
    }


    /**
     * 本方法用于将所有的客户信息从数据库中拿出，之后对信息进行组装传递给前台进行echart图标展示
     */
    @RequestMapping(value = "/getEchartData",method = RequestMethod.GET)
    public HashMap getEchartData(){

        List<CilentInfo> cilentInfo = cilentInfoRepository.findAll(); //取出所有的用户信息放进集合中
        ArrayList<String> listProvince = new ArrayList<>();
        ArrayList<String> listCity = new ArrayList<>();
        ArrayList<String> listDate = new ArrayList<>();
        ArrayList<Integer> listLockNum = new ArrayList<>();//用于装载锁的数量的字符串数据
        ArrayList<String> listSchool = new ArrayList<>();//用于装载学校名称的列表
        for (int i=0; i<cilentInfo.size(); i++){

            String schoolName = cilentInfo.get(i).getSchoolName();
            String lockNum = cilentInfo.get(i).getLockNumber();
            String date = cilentInfo.get(i).getPurchaseDate();
            String address = cilentInfo.get(i).getSchoolAddress();
            int a = address.indexOf('省');  //判断位置字符串的是否含有省字字符
            if (a != -1){
                String str1 = address.substring(0,a+1);  //有省字字符的时候，将省字字符之前的字符串截断赋值给str1，并丢进集合listProvince
                listProvince.add(str1);
            }else {
                int b = address.indexOf('市');  //没有的省字的时候，直接将市字之前的字符串截断赋值给str2，并丢进集合listCity
                String str2 = address.substring(0,b+1);
                listCity.add(str2);
            }

            String years = date.substring(0,4);
            listDate.add(years);  //取出购买日期的年份丢进集合listDate

            listSchool.add(schoolName);


            /**
             * 实现对锁数量的相加操作
             */
            String str=lockNum.trim();
            String str2="";
            if(str != null && !"".equals(str)){
                for(int j=0;j<str.length();j++){
                    if(str.charAt(j)>=48 && str.charAt(j)<=57 || str.charAt(j)==47){
                        str2+=str.charAt(j);
                    }
                }

            }
//            System.out.println(str2);
            int num = 0;
            // 循环遍历每个字符，判断是否是字符 a ，如果是，累加次数
            for (int k=0;k<str2.length();k++)
            {
                // 获取每个字符，判断是否是字符a
                if (str2.charAt(k)=='/') {
                    // 累加统计次数
                    num++;
                }
            }
//            System.out.println("字符/出现的次数：" + num);
            if (num==1){
                int n =str2.indexOf('/');
                String one = str2.substring(0,n);
                int oneNum = Integer.parseInt(one);
                listLockNum.add(oneNum);
            }else if(num==2){
                int n = str2.indexOf('/');
                String partOne = str2.substring(0,n);
                int partOneNum = Integer.parseInt(partOne);

                int m = str2.lastIndexOf('/');
                String partTwo = str2.substring(n+1,m);
                int partTwoNum = Integer.parseInt(partTwo);
                int twoNum = partOneNum + partTwoNum;
//                String two = String.valueOf(twoNum);
                listLockNum.add(twoNum);
            }else if(num==3){
                int n = str2.indexOf('/');
                String partOne = str2.substring(0,n);
                int partOneNum = Integer.parseInt(partOne);

                int m = str2.lastIndexOf('/');
                String other = str2.substring(n+1,m+1);
                int k = other.indexOf('/');
                String partTwo = other.substring(0,k);
                int partTwoNum = Integer.parseInt(partTwo);

                int p =other.lastIndexOf('/');
                String partThree = other.substring(k+1,p);
                int partThreeNum = Integer.parseInt(partThree);
                int threeNum = partOneNum+ partTwoNum +partThreeNum;
//                String three = String.valueOf(threeNum);
                listLockNum.add(threeNum);
            }


        }

        HashMap<String, Integer> address = new HashMap<>();
        for (String c : listProvince ){
            if (address.containsKey(c)){
               address.put(c,address.get(c)+1);
            }
            else {
               address.put(c,1);
            }
        }
        for (String c : listCity ){
            if (address.containsKey(c)){
                address.put(c, address.get(c)+1);
            }
            else {
               address.put(c,1);
            }
        }



        for (String d : listDate){
            if (address.containsKey(d)){
               address.put(d, address.get(d)+1);
            }
            else {
               address.put(d,1);
            }

        }




        for (int p = 0; p<listSchool.size(); p++){
            address.put(listSchool.get(p),listLockNum.get(p));
        }

        return address;

    }


    /**
     * 实现远程提供搜索建议
     */
    @RequestMapping(value = "/searchSuggestion",method = RequestMethod.GET)
    public ArrayList search(){
        ArrayList<HashMap<String,String>> hashMaps = new ArrayList<>();
        List<CilentInfo> cilentInfos = cilentInfoRepository.findAll();  //先拿到所有的高校信息

        for (CilentInfo cilentInfo : cilentInfos){
            HashMap<String,String> hashMap = new HashMap<>();

            hashMap.put("value",cilentInfo.getSchoolName());
            hashMaps.add(hashMap);
        }

        return hashMaps;  //把key值是"value"字段，value值是每个大学的名称的hashmap返回到前台
    }

}
