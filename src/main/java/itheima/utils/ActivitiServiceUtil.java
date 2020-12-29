package itheima.utils;


import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.zip.ZipInputStream;


/**
 * 对原先的方法进行了迁徙，所有带test的全部迁至项目test包下面
 *
 * @author mc
 * @date 2020-11-18 15:28
 */

@Service
public class ActivitiServiceUtil {


    @Autowired
    private ProcessEngine processEngine;

    /**
     * 初始化数据库 已经迁徙到test
     */
    // @Test

//    @PostConstruct
//    public void createTables() {
//        // 1.创建 activiti 配置对象实例
//        ProcessEngineConfiguration configuration = ProcessEngineConfiguration
//                .createStandaloneProcessEngineConfiguration();
//        // 2.设置数据库连接信息（数据库地址、数据库驱动、用户名、密码、设置数据库建表策略）
//        configuration.setJdbcUrl(jdbc_url);
//        configuration.setJdbcDriver(jdbc_driver);
//        configuration.setJdbcUsername(jdbc_username);
//        configuration.setJdbcPassword(jdbc_password);
//        // java.lang.String DB_SCHEMA_UPDATE_FALSE = "false"; 不存在表，抛出异常
//        // java.lang.String DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop";  删除表后再创建表
//        // java.lang.String DB_SCHEMA_UPDATE_TRUE = "true"; 不存在表，就创建表，否则就直接使用表
//        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
//        // 3.使用配置对象创建流程引擎实例（检查数据库连接环境信息是否正确）
//        processEngine = configuration.buildProcessEngine();
//        System.out.println("avitiviti实例已启动---" + processEngine);
//    }

    private static long getTotalPageCount(long totalCount, int pageSize) {
        if (totalCount % pageSize == 0) {
            return totalCount / pageSize;
        } else {
            return totalCount / pageSize + 1;
        }
    }

    /** 部署流程定义 */
//	@Test
//	public void deploymentProcessDefinition() {
//		Deployment deployment = processEngine.getRepositoryService()// 与流程定义和部署对象相关的Service
//				.createDeployment()// 创建一个部署对象
//				.name("helloworld入门程序")// 添加部署的名称
//				.addClasspathResource("diagrams/helloworld.bpmn")// 从classpath的资源中加载，一次只能加载一个文件
//				.addClasspathResource("diagrams/helloworld.png")// 从classpath的资源中加载，一次只能加载一个文件
//				.deploy();// 完成部署
//		System.out.println("部署ID：" + deployment.getId());// 1
//		System.out.println("部署名称：" + deployment.getName());// helloworld入门程序
//	}

    /**
     * 部署流程定义
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Map saveNewDeploye(File file, String filename) {
        Map map = new HashMap();
        try {
            // 2：将File类型的文件转化成ZipInputStream流
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
            Deployment deployment = processEngine.getRepositoryService().createDeployment()// 创建部署对象
                    .name(filename)// 添加部署名称
                    .addZipInputStream(zipInputStream)//
                    .deploy();// 完成部署
            System.out.println("部署后返回ID为：" + deployment.getId());
            map.put("id", deployment.getId());
            map.put("name", deployment.getName());
            map.put("category", deployment.getCategory());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /** 启动流程实例 */
//	@Test
//	public void startProcessInstance() {
//		// 流程定义的key
//		String processDefinitionKey = "huifengSpShanghuiProcess";
//		ProcessInstance pi = processEngine.getRuntimeService()// 与正在执行的流程实例和执行对象相关的Service
//				.startProcessInstanceByKey(processDefinitionKey);// 使用流程定义的key启动流程实例，key对应helloworld.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动
//		System.out.println("流程实例ID:" + pi.getId());// 流程实例ID 101
//		System.out.println("流程定义ID:" + pi.getProcessDefinitionId());// 流程定义ID
//																	// helloworld:1:4
//	}

