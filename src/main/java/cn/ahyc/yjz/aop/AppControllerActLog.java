package cn.ahyc.yjz.aop;
/**
 * AopConfiguration
 *
 * @author:sanlai_lee@qq.com
 * @date: 15/10/12
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Date;

/**
 * Created by sanlli on 15/10/12.
 */
@Aspect
@Configuration
public class AppControllerActLog extends ActionLogAopConfiguration{

		@Autowired
		ThreadPoolTaskExecutor threadPoolTaskExecutor;

		@Autowired
		ActionLogService actionLogService;


		/**
		 * AppController切点.
		 */
		@Pointcut("execution(* cn.ahyc.yjz.controller.AppController.*(..))")
		public void controllerPointCut() {}

		@Before("controllerPointCut()")
		public void before(JoinPoint point) {
				String method = method(point);
				ActionLog actionLog = null;
				switch (method){
						case("dashboard"):
								actionLog = actionLog("打开首页","开始","",point);
								break;
						case("savePassWd"):
								actionLog = actionLog("修改密码","开始","",point);
								break;
						case("logout"):
								actionLog = actionLog("注销","开始","",point);
								break;
						case("switchAccoutBook"):
								actionLog = actionLog("切换账套","开始","",point);
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
						case("dashboard"):
								actionLog = actionLog("打开首页","结束","",point);
								break;
						case("savePassWd"):
								actionLog = actionLog("修改密码","结束","",point);
								break;
						case("logout"):
								actionLog = actionLog("注销","结束","",point);
								break;
						case("switchAccoutBook"):
								actionLog = actionLog("切换账套","结束","",point);
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
