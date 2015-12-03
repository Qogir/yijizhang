package cn.ahyc.yjz.thread;
/**
 * WriteAcationLogThread
 *
 * @author:sanlai_lee@qq.com
 * @date: 15/10/16
 */

import cn.ahyc.yjz.model.ActionLog;
import cn.ahyc.yjz.service.ActionLogService;

/**
 * Created by sanlli on 15/10/16.
 */
public class WriteAcationLogThread implements Runnable{

		private ActionLogService actionLogService;

		private ActionLog actionLog;

		public ActionLog getActionLog() {
				return actionLog;
		}

		public void setActionLog(ActionLog actionLog) {
				this.actionLog = actionLog;
		}

		public ActionLogService getActionLogService() {
				return actionLogService;
		}

		public void setActionLogService(ActionLogService actionLogService) {
				this.actionLogService = actionLogService;
		}

		public WriteAcationLogThread() {
		}

		public WriteAcationLogThread(ActionLogService actionLogService, ActionLog actionLog) {
				this.actionLogService = actionLogService;
				this.actionLog = actionLog;
		}

		/**
		 * When an object implementing interface <code>Runnable</code> is used
		 * to create a thread, starting the thread causes the object's
		 * <code>run</code> method to be called in that separately executing
		 * thread.
		 * <p>
		 * The general contract of the method <code>run</code> is that it may
		 * take any action whatsoever.
		 *
		 * @see Thread#run()
		 */
		@Override
		public void run() {
				actionLogService.writeActionLog(actionLog);
		}

}