    /**
     * 启动流程实例
     *
     * @param processDefinitionKey 流程实例启动的key
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Map startProcessInstance(String processDefinitionKey) {
        // 流程定义的key
        ProcessInstance processInstance = processEngine.getRuntimeService()// 与正在执行的流程实例和执行对象相关的Service
                .startProcessInstanceByKey(processDefinitionKey);// 使用流程定义的key启动流程实例，key对应helloworld.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动
        System.out.println("流程实例ID:" + processInstance.getId());// 流程实例ID 101
        System.out.println("流程定义ID:" + processInstance.getProcessDefinitionId());// 流程定义ID
        // helloworld:1:4
        Map map = new HashMap();
        map.put("id", processInstance.getId());
        map.put("processDefinitionId", processInstance.getProcessDefinitionId());
        return map;
    }

    /** 查询当前人的个人任务 */
//	@Test
//	public void findMyPersonalTask() {
//		String assignee = "王五";
//		List<Task> list = processEngine.getTaskService()// 与正在执行的任务管理相关的Service
//				.createTaskQuery()// 创建任务查询对象
//				.taskAssignee(assignee)// 指定个人任务查询，指定办理人
//				.list();
//		if (list != null && list.size() > 0) {
//			for (Task task : list) {
//				System.out.println("任务ID:" + task.getId());
//				System.out.println("任务名称:" + task.getName());
//				System.out.println("任务的创建时间:" + task.getCreateTime());
//				System.out.println("任务的办理人:" + task.getAssignee());
//				System.out.println("流程实例ID：" + task.getProcessInstanceId());
//				System.out.println("执行对象ID:" + task.getExecutionId());
//				System.out.println("流程定义ID:" + task.getProcessDefinitionId());
//				System.out.println("########################################################");
//			}
//		} else {
//			System.out.println("没有任务。");
//		}
//	}

    /**
     * 查询当前的个人任务
     *
     * @param assignee
     */
    public List<Task> findMyPersonalTask(String assignee) {
        // String assignee = "王五";
        List<Task> list = processEngine.getTaskService()// 与正在执行的任务管理相关的Service
                .createTaskQuery()// 创建任务查询对象
                .taskAssignee(assignee)// 指定个人任务查询，指定办理人
                .list();
        if (list != null && list.size() > 0) {
            for (Task task : list) {
                System.out.println("任务ID:" + task.getId());
                System.out.println("任务名称:" + task.getName());
                System.out.println("任务的创建时间:" + task.getCreateTime());
                System.out.println("任务的办理人:" + task.getAssignee());
                System.out.println("流程实例ID：" + task.getProcessInstanceId());
                System.out.println("执行对象ID:" + task.getExecutionId());
                System.out.println("流程定义ID:" + task.getProcessDefinitionId());
                System.out.println("########################################################");
            }
        } else {
            System.out.println("没有任务。");
        }
        return list;
    }

    /** 完成我的任务 */
//	@Test
//	public void completeMyPersonalTask() {
//		Map<String, Object> variables = new HashMap<String,Object>();
//		variables.put("outcome", "打回重选");
//		// 任务ID
//		String taskId = "53606";
//		processEngine.getTaskService()// 与正在执行的任务管理相关的Service
//				.complete(taskId,variables);
//		System.out.println("完成任务：任务ID：" + taskId);
//	}

    /**
     * 完成我的任务
     *
     * @param taskId 任务ID
     */
    public void completeMyPersonalTask(String taskId) {
        // 任务ID
        // String taskId = "402";
        processEngine.getTaskService()// 与正在执行的任务管理相关的Service
                .complete(taskId);
        System.out.println("完成任务：任务ID：" + taskId);
    }
    /** 查询历史任务 **/
//	@Test
//	public void findMyHistoryTask() {
//		String assignee = "张三";
//		List<HistoricTaskInstance> list = processEngine.getHistoryService().createHistoricTaskInstanceQuery()
//				// .taskAssignee(assignee) //查询个人任务、在此处添加
//				.list();
//		if (list != null && list.size() > 0) {
//			for (HistoricTaskInstance task : list) {
//				System.out.println("任务ID:" + task.getId());
//				System.out.println("任务名称:" + task.getName());
//				System.out.println("任务的创建时间:" + task.getStartTime());
//				System.out.println("任务的完成时间:" + task.getEndTime());
//				System.out.println("任务完成时长:" + task.getWorkTimeInMillis());
//				System.out.println("任务的办理人:" + task.getAssignee());
//				System.out.println("流程实例ID：" + task.getProcessInstanceId());
//				System.out.println("执行对象ID:" + task.getExecutionId());
//				System.out.println("流程定义ID:" + task.getProcessDefinitionId());
//				System.out.println("########################################################");
//			}
//		} else {
//			System.out.println("没有历史任务");
//		}
//	}

