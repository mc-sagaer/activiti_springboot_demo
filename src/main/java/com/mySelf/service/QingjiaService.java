package com.mySelf.service;

import com.mySelf.bean.QingjiaBean;
import com.utils.ActivitiServiceNewUtil;
import com.utils.ActivitiServiceUtil;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class QingjiaService {

    @Autowired
    private ActivitiServiceUtil activitiServiceUtil;

    @Autowired
    private ActivitiServiceNewUtil activitiServiceNewUtil;


    @Autowired
    private ProcessEngine processEngine;


    // 部署流程定义
    public Map deploymentActivitiDemos(String path) {
        File file = new File(path);
        Map map = activitiServiceUtil.saveNewDeploye(file, file.getName());
        return map;
    }

    // 删除流程定义
    public Map deleteDeploymentActivitiDemos(String deploymentId, boolean cascade) {
        activitiServiceNewUtil.deleteDeployment(deploymentId, cascade);
        Map map = new HashMap();
        map.put("success", true);
        return map;
    }

    // 查询流程定义
    public Map getDeploymentActivitiDemos(String key) {
        List processDefinitionList = activitiServiceNewUtil.getProcessDefinitionList(key);
        Map map = new HashMap();
        map.put("processDefinitionList", processDefinitionList);
        return map;
    }

    // 发起一个请假
    public Map start22ActivitiDemos(String key,  Map conditionMap ) {


        String uuid = "my-" + UUID.randomUUID().toString().replaceAll("-", "");
        conditionMap.put("uuid", uuid);



        // 这里不再调用工具方法，尝试使用原生方法Api
        ProcessInstance instance = processEngine.getRuntimeService().startProcessInstanceByKey(key, uuid, conditionMap);

        JdbcService.update("INSERT INTO demo_qingjia (id, name, reason, day, take_money, unique_id) VALUES (?, ?, ?, ?, ?, ?)"
                , null, 1, 1,1, 1, uuid
        );

        System.out.println(instance.toString());
        Map map = new HashMap();
        map.put("instance", instance);
        return map;
    }

    // 发起一个请假
    public Map startActivitiDemos(String key, QingjiaBean qingjiaBean) {

        // 发起初始，我就把条件都给定
        Map conditionMap = new HashMap();
        conditionMap.put("qingjiaBean", qingjiaBean);
        // 我也可以额外加一些参数，
        String uuid = "my-" + UUID.randomUUID().toString().replaceAll("-", "");
        conditionMap.put("uuid", uuid);
        conditionMap.put("moneyy", 12);

        qingjiaBean.setUuid(uuid);

        // 这里不再调用工具方法，尝试使用原生方法Api
        ProcessInstance instance = processEngine.getRuntimeService().startProcessInstanceByKey(key, uuid, conditionMap);

        JdbcService.update("INSERT INTO demo_qingjia (id, name, reason, day, take_money, unique_id) VALUES (?, ?, ?, ?, ?, ?)"
                , null, qingjiaBean.getName(), qingjiaBean.getReason(), qingjiaBean.getDay(), qingjiaBean.getMoney(), uuid
        );

        System.out.println(instance.toString());
        System.out.println(uuid);
        Map map = new HashMap();
        map.put("instance", instance);
        return map;
    }


    // 执行一个请假
    public Map submmitActivitiDemos(String assignee, String uniqueId, Map<String,Object>varlues) {
        Task task = activitiServiceNewUtil.getTaskByAssigneeAndUuid(assignee, uniqueId);
        activitiServiceNewUtil.completByTaskIdAndSetVar(task.getId(), varlues);
        boolean b = JdbcService.update("INSERT INTO demo_log (name, note, unique_id) VALUES (?, ?, ?)", task.getName()+task.getAssignee(), "let me..." + task.getId(), uniqueId);
        System.out.println("已执行该节点" + b);
        Map map = new HashMap();
        map.put("success", b);
        return map;
    }

    // 执行一个请假
    public Map submmitActivitiDemos(String assignee, String uniqueId) {
        Task task = activitiServiceNewUtil.getTaskByAssigneeAndUuid(assignee, uniqueId);
        activitiServiceNewUtil.completByTaskId(task.getId());
        boolean b = JdbcService.update("INSERT INTO demo_log (name, note, unique_id) VALUES (?, ?, ?)", task.getName()+task.getAssignee(), "let me..." + task.getId(), uniqueId);
        System.out.println("已执行该节点" + b);
        Map map = new HashMap();
        map.put("success", b);
        return map;
    }


    // 查询我的请假
    public Map getActivitiTaskDemos(String key, String user) {

        List<Task> personalTaskList = activitiServiceNewUtil.getPersonalTaskList(user, key);

        Map map = new HashMap();
        map.put("personalTaskList", personalTaskList);
        return map;
    }


    // 查询任务节点通过自己的uuid
    public Map getActivitiTaskDemosByAssigneeAndUuid(String assignee, String uniqueId) {
        Task task = activitiServiceNewUtil.getTaskByAssigneeAndUuid(assignee, uniqueId);
        Map map = new HashMap();
        map.put("task", task);
        return map;
    }

    // 查询任务节点通过自己的uuid
    public Map getActivitiTaskDemosByUuid(String uniqueId) {
        List<Task> list = activitiServiceNewUtil.getTaskByUuid(uniqueId);
        Map map = new HashMap();
        map.put("taskList", list);
        return map;
    }

    // 查询任务节点通过业务键
    public Map getActivitiTaskDemosByBusinessKey(String businessKey) {
        List<Task> personalTaskList = activitiServiceNewUtil.getTaskListByBusinessKey(businessKey);

        Map map = new HashMap();
        map.put("personalTaskList", personalTaskList);
        return map;
    }

    // 设置候选人为办理人
    public void setActivitiTaskDemosCandidateByBusinessKey(String businessKey, String user) {
       activitiServiceNewUtil.claimTask(businessKey, user);

    }

    // 退还任务
    public void backActivitiTaskDemosCandidateByBusinessKey(String businessKey) {
        activitiServiceNewUtil.backTask(businessKey);

    }

    // 交接任务
    public void giveActivitiTaskDemosCandidateByBusinessKey(String businessKey, String user) {
        activitiServiceNewUtil.giveTaskToOtherUser(businessKey, user);

    }




}
