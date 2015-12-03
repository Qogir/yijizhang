/**
 * WebsocketConfiguration
 *
 * @author: sanlai_lee@qq.com
 * @date: 15/10/21
 */
package cn.ahyc.yjz.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Created by sanlli on 15/10/21.
 */
@Configuration
@EnableWebSocket
public class WebsocketConfiguration implements WebSocketConfigurer {

		@Override
		public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
				//最新余额以及最新凭证
				registry.addHandler(latestInfoListWSHandler(), "/latest/info/list").withSockJS().setSessionCookieNeeded(false);
		}

		@Bean
		@Autowired
		public WebSocketHandler latestInfoListWSHandler(){
				return new LatestInfoListWSHandler();
		}

}
