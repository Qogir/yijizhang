package cn.ahyc.yjz.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;

import javax.servlet.DispatcherType;
import java.util.HashMap;
import java.util.Map;

/**
 * FilterConfiguration
 *
 * @author:sanlai_lee@qq.com
 * @date: 15/9/24
 */
@Configuration
@Profile("production")
public class UrlRewriteFilterConfiguration {

      @Value("${urlRewrite.confReloadCheckInterval}")
      private String confReloadCheckInterval;

      @Value("${urlRewrite.confPath}")
      private String confPath;

      @Value("${urlRewrite.logLevel}")
      private String logLevel;

      /* UrlRewrite 过滤器配置 */

      /**
       * urlRewriteFilter
       *
       * @return
       */
      @Bean
      public UrlRewriteFilter urlRewriteFilter() {
            return new UrlRewriteFilter();
      }

      /**
       * urlRewriteInitParameters
       * <p>
       * <p>参考：http://cdn.rawgit.com/paultuckey/urlrewritefilter/master/src/doc/manual/4.0/index.html</p>
       *
       * @return
       */
      @Bean
      public Map urlRewriteInitParameters() {
            Map map = new HashMap();
            map.put("confReloadCheckInterval", confReloadCheckInterval);
            map.put("confPath", confPath);
            map.put("logLevel", logLevel);
            return map;
      }

      /**
       * urlRewriteFilterRegistrationBean
       *
       * @return
       */
      @Bean
      public FilterRegistrationBean urlRewriteFilterRegistrationBean() {
            FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
            filterRegistrationBean.setFilter(urlRewriteFilter());
            filterRegistrationBean.setEnabled(true);
            filterRegistrationBean.setInitParameters(urlRewriteInitParameters());
            filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD);
            filterRegistrationBean.addUrlPatterns("/*");
            return filterRegistrationBean;
      }
}
