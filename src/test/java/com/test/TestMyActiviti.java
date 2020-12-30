package com.test;


import com.ActApplication;
import com.mySelf.bean.QingjiaBean;
import com.mySelf.service.QingjiaService;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
     * 启动流程
     */
    @Test
    public void startProcess(){
        QingjiaBean qingjiaBean = new QingjiaBean(16888, 7, "mc", "终章的叹息");
        qingjiaService.startActivitiDemos("qingjiaProcess", qingjiaBean);
    }

    /**
     * 执行流程
     */
    @Test
    public void submmitProcess(){
        qingjiaService.submmitActivitiDemos("my-e083b4a177da4f449ad7e77af3b5b33c");

    }

    /**
     * 执行流程
     */
    @Test
    public void submmitPro22cess(){
        qingjiaService.submmitActivitiDemos("my-e083b4a177da4f449ad7e77af3b5b33c");

    } /**
     * 执行流程
     */
    @Test
    public void submmitProce2ss(){
        qingjiaService.submmitActivitiDemos("my-e083b4a177da4f449ad7e77af3b5b33c");

    } /**
     * 执行流程
     */
    @Test
    public void subm2mi2tProcess(){
        qingjiaService.submmitActivitiDemos("my-e083b4a177da4f449ad7e77af3b5b33c");

    } /**
     * 执行流程
     */
    @Test
    public void subm2mitProcess(){
        qingjiaService.submmitActivitiDemos("my-e083b4a177da4f449ad7e77af3b5b33c");

    }
    /**
     * 执行流程
     */
    @Test
    public void s22ubmmitProcess(){
        qingjiaService.submmitActivitiDemos("my-e083b4a177da4f449ad7e77af3b5b33c");

    } /**
     * 执行流程
     */
    @Test
    public void su12bmmitProcess(){
        qingjiaService.submmitActivitiDemos("my-e083b4a177da4f449ad7e77af3b5b33c");

    }

}
