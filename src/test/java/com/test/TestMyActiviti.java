package com.test;


import com.ActApplication;
import com.mySelf.bean.QingjiaBean;
import com.mySelf.service.QingjiaService;

import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 自己写的测试类
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ActApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestMyActiviti {

    @Autowired
    private QingjiaService qingjiaService;


    /**
     * 删除流程定义（删除流程图）
     */
    @Test
    public void deleteDeployment(){
        qingjiaService.deleteDeploymentActivitiDemos("c08301af-4a67-11eb-bdf4-002324e3fafb", true);
    }


    /**
     * 查看流程定义
     */
    @Test
    public void getDeployment(){
        Map qingjiaProcess = qingjiaService.getDeploymentActivitiDemos("qingjiaProcess");
        System.out.println(qingjiaProcess);


    }

    /**
     * 启动流程
     */
    @Test
    public void startProcess(){
        QingjiaBean qingjiaBean = new QingjiaBean(63210, 2, "mc2021-1", "星繁");
        qingjiaService.startActivitiDemos("qingjiaProcess", qingjiaBean);
    }

    /**
     * 启动流程
     */
    @Test
    public void start222Process(){
        Map conditionMap = new HashMap();
        conditionMap.put("moneyy", 54321);
        qingjiaService.start22ActivitiDemos("sonProcess", conditionMap);
    }

    /**
     * 执行流程
     */
    @Test
    public void sunmmitProcess(){
        qingjiaService.submmitActivitiDemos("jingli2", "my-adbeb5d350204184bf72d6e9c2dbfa64");

    }

    /**
     * 执行流程
     */
    @Test
    public void sunmmitProcessAndSetVar(){
        // 发起初始，我就把条件都给定
        Map conditionMap = new HashMap();
        QingjiaBean qingjiaBean = new QingjiaBean(83210, 2, "mc2021-1", "星繁");
        conditionMap.put("qingjiaBean", qingjiaBean);
       String  uuid  = "my-0634d699cb7e4d09b1b563a073552a2a";
        conditionMap.put("uuid", uuid);
        conditionMap.put("moneyy", 12);

        qingjiaBean.setUuid(uuid);
        qingjiaService.submmitActivitiDemos("sonkezhang2", uuid, conditionMap);

    }

    /**
     * 查询流程到哪了, 用uuid
     */
    @Test
    public void getProcessByUuid(){
        Map activitiTaskDemosByBusinessKey = qingjiaService.getActivitiTaskDemosByUuid("my-0634d699cb7e4d09b1b563a073552a2a");
        System.out.println(activitiTaskDemosByBusinessKey);
    }


    /**
     * 查询流程到哪了
     */
    @Test
    public void getProcess(){
        Map activitiTaskDemosByBusinessKey = qingjiaService.getActivitiTaskDemosByBusinessKey("my-adbeb5d350204184bf72d6e9c2dbfa64");
        System.out.println(activitiTaskDemosByBusinessKey);
    }


    @Test
    public void getProces22222s() throws IOException, ClassNotFoundException {
        //读取
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("C:\\Users\\mc\\Desktop\\qqq"));

        Object obj = ois.readObject();
        //读一个Object
//        QingjiaBean s = (QingjiaBean) obj;
//        System.out.println(s);
    }




    /**
     * 领取任务
     */
    @Test
    public void claimProcess(){
       qingjiaService.setActivitiTaskDemosCandidateByBusinessKey("my-ed2b86c50cab493787ee7fa2dd6a8ad4", "zhangsan");
    }

    /**
     * 退还任务
     */
    @Test
    public void backProcess(){
        qingjiaService.backActivitiTaskDemosCandidateByBusinessKey("my-ed2b86c50cab493787ee7fa2dd6a8ad4");
    }

    /**
     * 交接任务
     */
    @Test
    public void giveProcess(){
        qingjiaService.giveActivitiTaskDemosCandidateByBusinessKey("my-ed2b86c50cab493787ee7fa2dd6a8ad4", "lisi");
    }





}
