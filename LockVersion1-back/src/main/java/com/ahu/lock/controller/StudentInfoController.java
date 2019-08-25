package com.ahu.lock.controller;

import com.ahu.lock.domain.BuildingInfo;
import com.ahu.lock.domain.StudentInfo;
import com.ahu.lock.domain.SyncCommand;
import com.ahu.lock.exception.MyException;
import com.ahu.lock.repository.BuildingInfoRepository;
import com.ahu.lock.repository.StudentInfoRepository;
import com.ahu.lock.repository.SyncCommandRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @ClassName StudentInfoController
 * @Description TODO
 * @Author Kikityer
 * @Date 2018/9/26 16:15
 * @Version 1.0.0
 **/
@RestController
@RequestMapping(value = "/studentInfo")
public class StudentInfoController {

    private final StudentInfoRepository studentInfoRepository;
    private final BuildingInfoRepository buildingInfoRepository;
    private final SyncCommandRepository syncCommandRepository;
    @Autowired
    public StudentInfoController(StudentInfoRepository studentInfoRepository, BuildingInfoRepository buildingInfoRepository,SyncCommandRepository syncCommandRepository){

        this.studentInfoRepository = studentInfoRepository;

        this.buildingInfoRepository = buildingInfoRepository;

        this.syncCommandRepository = syncCommandRepository;
    }



    /**
     * 返回所有的学生相关信息到前台展示
     */
    @RequestMapping(value = "/getAllInfo")
    public List<StudentInfo> getStudentInfo(){

        return studentInfoRepository.findAll();

    }



    /**
     * 返回楼宇信息在学生信息中供其搜索（远程搜素时需要返回的信息）
     */
    @RequestMapping(value = "/returnBuildingInfo")
    public ArrayList returnBuilding(){
        ArrayList<HashMap<String,String>> hashMapList = new ArrayList<>();

        ArrayList<BuildingInfo> buildingInfo = (ArrayList<BuildingInfo>) buildingInfoRepository.findAll();
        for (BuildingInfo buildingInfo1:buildingInfo){
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("value",buildingInfo1.getBuildingName());
            hashMapList.add(hashMap);
        }

        return hashMapList;

    }



    /**
     * 新增一个学生信息(同更新的一个学生的效果一样，将信息保存在student_info表中，同时同步同一个寝室的所有学生的信息到sync_command表中)
     * @param buildingName
     * @param dormNumber
     * @param lockId
     * @param studentName
     * @param studentNumber
     * @return
     */
    @Transactional
    @RequestMapping(value = "/newStudentInfo", method = RequestMethod.POST)
    public String newStudentInfo(@RequestParam("buildingName") String buildingName,
                                 @RequestParam("dormNumber") String dormNumber,
                                 @RequestParam("lockId") String lockId,
                                 @RequestParam("userNumber") String userNumber,
                                 @RequestParam("studentName") String studentName,
                                 @RequestParam("studentNumber") String studentNumber,
                                 @RequestParam("studentCardId") String studentCardId,
                                 @RequestParam("identificationCardId") String identificationCardId){

        //保存新增加学生的相关信息到student_info表中
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setBuildingName(buildingName);
        studentInfo.setDormNumber(dormNumber);
        studentInfo.setLockId(lockId);
        studentInfo.setUserNumber(userNumber);
        studentInfo.setStudentName(studentName);
        studentInfo.setStudentNumber(studentNumber);
        studentInfo.setStudentCardId(studentCardId);
        studentInfo.setIdentificationCardId(identificationCardId);
        studentInfoRepository.save(studentInfo);
        //将新增加的学生信息及同一个寝室同学的相关信息同步到sync_command表中
        SyncCommand syncCommand = syncCommandRepository.findByLockId(lockId);
        if (syncCommand == null){
            SyncCommand syncCommand1 = new SyncCommand();
            String result = findByLockId(lockId);  //调用下面定义的函数
            syncCommand1.setLockId(lockId);
            syncCommand1.setInfo(result);
            syncCommandRepository.save(syncCommand1);
        }else {
            String result = findByLockId(lockId);  //调用下面定义的函数
            syncCommand.setInfo(result);
            syncCommandRepository.save(syncCommand);
        }

        return "success";
    }




