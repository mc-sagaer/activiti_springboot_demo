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

import java.util.Map;

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
        QingjiaBean qingjiaBean = new QingjiaBean(66666, 8, "mc", "旋律的回响");
        qingjiaService.startActivitiDemos("qingjiaProcess", qingjiaBean);
    }

    /**
     * 执行流程
     */
    @Test
    public void sunmmitProcess(){
        qingjiaService.submmitActivitiDemos("caiwu", "my-29c6e425cf4a41abbdc6d94f68527b70");

    }




}
