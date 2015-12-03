package cn.ahyc.yjz.aop;
/**
 * AccountBookControllerActlog
 *
 * @author:sanlai_lee@qq.com
 * @date: 15/10/16
 */

import cn.ahyc.yjz.model.ActionLog;
import cn.ahyc.yjz.service.ActionLogService;
import cn.ahyc.yjz.thread.WriteAcationLogThread;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by sanlli on 15/10/16.
 */
@Aspect
@Configuration
public class AccountSubjectControllerActlog extends ActionLogAopConfiguration{

		@Autowired
		ThreadPoolTaskExecutor threadPoolTaskExecutor;

		@Autowired
		ActionLogService actionLogService;

		@Pointcut("execution(* cn.ahyc.yjz.controller.AccountSubjectController.*(..))")
		public void controllerPointCut() {}

		//-------------------------------------------------------------------------------------------------------->
		@Before("controllerPointCut()")
		public void before(JoinPoint point) {
				String method = method(point);
				ActionLog actionLog = null;
				switch (method){
						case("balancePage"):
								actionLog = actionLog("试算平衡","开始","",point);
								break;
						case("calculate"):
								actionLog = actionLog("汇总","开始","",point);
								break;
						case("initDataEdit"):
								actionLog = actionLog("修改初始化数据","开始","",point);
								break;
						case("initDataPage"):
								actionLog = actionLog("打开初始化数据页面","开始","",point);
								break;
						case("delete"):
								actionLog = actionLog("删除会计科目","开始","",point);
								break;
						case("tip"):
								actionLog = actionLog("查看科目说明","开始","",point);
								break;
						case("main"):
								actionLog = actionLog("打开会计科目管理页面","开始","",point);
								break;
						case("editPage"):
								actionLog = actionLog("打开新增/修改会计科目页面","开始","",point);
								break;
						default:
								break;
				}

				//如果actionLog不为null，执行线程写操作日志.
				if(actionLog!=null){
						threadPoolTaskExecutor.execute(new WriteAcationLogThread(actionLogService,actionLog));
				}
		}


		@After("controllerPointCut()")
		public void after(JoinPoint point) {
				String method = method(point);
				ActionLog actionLog = null;
				switch (method) {
						case ("balancePage"):
								actionLog = actionLog("试算平衡", "结束", "", point);
								break;
						case ("calculate"):
								actionLog = actionLog("汇总", "结束", "", point);
								break;
						case ("initDataEdit"):
								actionLog = actionLog("修改初始化数据", "结束", "", point);
								break;
						case ("initDataPage"):
								actionLog = actionLog("打开初始化数据页面", "结束", "", point);
								break;
						case ("delete"):
								actionLog = actionLog("删除会计科目", "结束", "", point);
								break;
						case ("tip"):
								actionLog = actionLog("查看科目说明", "结束", "", point);
								break;
						case ("main"):
								actionLog = actionLog("打开会计科目管理页面", "结束", "", point);
								break;
						case ("editPage"):
								actionLog = actionLog("打开新增/修改会计科目页面", "结束", "", point);
								break;
						default:
								break;
				}
				//如果actionLog不为null，执行线程写操作日志.
				if (actionLog != null) {
						threadPoolTaskExecutor.execute(new WriteAcationLogThread(actionLogService, actionLog));
				}
		}
}

