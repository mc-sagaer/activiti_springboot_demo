package com.utils;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

@Service
public class ActivitiServiceNewUtil {

    @Autowired
    private ProcessEngine processEngine;

    // 流程部署定义有几种，一种是自动注入（当前项目便是，配置下yml注入的资源位置）； 一种是导入bpmn，（png可选，导入不导入都行）； 还有一种用的也多，导入含bpmn，png的zip文件

    /**
     * 部署流程定义
     * 导入bpmn，（png可选，导入不导入都行）
     */
    public void deploymentResourceByBpmn(String bpmnPath, String pngPath, String name) {
        // 1、创建ProcessEngine 不用创建了，这里直接注入
        // ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、得到RepositoryService实例
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3、使用RepositoryService进行部署
        Deployment deployment = repositoryService.createDeployment()
                // .addClasspathResource("bpmn/evection.bpmn") // 添加bpmn资源
                // .addClasspathResource("bpmn/evection.png") // 添加png资源
                .addClasspathResource(bpmnPath) // 添加bpmn资源
                .addClasspathResource(pngPath) // 添加png资源
                .name(name)
                .deploy();
        // 4、输出部署信息
        System.out.println("流程部署id：" + deployment.getId());
        System.out.println("流程部署名称：" + deployment.getName());
    }


