package cn.ahyc.yjz.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import javax.sql.DataSource;

import cn.ahyc.yjz.Application;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.*;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import cn.ahyc.yjz.util.PasswordUtil;
import org.springframework.util.ResourceUtils;

/**
 * MyBatisConfiguration
 *
 * @author sanlai_lee@qq.com
 */
@Configuration
@MapperScan(basePackages = {"cn.ahyc.yjz.mapper"})
public class MyBatisConfiguration {

      private static final Logger LOGGER = LoggerFactory.getLogger(MyBatisConfiguration.class);


      @Value("${mybatis.configLocation}")
      private String configLocation;

      @Value("${mybatis.mapperLocations}")
      private String mapperLocations;

      @Autowired
      private DataSourceProperties dataSourceProperties;

      @Autowired
      private ApplicationContext applicationContext;

      /**
       * DataSource
       * @return
       */
      @Bean
      public DataSource dataSource(){
            DataSource dataSource = DataSourceBuilder
                    .create()
                    .driverClassName(this.dataSourceProperties.getDriverClassName())
                    .url(this.dataSourceProperties.getUrl())
                    .username(PasswordUtil.decrypt(this.dataSourceProperties.getUsername()))
                    .password(PasswordUtil.decrypt(this.dataSourceProperties.getPassword()))
                          .build();
            return  dataSource;
      }

      /**
       * DataSourceTransactionManager.
       *
       * @return
       */
      @Bean
      public DataSourceTransactionManager transactionManager() {
            LOGGER.info("Initialize DataSourceTransactionManager with datasource '{}'", dataSource());
            return new DataSourceTransactionManager(dataSource());
      }


      /**
       * SqlSessionFactoryBean.
       *
       * @return
       */
      @Bean
      public SqlSessionFactory sqlSessionFactory(){
            LOGGER.info("Initialize SqlSessionFactory...mapperLocations={}", this.mapperLocations);
            SqlSessionFactory sqlSessionFactory = null;
            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            try {
                  //设置数据源 datasource
                  DataSource dataSource = dataSource();
                  sqlSessionFactoryBean.setDataSource(dataSource);

                  //设置 configLocation
                  LOGGER.info("Read mybatis config location from config file is '{}'", configLocation);
                  configLocation = StringUtils.isBlank(configLocation) ? "mybatis/mybatis-config.xml" : configLocation;

                  Resource resource = new ClassPathResource(configLocation);
                  if(!resource.exists()){
                        throw new Exception("Can not found mybatis-config.xml at '"+configLocation+"'");
                  }else{
                        sqlSessionFactoryBean.setConfigLocation(resource);
                  }

                  //设置 mapperLocations
                  mapperLocations = StringUtils.isBlank(mapperLocations) ? "mybatis/mappers/**/*.xml" : mapperLocations;
                  PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
                  Resource[]  mapperLocationsRes = pathMatchingResourcePatternResolver.getResources(mapperLocations);
                  if (mapperLocationsRes!=null && mapperLocationsRes.length > 0) {
                        sqlSessionFactoryBean.setMapperLocations(mapperLocationsRes);
                  } else {
                        LOGGER.warn("Set mapper locations failed. Can not found any file in path '{}'", mapperLocations);
                  }
                  sqlSessionFactory = sqlSessionFactoryBean.getObject();
            } catch (Exception e) {
                  LOGGER.error("Failed to stat up system when initializing sqlSessionFactory. Caused by: {}",e.getMessage());
                  e.printStackTrace();
                  System.exit(1);
            }
            return sqlSessionFactory;
      }

      /**
       * SqlSessionTemplate.
       *
       * @return
       */
      @Bean
      public SqlSessionTemplate sqlSessionTemplate() {
            SqlSessionFactory sqlSessionFactory = sqlSessionFactory();
            LOGGER.info("Initialize SqlSessionTemplate bean with sqlSessionFactory '{}'", sqlSessionFactory);
            //默认采用Batch方式提交事务
            //SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
            // 默认采用REUSE方式提交事务
            SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory, ExecutorType.REUSE);
            return sqlSessionTemplate;
      }
}
