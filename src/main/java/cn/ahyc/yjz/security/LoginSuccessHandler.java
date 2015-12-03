package cn.ahyc.yjz.security;
/**
 * CustomAuthenticationSuccessHandler
 *
 * @author:sanlai_lee@qq.com
 * @date: 15/9/28
 */

import cn.ahyc.yjz.model.AccountBook;
import cn.ahyc.yjz.model.LoginHistory;
import cn.ahyc.yjz.service.AccountBookService;
import cn.ahyc.yjz.service.LoginHistoryService;
import cn.ahyc.yjz.service.PeriodService;
import cn.ahyc.yjz.util.Constant;
import cn.ahyc.yjz.util.NetworkUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created by sanlli on 15/9/28.
 */
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

		private LoginHistoryService loginHistoryService;

		private AccountBookService accountBookService;

		private PeriodService periodService;

		public LoginSuccessHandler() {
		}

		@Override
		public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
				//先读取上次登录成功的历史
				String username = ((User)authentication.getPrincipal()).getUsername();
				LoginHistory lastLoginHistory = loginHistoryService.selectLastSuccessLoginHistory(username);
				//获取上次操作的账套ID
				Long accountBookId = lastLoginHistory==null?0:lastLoginHistory.getAccountBookId();
				if(accountBookId<=0){
						AccountBook accountBook = accountBookService.selectLatestAccountBook();
						accountBookId = accountBook==null?accountBookId:accountBook.getId();
				}
				//把上次操作的账套ID放到Session里面，登录的时候，默认操作的账套ID是上次操作的账套
				AccountBook accountBook = accountBookService.selectAccountBookById(accountBookId);
				if(accountBook!=null){
						request.getSession().setAttribute(Constant.CURRENT_ACCOUNT_BOOK,accountBook);
						request.getSession().setAttribute(Constant.CURRENT_YEAR, accountBook.getInitYear()+"年");
						request.getSession().setAttribute(Constant.CURRENT_PERIOD, periodService.selectCurrentPeriod(accountBookId));
				}

				//保存本次的登录历史
				LoginHistory loginHistory = new LoginHistory();
				loginHistory.setUsername(username);
				loginHistory.setAccountBookId(accountBookId);
				loginHistory.setLoginTime(new Date());
				loginHistory.setLoginResult(true);
				loginHistory.setLoginIp(NetworkUtil.getRemoteHost(request));
				loginHistoryService.saveLoginHistory(loginHistory);

				super.onAuthenticationSuccess(request, response, authentication);
		}


		public void setLoginHistoryService(LoginHistoryService loginHistoryService) {
				this.loginHistoryService = loginHistoryService;
		}

		public void setAccountBookService(AccountBookService accountBookService) {
				this.accountBookService = accountBookService;
		}

		public void setPeriodService(PeriodService periodService) {
				this.periodService = periodService;
		}
}