    /**
     * 部署流程定义
     * 导入zip
     */
    public void deploymentResourceByZip(File file, String name) throws FileNotFoundException {

        // 1、创建ProcessEngine 不用创建了，这里直接注入
        // ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、得到RepositoryService实例
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3、使用RepositoryService进行部署
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)//
                .name(name)
                .deploy();
        // 4、输出部署信息
        System.out.println("流程部署id：" + deployment.getId());
        System.out.println("流程部署名称：" + deployment.getName());
    }


    /**
     * 查询流程定义
     */
    public List getProcessDefinitionList(String key) {
        // 获取引擎
        //ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 得到ProcessDefinitionQuery 对象
        ProcessDefinitionQuery processDefinitionQuery =
                repositoryService.createProcessDefinitionQuery();
        // 查询出当前所有的流程定义
        // 条件：processDefinitionKey =evection
        // orderByProcessDefinitionVersion 按照版本排序
        // desc倒叙
        // list 返回集合
        List<ProcessDefinition> definitionList =
                processDefinitionQuery.processDefinitionKey(key)
                        .orderByProcessDefinitionVersion()
                        .desc()
                        .list();
        return definitionList;
    }


    /**
     * 删除流程定义（删除流程图）
     */
    public void deleteDeployment(String deploymentId, boolean cascade) {

        // 通过流程引擎获取repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();

        //删除流程定义，如果该流程定义已有流程实例启动则删除时出错
        //repositoryService.deleteDeployment(deploymentId);

        //设置true 级联删除流程定义，即使该流程有流程实例启动也可以删除，设置为false非级别删除方式,如果该流程定义已有流程实例启动则删除时出错
        repositoryService.deleteDeployment(deploymentId, cascade);
    }

    /**
     * 启动流程实例
     */
    public Map startProcess(String key) {

        // 2、获取RunTimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3、根据流程定义Id启动流程
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey(key);
        // 输出内容
        System.out.println("流程定义id：" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id：" + processInstance.getId());
        System.out.println("当前活动Id：" + processInstance.getActivityId());
        Map map = new HashMap<>();
        map.put("processDefinitionId", processInstance.getProcessDefinitionId());
        map.put("processInstanceId", processInstance.getProcessDefinitionId());
        map.put("processInstanceName", processInstance.getName());
        return map;
    }

    /**
     * 查询当前个人待执行的任务
     * assignee 任务负责人
     */
    public List<Task> getPersonalTaskList(String assignee, String key) {

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 创建TaskService
        TaskService taskService = processEngine.getTaskService();
        // 根据流程key 和 任务负责人 查询任务
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey(key) //流程Key
                .taskAssignee(assignee)//只查询该任务负责人的任务
                .list();
        return list;
    }

    /**
     * 查询任务
     * assignee 任务负责人
     */
    public Task getTaskByAssigneeAndUuid(String assignee, String uuid) {

        // 获取taskService
        TaskService taskService = processEngine.getTaskService();
        // 根据流程key 和 任务的负责人 查询任务
        // 返回一个任务对象
        Task task = taskService.createTaskQuery()
                .processVariableValueEquals("uuid", uuid) //流程Key
                .taskAssignee(assignee) //要查询的负责人
                .singleResult();

        return task;
    }

    /**
     * 查询任务
     * assignee 任务负责人
     */
    public Task getTaskByAssigneeAndBusinessKey(String assignee, String businessKey) {

        // 获取taskService
        TaskService taskService = processEngine.getTaskService();
        // 根据流程key 和 任务的负责人 查询任务
        // 返回一个任务对象
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .taskAssignee(assignee) //要查询的负责人
                .singleResult();

        return task;
    }

    /**
     * 查询任务
     * assignee 任务负责人
     */
    public List<Task> getTaskListByBusinessKey(String businessKey) {

        // 获取taskService
        TaskService taskService = processEngine.getTaskService();
        // 根据流程key 和 任务的负责人 查询任务
        // 返回一个任务对象
        List<Task> list = taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .list();

        return list;
    }

    /**
     * 完成单个任务（不推荐）
     * 假如一个流程上，张三连着请了两个假，正好都到了你这了，那返回的可就不是一个任务了，是两个。 就不能用这个
     */
    public void completSingleTaskByAssigneeAndKey(String assignee, String key) {

        // 获取taskService
        TaskService taskService = processEngine.getTaskService();
        // 根据流程key 和 任务的负责人 查询任务
        // 返回一个任务对象
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(key) //流程Key
                .taskAssignee(assignee) //要查询的负责人
                .singleResult();

        // 完成任务,参数：任务id
        taskService.complete(task.getId());
    }

    /**
     * 完成任务
     * 因为每个任务有唯一的uuid, 但这并不够，假如是并行任务呢，还需要来个assignee（负责人区分）
     */
    public void completSingleTaskByUuid(String assignee, String uuid) {
        Task task = getTaskByAssigneeAndUuid(assignee, uuid);
        // 完成任务,参数：任务id
        processEngine.getTaskService().complete(task.getId());
    }

    /**
     * 完成任务
     * 因为每个任务有唯一的uuid, 但这并不够，假如是并行任务呢，还需要来个assignee（负责人区分）
     */
    public void completByTaskId(String taskId) {
        // 完成任务,参数：任务id
        processEngine.getTaskService().complete(taskId);
    }

    /**
     * 查看历史信息
     */
    public List findHistoryInfo(String instanceId) {

        // 获取HistoryService
        HistoryService historyService = processEngine.getHistoryService();
        // 获取 actinst表的查询对象
        HistoricActivityInstanceQuery instanceQuery =
                historyService.createHistoricActivityInstanceQuery();
        // 查询 actinst表，条件：根据 InstanceId 查询
        // instanceQuery.processInstanceId("2501");
        // 查询 actinst表，条件：根据 DefinitionId 查询
        instanceQuery.processInstanceId(instanceId);
        // 增加排序操作,orderByHistoricActivityInstanceStartTime 根据开始时间排序 asc 升序
        instanceQuery.orderByHistoricActivityInstanceStartTime().asc();
        // 查询所有内容
        List<HistoricActivityInstance> activityInstanceList = instanceQuery.list();

        return activityInstanceList;
    }


    /**
     * 拾取任务---候选人模式
     */
    public void claimTask(String businessKey, String candidateUser) {
        //得到TaskService对象
        TaskService taskService = processEngine.getTaskService();

        //执行查询
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();

        if (task != null) {
            taskService.claim(task.getId(), candidateUser);//第一个参数任务ID,第二个参数为具体的候选用户名
            System.out.println("任务拾取完毕!");
        }

    }

    /**
     * 归还任务---候选人模式
     */
    public void backTask(String businessKey) {
        TaskService taskService = processEngine.getTaskService();

        //执行查询
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();
        //判断是否有这个任务
        if (task != null) {
            //如果设置为null,归还组任务,该任务没有负责人
            taskService.setAssignee(task.getId(), null);
        }
    }

    /**
     * 任务交接---候选人模式
     */
    public void giveTaskToOtherUser(String businessKey, String candidateUser) {
        TaskService taskService = processEngine.getTaskService();

        //执行查询
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();
        //判断是否有这个任务
        if (task != null) {
            taskService.setAssignee(task.getId(), candidateUser);
            System.out.println("交接任务完成~!");
        }
    }

}
