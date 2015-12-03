package cn.ahyc.yjz.config;
/**
 * SecurityConfiguration
 *
 * @author:sanlai_lee@qq.com
 * @date: 15/9/25
 */

import cn.ahyc.yjz.security.LoginSuccessHandler;
import cn.ahyc.yjz.service.AccountBookService;
import cn.ahyc.yjz.service.LoginHistoryService;
import cn.ahyc.yjz.service.PeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import javax.sql.DataSource;

/**
 * Created by sanlli on 15/9/25.
 */
@Configuration
@EnableWebMvcSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

		@Autowired
		private DataSource dataSource;

		@Autowired
		private LoginHistoryService loginHistoryService;

		@Autowired
		private AccountBookService accountBookService;

		@Autowired
		private PeriodService periodService;

		@Bean
		public LoginSuccessHandler loginSuccessHandler(){
				LoginSuccessHandler loginSuccessHandler = new LoginSuccessHandler();
				loginSuccessHandler.setLoginHistoryService(loginHistoryService);
				loginSuccessHandler.setAccountBookService(accountBookService);
				loginSuccessHandler.setPeriodService(periodService);
				return loginSuccessHandler;
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
				http.csrf()
							.disable()
							.headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
							.and()
							.authorizeRequests().antMatchers("/static/**","/public/**","/app/**","**/login.js","**/favicon.ico","/about").permitAll()
							.anyRequest().fullyAuthenticated()
							.and().formLogin()
							.defaultSuccessUrl("/").successHandler(loginSuccessHandler())
							.failureUrl("/login?error")
							.loginPage("/login").permitAll()
							.and().logout().logoutSuccessUrl("/login?logout").invalidateHttpSession(true).permitAll()
							.and().sessionManagement().maximumSessions(10).expiredUrl("/login?expired");
		}

		@Bean
		public PasswordEncoder passwordEncoder(){
				return new StandardPasswordEncoder();
		}

		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
				auth.jdbcAuthentication().passwordEncoder(passwordEncoder()).dataSource(this.dataSource);
		}

}