    /**
     * 更新学生信息
     * 更新学生的信息同增加一个学生的信息效果相同，直接修改学生的学生卡id或者身份证id信息，将寝室中所有的学生信息都返回
     * @param id
     */
    @Transactional
    @RequestMapping(value = "/editStudentInfo")
    public void editStudentInfo(@RequestParam("id") Integer  id,
                                @RequestParam("studentCardId") String studentCardId,
                                @RequestParam("identificationCardId") String identificationCardId){

        StudentInfo studentInfo = studentInfoRepository.findById(id).orElse(null); //根据id查找到学生信息
        String lockId1 = studentInfo.getLockId(); //获取本学生对应的锁id
        studentInfo.setStudentCardId(studentCardId);
        studentInfo.setIdentificationCardId(identificationCardId);
        studentInfoRepository.save(studentInfo);

        StringBuilder s1 = new StringBuilder();
        //学生校园卡或身份证丢失得到的新的卡ID，此时更新一个人的信息，但是需要把全寝室人的信息全部写到sync_command表中
        try {
            SyncCommand syncCommand = syncCommandRepository.findByLockId(lockId1); //在同步表中找到对应锁id的信息
            String result = findByLockId(lockId1);  //调用下面定义的函数，返回一个拼接好的学生信息的字符串
            syncCommand.setInfo(result); //将返回的字符串覆盖之前的info字段内的信息
            syncCommandRepository.save(syncCommand);
        } catch (Exception e) {
            throw new MyException("发生运行时异常!");
        }


    }






    /**
     * 单行删除学生信息
     * @param id
     */
    @Transactional
    @RequestMapping(value = "/deleteOne")
    public void deleteOne(@RequestParam("id") Integer id){

        //在单个删除学生信息之前，按照规定的数据格式组帧下发数据存到数据库中sync_command表中
        //下发格式（删除用户）：（锁id）02（用户代号）ffff
        StudentInfo studentInfo = studentInfoRepository.findById(id).orElse(null);
        String lockId = studentInfo.getLockId();  //获得删除的学生寝室的锁id
        String userNumber = studentInfo.getUserNumber(); //获得删除学生的用户编号

        studentInfoRepository.deleteById(id);  //根据前端传人的id，删除对应的学生信息

        String result = findByLockId(lockId);
        SyncCommand syncCommand = syncCommandRepository.findByLockId(lockId);
        syncCommand.setCommand(userNumber);
        syncCommand.setInfo(result);
        syncCommandRepository.save(syncCommand);

    }

    /**
     * 批量删除客户的具体信息的操作
     */
    @Transactional
    @RequestMapping(value = "/deleteMany")
    public void deleteMany(Integer ids[]) {
        for (int i = 0; i < ids.length; i++) {
            StudentInfo studentInfo = studentInfoRepository.findById(ids[i]).orElse(null);
            String lockId = studentInfo.getLockId();  //获得删除的学生寝室的锁id
            String userNumber = studentInfo.getUserNumber(); //获得删除学生的用户编号

            studentInfoRepository.deleteById(ids[i]);  //根据前端传人的id，删除对应的学生信息

            String result = findByLockId(lockId);
            SyncCommand syncCommand = syncCommandRepository.findByLockId(lockId);
            if (syncCommand.getCommand() == null){  //删除之前判断command字段是否为空，若为空则直接插入
                syncCommand.setCommand(userNumber);
            }else {                                //若不为空则直接拼接
                String beforeCommand = syncCommand.getCommand();
                String afterCommand = beforeCommand + userNumber;
                syncCommand.setCommand(afterCommand);
            }
            syncCommand.setInfo(result);
            syncCommandRepository.save(syncCommand);
        }
    }


    /**
     * 根据锁id在学生信息表中查找学生相关信息的函数
     */
    public String findByLockId(String lockId){

        StringBuilder stringBuilder = new StringBuilder();
        String number;
        String cardID;
        String identificationID;
        List<StudentInfo> arrayList = studentInfoRepository.findByLockId(lockId);
        for (int i = 0 ; i < arrayList.size() ; i++){
            number = arrayList.get(i).getUserNumber();
            cardID = arrayList.get(i).getStudentCardId();
            identificationID = arrayList.get(i).getIdentificationCardId();
            stringBuilder.append(number+cardID+identificationID);
        }
        return stringBuilder.toString();
    }




