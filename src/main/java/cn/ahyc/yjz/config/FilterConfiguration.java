package cn.ahyc.yjz.config;

import cn.ahyc.yjz.filter.ProcessTimeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * FilterConfiguration
 *
 * @author:sanlai_lee@qq.com
 * @date: 15/9/24
 */
@Configuration
@Profile("development")
public class FilterConfiguration {

      @Autowired
      private ProcessTimeFilter processTimeFilter;

      /**
       * 添加拦截器调试每个请求的耗时.
       * @return
       */
      @Bean
      public FilterRegistrationBean processTimeFilterRegistrationBean() {
            FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
            filterRegistrationBean.setFilter(processTimeFilter);
            filterRegistrationBean.setEnabled(true);
            filterRegistrationBean.addUrlPatterns("/*");
            return filterRegistrationBean;
      }

}
