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
public class VoucherControllerActlog extends ActionLogAopConfiguration{

		@Autowired
		ThreadPoolTaskExecutor threadPoolTaskExecutor;

		@Autowired
		ActionLogService actionLogService;

		@Pointcut("execution(* cn.ahyc.yjz.controller.VoucherController.*(..))")
		public void controllerPointCut() {}

		//-------------------------------------------------------------------------------------------------------->
		@Before("controllerPointCut()")
		public void before(JoinPoint point) {
				String method = method(point);
				ActionLog actionLog = null;
				switch (method){
						case("voucher"):
								actionLog = actionLog("打开记账页面","开始","",point);
								break;
						case("voucherDetailList"):
								actionLog = actionLog("查看凭证明细列表","开始","",point);
								break;
						case("save"):
								actionLog = actionLog("保存凭证","开始","",point);
								break;
						case("help"):
								actionLog = actionLog("查看凭证制作说明","开始","",point);
								break;
						case("subjectBalance"):
								actionLog = actionLog("打开记账-科目余额页面","开始","",point);
								break;
						case("voucherTemplate"):
								actionLog = actionLog("打开模式凭证列表页面","开始","",point);
								break;
						case("voucherTemplateList"):
								actionLog = actionLog("查看模式凭证列表","开始","",point);
								break;
						case("voucherTemplateAdd"):
								actionLog = actionLog("打开模式凭证新增/编辑页面","开始","",point);
								break;
						case("voucherTemplateSave"):
								actionLog = actionLog("打开模式凭证保存页面","开始","",point);
								break;
						case("voucherTemplateDetailList"):
								actionLog = actionLog("查看模式凭证明细列表","开始","",point);
								break;
						case("deleteTemplate"):
								actionLog = actionLog("删除模式凭证","开始","",point);
								break;
						case("saveTemplate"):
								actionLog = actionLog("保存模式凭证","开始","",point);
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
						case("voucher"):
								actionLog = actionLog("打开记账页面","结束","",point);
								break;
						case("voucherDetailList"):
								actionLog = actionLog("查看凭证明细列表","结束","",point);
								break;
						case("save"):
								actionLog = actionLog("保存凭证","结束","",point);
								break;
						case("help"):
								actionLog = actionLog("查看凭证制作说明","结束","",point);
								break;
						case("subjectBalance"):
								actionLog = actionLog("打开记账-科目余额页面","结束","",point);
								break;
						case("voucherTemplate"):
								actionLog = actionLog("打开模式凭证列表页面","结束","",point);
								break;
						case("voucherTemplateList"):
								actionLog = actionLog("查看模式凭证列表","结束","",point);
								break;
						case("voucherTemplateAdd"):
								actionLog = actionLog("打开模式凭证新增/编辑页面","结束","",point);
								break;
						case("voucherTemplateSave"):
								actionLog = actionLog("打开模式凭证保存页面","结束","",point);
								break;
						case("voucherTemplateDetailList"):
								actionLog = actionLog("查看模式凭证明细列表","结束","",point);
								break;
						case("deleteTemplate"):
								actionLog = actionLog("删除模式凭证","结束","",point);
								break;
						case("saveTemplate"):
								actionLog = actionLog("保存模式凭证","结束","",point);
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

