package com.mySelf.service;

import com.mySelf.bean.QingjiaBean;
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
    private ProcessEngine processEngine;


    // 部署流程定义
    public Map deploymentActivitiDemos(String path) {
        File file = new File(path);
        Map map = activitiServiceUtil.saveNewDeploye(file, file.getName());
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

        // 这里不再调用工具方法，尝试使用原生方法Api
        ProcessInstance instance = processEngine.getRuntimeService().startProcessInstanceByKey(key, conditionMap);

        JdbcService.update("INSERT INTO demo_qingjia (id, name, reason, day, take_money, unique_id) VALUES (?, ?, ?, ?, ?, ?)"
                , null, qingjiaBean.getName(), qingjiaBean.getReason(), qingjiaBean.getDay(), qingjiaBean.getMoney(), uuid
        );

        System.out.println(instance.toString());
        Map map = new HashMap();
        map.put("instance", instance);
        return map;
    }


    // 执行一个请假
    public Map submmitActivitiDemos(String uniqueId) {

        // 用自己存的唯一uuid获取这个请假实例
        List<Task> taskList = processEngine.getTaskService().createTaskQuery()
                .processVariableValueEquals("uuid", uniqueId)
                .list();

        if (taskList.isEmpty()) {
            return new HashMap();
        }

        // 可能出现该节点是并行任务，同一个节点需要几个人同意，，执行一次少一个。
        Task task = taskList.get(0);
        String taskId = task.getId();
        String taskName = task.getName();
        System.out.println(taskId + taskName);

        // 去完成一个节点
        processEngine.getTaskService().complete(taskId);

        boolean b = JdbcService.update("INSERT INTO demo_log (name, note, unique_id) VALUES (?, ?, ?)", taskName, "let me..." + taskId, uniqueId);
        System.out.println("已执行该节点" + b);
        Map map = new HashMap();
        map.put("success", b);
        return map;
    }


}
