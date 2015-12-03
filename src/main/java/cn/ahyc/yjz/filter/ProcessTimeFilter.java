package cn.ahyc.yjz.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * ProcessTimeFilter
 *
 * @author:sanlai_lee@qq.com
 * @date: 15/9/24
 */
@Component
public class ProcessTimeFilter implements Filter {

      protected final Logger log = LoggerFactory.getLogger(ProcessTimeFilter.class);

      /**
       * 请求执行开始时间
       */
      public static final String START_TIME = "_start_time";


      @Override
      public void init(FilterConfig filterConfig) throws ServletException {

      }

      @Override
      public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
              throws IOException, ServletException {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            long time = System.currentTimeMillis();
            request.setAttribute(START_TIME, time);
            filterChain.doFilter(request, servletResponse);
            time = System.currentTimeMillis() - time;
            if(time>5000){
                  log.warn("Low performance - process in {} ms: {}", time, request.getRequestURI());
            }else{
                  log.debug("Normal performance - process in {} ms: {}", time, request.getRequestURI());
            }
      }

      @Override
      public void destroy() {

      }
}