    /**
     * 查询个人历史任务
     *
     * @param assignee
     */
    public List<HistoricTaskInstance> findMyHistoryTask(String assignee) {
        List<HistoricTaskInstance> list = processEngine.getHistoryService().createHistoricTaskInstanceQuery()
                .taskAssignee(assignee) //查询个人任务、在此处添加
                .orderByHistoricTaskInstanceStartTime()
                .desc()
                .list();
        return list;
    }

    /**
     * 删除流程定义，在未使用时可以删除
     *
     * @param processId
     */
    public void deleteMyProcess(String processId) {
        processEngine.getRepositoryService().deleteDeployment(processId);
        System.out.println("删除：" + processId);
    }

    /**
     * 删除流程定义，在未使用时可以删除 fxy ， 已经迁徙到test
     */
//	@Test
//	public void deleteMyProcess() {
//		String processId = "1";
//		processEngine.getRepositoryService().deleteDeployment(processId);
//		System.out.println("删除：" + processId);
//	}
//
//	public InputStream findImageInputStream(String deploymentId,
//			String imageName) {
//		return processEngine.getRepositoryService().getResourceAsStream(deploymentId, imageName);
//	}
    public Task findTaskByTaskId(String taskId) {
        Task task = processEngine.getTaskService().createTaskQuery()//
                .taskId(taskId)//使用任务ID查询
                .singleResult();
        return task;
    }

    public ProcessDefinition findProcessDefinitionByTaskId(String taskId) {
        //使用任务ID，查询任务对象
        Task task = processEngine.getTaskService().createTaskQuery()//
                .taskId(taskId)//使用任务ID查询
                .singleResult();
        //获取流程定义ID
        String processDefinitionId = task.getProcessDefinitionId();
        //查询流程定义的对象
        ProcessDefinition pd = processEngine.getRepositoryService().createProcessDefinitionQuery()//创建流程定义查询对象，对应表act_re_procdef
                .processDefinitionId(processDefinitionId)//使用流程定义ID查询
                .singleResult();
        return pd;
    }

    public void taskComplete(String taskId, Map<String, Object> variables) {
        this.processEngine.getTaskService().complete(taskId, variables);
    }

    public void taskComplete(String taskId) {
        this.processEngine.getTaskService().complete(taskId);
    }

    public void taskComment(String cellphone, String taskId, String message, String processInstanceId) {
        Authentication.setAuthenticatedUserId(cellphone);
        this.processEngine.getTaskService().addComment(taskId, processInstanceId, message);
    }

    public List<Comment> findCommentByTaskId(String taskId) {
        List<Comment> list = new ArrayList<Comment>();
        //使用当前的任务ID，查询当前流程对应的历史任务ID
        //使用当前任务ID，获取当前任务对象
        Task task = this.processEngine.getTaskService().createTaskQuery()//
                .taskId(taskId)//使用任务ID查询
                .singleResult();
        //获取流程实例ID
        String processInstanceId = task.getProcessInstanceId();
//		//使用流程实例ID，查询历史任务，获取历史任务对应的每个任务ID
//		List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()//历史任务表查询
//						.processInstanceId(processInstanceId)//使用流程实例ID查询
//						.list();
//		//遍历集合，获取每个任务ID
//		if(htiList!=null && htiList.size()>0){
//			for(HistoricTaskInstance hti:htiList){
//				//任务ID
//				String htaskId = hti.getId();
//				//获取批注信息
//				List<Comment> taskList = taskService.getTaskComments(htaskId);//对用历史完成后的任务ID
//				list.addAll(taskList);
//			}
//		}
        list = this.processEngine.getTaskService().getProcessInstanceComments(processInstanceId);
        return list;
    }

    /**
     * 查询部署对象信息，对应表（act_re_deployment）
     */
    public List<Deployment> findDeploymentList() {
        List<Deployment> list = this.processEngine.getRepositoryService().createDeploymentQuery()//创建部署对象查询
                .orderByDeploymenTime()
                .desc()//
                .list();
        return list;
    }

