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
public class CashierControllerActlog extends ActionLogAopConfiguration{

		@Autowired
		ThreadPoolTaskExecutor threadPoolTaskExecutor;

		@Autowired
		ActionLogService actionLogService;

		@Pointcut("execution(* cn.ahyc.yjz.controller.CashierController.*(..))")
		public void controllerPointCut() {}

		//-------------------------------------------------------------------------------------------------------->
		@Before("controllerPointCut()")
		public void before(JoinPoint point) {
				String method = method(point);
				ActionLog actionLog = null;
				switch (method){
						case("main"):
								actionLog = actionLog("打开结账页面","开始","",point);
								break;
						case("cashierSubmit"):
								actionLog = actionLog("结账","开始","",point);
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
				switch (method){
						case("main"):
								actionLog = actionLog("打开结账页面","结束","",point);
								break;
						case("cashierSubmit"):
								actionLog = actionLog("结账","结束","",point);
								break;
						default:
								break;
				}
				//如果actionLog不为null，执行线程写操作日志.
				if(actionLog!=null){
						threadPoolTaskExecutor.execute(new WriteAcationLogThread(actionLogService,actionLog));
				}
		}

}