    /**
     * 接收前台传入的学生信息excel表格
     * 将整个表格的内容读出写进数据库
     */
    @RequestMapping("/getExcel")
    public String getExcel(@RequestParam("file") MultipartFile file){

        String fetchResult;
        String fileName = file.getOriginalFilename();
        try {
            fetchResult = batchImport(fileName,file);
        }catch (Exception e) {
            return e.getMessage();
        }
        return fetchResult;

    }
    //批量导入函数
    @Transactional
    public String batchImport( String fileName, MultipartFile file) throws IOException, MyException {

        String result = "error";
        List<StudentInfo> studentInfoList = new ArrayList<>(); //装载学生信息对象
        Map<String,String> syncCommandMap = new HashMap<>();   //装载组装同步命令字符串
        List<SyncCommand> syncCommandList = new ArrayList<>(); //装载同步命令对象
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {    //如果满足条件则不是2003及以前版本
            isExcel2003 = false;
        }
        InputStream is = file.getInputStream();
        Workbook wb = null;
        if (isExcel2003){
            wb = new HSSFWorkbook(is);
        }else {
            wb = new XSSFWorkbook(is);
        }
        Sheet sheet = wb.getSheetAt(0);
        if (sheet!=null){
            result = "success";
        }

        StudentInfo studentInfo;
        SyncCommand syncCommand;
        for (int r = 1; r <= sheet.getLastRowNum(); r++){  //读取excel中的每一行对应的数据
            Row row = sheet.getRow(r);
            if (row == null){
                continue;
            }
            studentInfo = new StudentInfo();

            String buildingName = row.getCell(0).getStringCellValue();
            if (buildingName == null || buildingName.isEmpty()){
                throw new MyException("导入失败（第"+(r+1)+"行，楼宇名称未填写)");
            }

            row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
            String dormNumber = row.getCell(1).getStringCellValue();
            if (dormNumber == null || dormNumber.isEmpty()){
                throw new MyException("导入失败（第"+(r+1)+"行，寝室号未填写)");
            }

            row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
            String lockId = row.getCell(2).getStringCellValue();
            if (lockId == null || lockId.isEmpty()){
                throw new MyException("导入失败（第"+(r+1)+"行，锁ID未填写)");
            }else {
                if (lockId.length() == 1) lockId = "000"+lockId;
                if (lockId.length() == 2) lockId = "00"+lockId;
                if (lockId.length() == 3) lockId = "0" +lockId;
            }

            row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
            String userNumber = row.getCell(3).getStringCellValue();
            String userNumber1 = "0"+userNumber;
            if (userNumber == null || userNumber.isEmpty()){
                throw new MyException("导入失败（第"+(r+1)+"行，学生编号未填写)");
            }

            String studentName = row.getCell(4).getStringCellValue();
            if (studentName == null || studentName.isEmpty()){
                throw new MyException("导入失败（第"+(r+1)+"行，学生姓名未填写)");
            }

            row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
            String studentNumber = row.getCell(5).getStringCellValue();
            if (studentNumber == null || studentNumber.isEmpty()){
                throw new MyException("导入失败（第"+(r+1)+"行，学号未填写)");
            }

            row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
            String studentCardId = row.getCell(6).getStringCellValue();
            if (studentCardId == null || studentCardId.isEmpty()){
                throw new MyException("导入失败（第"+(r+1)+"行，学生卡ID未填写)");
            }

            row.getCell(7).setCellType(Cell.CELL_TYPE_STRING);
            String identificationCardId = row.getCell(7).getStringCellValue();
            if (identificationCardId == null || identificationCardId.isEmpty()){
                throw new MyException("导入失败（第"+(r+1)+"行，身份证ID未填写)");
            }

            studentInfo.setBuildingName(buildingName);
            studentInfo.setDormNumber(dormNumber);
            studentInfo.setLockId(lockId);
            studentInfo.setUserNumber(userNumber1);
            studentInfo.setStudentName(studentName);
            studentInfo.setStudentNumber(studentNumber);
            studentInfo.setStudentCardId(studentCardId);
            studentInfo.setIdentificationCardId(identificationCardId);
            studentInfoList.add(studentInfo);   //把excel信息映射成studentInfo然后放入list中


            StringBuilder stringBuilder = new StringBuilder();
            String appendBefore;
            String appendAfter;
            if (!syncCommandMap.containsKey(lockId)){  //map中不包含某个锁id，直接放入
                syncCommandMap.put(lockId,"0"+userNumber+studentCardId+identificationCardId);
            }else {
                appendBefore = syncCommandMap.get(lockId);
                stringBuilder.append(appendBefore);
                stringBuilder.append("0"+userNumber+studentCardId+identificationCardId);
                appendAfter = stringBuilder.toString();
                syncCommandMap.put(lockId,appendAfter);
            }
        }
        //syncCommandMap数据装载结束之后遍历出key和value实例化syncCommand，然后将对象装载到syncCommandList中
        Iterator iterator = syncCommandMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry =(Map.Entry)iterator.next();
            String key = (String)entry.getKey();
            String value =(String)entry.getValue();
            syncCommand  = new SyncCommand();
            syncCommand.setLockId(key);
            syncCommand.setInfo(value);
            syncCommandList.add(syncCommand);
        }
        try {
            //把所有的学生信息对象遍历出来，存进数据库
            for (StudentInfo studentInfo1: studentInfoList){
                studentInfoRepository.save(studentInfo1);
            }
            //把所有同步命令对象遍历出来，存进数据库
            for (SyncCommand syncCommand1 : syncCommandList){
                syncCommandRepository.save(syncCommand1);
            }
        }catch (RuntimeException e){

            throw new MyException("写入数据库出错！");  //当抛出此异常的时候 事务回滚

        }

        return result;
    }


}