    /**
     * 查询部署对象信息
     **/
    public Deployment findDeployment(String deploymentId) {
        Deployment deployment = this.processEngine.getRepositoryService().createDeploymentQuery()
                .deploymentId(deploymentId)
                .singleResult();
        return deployment;
    }

    /**
     * 查询流程定义的信息，对应表（act_re_procdef）
     */
    public List<ProcessDefinition> findProcessDefinitionList() {
        List<ProcessDefinition> list = this.processEngine.getRepositoryService().createProcessDefinitionQuery()//创建流程定义查询
                .orderByProcessDefinitionVersion()
                .desc()//
                .list();
        return list;
    }

    /**
     * 查询流程定义的信息，对应表（act_re_procdef）
     */
    public List<ProcessDefinition> findProcessDefinitionList(String key) {
        List<ProcessDefinition> list = this.processEngine.getRepositoryService().createProcessDefinitionQuery()
                .processDefinitionKey(key)
                .orderByProcessDefinitionVersion()
                .desc()
                .list();
        return list;
    }

    /**
     * 流程定义资源文件，对应表（act_re_procdef）
     *
     * @param deploymentId
     * @return
     */
    public ProcessDefinition findProcessDefinition(String deploymentId) {
        ProcessDefinition processDefinition = this.processEngine.getRepositoryService().createProcessDefinitionQuery()//创建流程定义查询
                .deploymentId(deploymentId)
                .singleResult();
        return processDefinition;
    }

    /**
     * 删除部署的流程定义
     *
     * @param deploymentId
     */
    public void deleteProcessDefinitionByDeploymentId(String deploymentId) {
        this.processEngine.getRepositoryService().deleteDeployment(deploymentId, true);
    }

    /**
     * 保存，启动流程
     *
     * @param key
     * @param objId
     * @param variables
     */
    public void saveStartProcess(String key, String objId, Map<String, Object> variables) {
        this.processEngine.getRuntimeService().startProcessInstanceByKey(key, objId, variables);
    }

    /**
     * 根据定义的key查询当前所有任务
     *
     * @param key
     * @return
     */
    public List<Task> findTaskList(String key) {
        List<Task> list = this.processEngine.getTaskService().createTaskQuery()
                .processDefinitionKey(key)
                .orderByTaskName()
                .asc()
                .list();
        return list;
    }

    /**
     * 查询当前个人任务
     *
     * @return
     */
    public List<Task> findTaskList(String assignee, String key) {
        List<Task> list = this.processEngine.getTaskService().createTaskQuery()//
                .taskAssignee(assignee)//指定个人任务查询
                .processDefinitionKey(key) //指定流程的key 区分当前所在模块的工作流
                .orderByTaskCreateTime()
                .asc()//
                .list();
        return list;
    }

    public List<Task> findTaskListByAssignee(String assignee) {
        List<Task> list = this.processEngine.getTaskService().createTaskQuery()//
                .taskAssignee(assignee)//指定个人任务查询
                .orderByTaskCreateTime()
                .asc()//
                .list();
        return list;
    }

    public Page findTaskPage(String assignee, String key, int pageNo) {
        long count = findTaskListCount(assignee, key);
        int startIndex = (pageNo - 1) * 15;
        List<Task> list = this.processEngine.getTaskService().createTaskQuery()//
                .taskAssignee(assignee)//指定个人任务查询
                .processDefinitionKey(key) //指定流程的key 区分当前所在模块的工作流
                .orderByTaskCreateTime()
                .asc()//
                .listPage(startIndex, 15);
        long totalPageCount = getTotalPageCount(count, 15);
        return new Page(list, count, pageNo, 15, totalPageCount);
    }

    public long findTaskListCount(String assignee, String key) {
        return this.processEngine.getTaskService().createTaskQuery()//
                .taskAssignee(assignee)//指定个人任务查询
                .processDefinitionKey(key) //指定流程的key 区分当前所在任务模块的工作流
                .count();
    }


