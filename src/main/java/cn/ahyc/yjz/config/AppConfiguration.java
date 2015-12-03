package cn.ahyc.yjz.config;
/**
 * AppConfiguration
 *
 * @author:sanlai_lee@qq.com
 * @date: 15/10/14
 */

import cn.ahyc.yjz.dto.BuildInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by sanlli on 15/10/14.
 */
@Configuration
public class AppConfiguration {


		@Value("${build.name}")
		private String buildName;

		@Value("${build.version}")
		private String buildVersion;

		@Value("${build.date}")
		private String buildDate;

		/**
		 * ThreadPoolTaskExecutor.
		 * @return
		 */
		@Bean
		public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
				ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
				poolTaskExecutor.setQueueCapacity(10000);
				poolTaskExecutor.setCorePoolSize(5);
				poolTaskExecutor.setMaxPoolSize(100);
				poolTaskExecutor.setKeepAliveSeconds(5000);
				poolTaskExecutor.initialize();
				return  poolTaskExecutor;
		}

		/**
		 * 构建信息.
		 * @return
		 */
		@Bean
		public BuildInfo buildInfo(){
				try {
						buildName = buildName==null?"安徽新华社财务记账系统":StringUtils.toEncodedString((buildName.getBytes("ISO-8859-1")), Charset.forName("utf-8"));
				} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
				}
				return new BuildInfo(buildName,buildVersion,buildDate);
		}

}
