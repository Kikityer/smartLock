package com.ahu.lock.controller;

import com.ahu.lock.domain.LockState;
import com.ahu.lock.repository.LockStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName LockStateController
 * @Description TODO
 * @Author Kikityer
 * @Date 2018/10/9 9:06
 * @Version 1.0.0
 **/

@RestController
@RequestMapping(value = "/lockState")
public class LockStateController {

    private final LockStateRepository lockStateRepository;

    @Autowired
    public LockStateController(LockStateRepository lockStateRepository) {  //依赖注入 通过构造函数注入仓储

        this.lockStateRepository = lockStateRepository;
    }


    /**
     * 获取锁状态的所有信息(后续需要改变 只获取24小时内的状态信息)
     */
    @RequestMapping(value = "/getLockState", method = RequestMethod.GET)
    public List<LockState> getLockState() {

        List<LockState> arrayList = new ArrayList<>();

        getDifferentTimeState(1, arrayList);

        return arrayList;
    }

    private void getDifferentTimeState(int days, List arrayList) {

        Date currentDate = new Date();
        Date temp = new Date(currentDate.getTime() - (long) days * 24 * 60 * 60 * 1000);

        List<LockState> hen = lockStateRepository.findDifferentDate(currentDate, temp);

        for (int f = hen.size(); f > 0; f--) {
            arrayList.add(hen.get(f - 1));
        }

    }


    /**
     * 单行删除锁状态信息操作
     */
    @RequestMapping(value = "/deleteOne")
    public void deleteOne(@RequestParam("id") Integer id) {

        lockStateRepository.deleteById(id);
    }


    /**
     * 批量删除锁状态信息操作
     *
     * @param ids
     */
    @RequestMapping(value = "/deleteMany")
    public void deleteMany(Integer ids[]) {

        for (int i = 0; i < ids.length; i++) {

            lockStateRepository.deleteById(ids[i]);
        }
    }


    /**
     * 实现立即开锁方案二：
     * 将前台的的lockId传到后台，通过socket实现和通信模块直接对等通信。
     * 创建一个socketClient
     *
     * @param lockId
     */
    @RequestMapping(value = "/openImmediately")
    private String openDoor(@RequestParam("lockId") String lockId) {

        final int TIMEOUT = 15000; //设置15秒超时
        Socket socket = null;
        StringBuilder result = new StringBuilder();
        try {

            socket = new Socket("172.19.2.94", 5426);  //建立一个socket对像
            socket.setSoTimeout(TIMEOUT);

            //向socket服务端发送信息
            OutputStream os = socket.getOutputStream(); //获得一个字节输出流
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(os);
            bufferedOutputStream.write((lockId+"03ffff").getBytes());  //输出
            bufferedOutputStream.flush();
            System.out.println("发送完毕");

            //接收socket服务端返回的信息
            InputStream is = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String temp = "";
            if ((temp = bufferedReader.readLine())!= null) {
                result.append(temp);
            }
            System.out.println(result);

            //关闭资源
            os.close();
            bufferedOutputStream.close();
            is.close();
            inputStreamReader.close();
            bufferedReader.close();
            socket.close();
            System.out.println(123);
        } catch (InterruptedIOException e){     //接收服务器回复信息超时异常

           return e.getMessage();
        } catch (ConnectException exception){   //连接服务器异常(拒绝连接、连接超时等)

            return exception.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

}