    /**
     * 获得到办理业务的数据
     *
     * @param taskId
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Map findDataObjectByTaskId(String taskId) {
        //1：使用任务ID，查询任务对象Task
        Task task = this.processEngine.getTaskService().createTaskQuery()//
                .taskId(taskId)//使用任务ID查询
                .singleResult();
        //2：使用任务对象Task获取流程实例ID
        String processInstanceId = task.getProcessInstanceId();
        //3：使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        ProcessInstance pi = this.processEngine.getRuntimeService().createProcessInstanceQuery()//
                .processInstanceId(processInstanceId)//使用流程实例ID查询
                .singleResult();
        //4：使用流程实例对象获取BUSINESS_KEY
        String buniness_key = pi.getBusinessKey();
        //5：获取BUSINESS_KEY对应的主键ID，使用主键ID，查询请假单对象（LeaveBill.1）
        String id = "";
        if (StringUtils.isNotBlank(buniness_key)) {
            //截取字符串，取buniness_key小数点的第2个值
            id = buniness_key.split("\\.")[1];
        }
        //查询业务数据对象
        //使用hql语句：from LeaveBill o where o.id=1
//		LeaveBill leaveBill = leaveBillDao.findLeaveBillById(Long.parseLong(id));
        String sql = "select * from act_atest_table where id=?";
//        Map map = Jdbc.findMap(sql, new Object[]{id});
        Map map = new HashMap();
        return map;
    }

    public ProcessInstance findProcessInstance(String processInstanceId) {
        ProcessInstance pi = this.processEngine.getRuntimeService().createProcessInstanceQuery()//
                .processInstanceId(processInstanceId)//使用流程实例ID查询
                .singleResult();
        return pi;
    }


    /**
     * 使用请假单ID，查询历史批注信息
     */
    public List<Comment> findCommentByLeaveBillId(String id) {
        //使用请假单ID，查询请假单对象
        //获取对象的名称
        //组织流程表中的字段中的值
        String objId = "testProcess." + id;

        /**1:使用历史的流程实例查询，返回历史的流程实例对象，获取流程实例ID*/
//		HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()//对应历史的流程实例表
//						.processInstanceBusinessKey(objId)//使用BusinessKey字段查询
//						.singleResult();
//		//流程实例ID
//		String processInstanceId = hpi.getId();
        /**2:使用历史的流程变量查询，返回历史的流程变量的对象，获取流程实例ID*/
        HistoricVariableInstance hvi = this.processEngine.getHistoryService().createHistoricVariableInstanceQuery()//对应历史的流程变量表
                .variableValueEquals("objId", objId)//使用流程变量的名称和流程变量的值查询
                .singleResult();
        //流程实例ID
        String processInstanceId = hvi.getProcessInstanceId();
        List<Comment> list = this.processEngine.getTaskService().getProcessInstanceComments(processInstanceId);
        return list;
    }

    public Task findTaskByVariableValue(String value) {
        //使用任务ID，查询任务对象
        Task task = processEngine.getTaskService().createTaskQuery()
                .processVariableValueEquals("objId", value)
                .singleResult();
        return task;
    }

    public List<Task> findTaskListByVariableValue(String value) {
        return processEngine.getTaskService().createTaskQuery()
                .processVariableValueEquals("objId", value)
                .list();
    }

    public List<Task> findTaskListByProcessInstanceId(String processInstanceId) {
        return processEngine.getTaskService().createTaskQuery()
                .processInstanceId(processInstanceId)
                .list();
    }

    public Collection<FlowElement> findAllElement(String key) {
        //为流程定义Id，该Id可以通过多种方式获得，如通过ProcessDefinitionQuery可以查询一个ProcessDefinition对象，Task对象中也包含
        ProcessDefinition pd =
                processEngine.getRepositoryService()
                        .createProcessDefinitionQuery()
                        .processDefinitionKey(key)
                        .latestVersion()
                        .singleResult();
//		ProcessInstance pi = 	
//				processEngine.getRuntimeService()
//				.createProcessInstanceQuery()
//				.processInstanceBusinessKey(key)
//				.orderByProcessInstanceId()
//				.desc()
//				.singleResult();
//		String id = pi.getId();
        String id = pd.getId();
        RepositoryService rs = processEngine.getRepositoryService();
        BpmnModel bm = rs.getBpmnModel(id);
        Collection<FlowElement> fes = bm.getMainProcess().getFlowElements();
//		for (FlowElement fe : fes) {
//			System.out.print("------");
//			System.out.println(fe.getId()+"|"+fe.getName());
//		}
        return fes;
    }
}
