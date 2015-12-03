package cn.ahyc.yjz.aop;
/**
 * ActionLogAopConfiguration
 *
 * @author:sanlai_lee@qq.com
 * @date: 15/10/16
 */

import cn.ahyc.yjz.model.ActionLog;
import org.aspectj.lang.JoinPoint;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Date;

/**
 * Created by sanlli on 15/10/16.
 */
public abstract class ActionLogAopConfiguration {


		/**
		 * 组装ActionLog对象.
		 * @param action
		 * @param states
		 * @param description
		 * @param point
		 * @return
		 */
		ActionLog actionLog(String action, String states,String description, JoinPoint point){
				String operator = principal();
				ActionLog actionLog = new ActionLog(0L,action,states,description,operator,remoteAddress(),null,null,new Date(),methodFullName(point));
				return actionLog;
		}

		/**
		 * 从SecurityContextHolder里面获取username.
		 * @return
		 */
		String principal(){
				String operator = null;
				Authentication authentication = SecurityContextHolder.getContext()==null?null:SecurityContextHolder.getContext().getAuthentication();
				operator = authentication==null?operator:((UserDetails)authentication.getPrincipal()).getUsername();
				return operator;
		}

		/**
		 * 从SecurityContextHolder里面获取IP.
		 * @return
		 */
		String remoteAddress(){
				String address = null;
				Authentication authentication = SecurityContextHolder.getContext()==null?null:SecurityContextHolder.getContext().getAuthentication();
				address = authentication==null?address:((WebAuthenticationDetails)authentication.getDetails()).getRemoteAddress();
				return address;
		}

		/**
		 * 获取切点的方法名.
		 * @param point
		 * @return
		 */
		String method(JoinPoint point){
				return point.getSignature().getName();
		}

		/**
		 * 获取切点的目标类名+方法名.
		 *
		 * @param point
		 * @return
		 */
		String methodFullName(JoinPoint point) {
				return point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName();
		}

}
