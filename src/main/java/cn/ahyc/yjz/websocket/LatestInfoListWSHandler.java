/**
 * LatestBalanceWSHandler
 *
 * @author: sanlai_lee@qq.com
 * @date: 15/10/21
 */
package cn.ahyc.yjz.websocket;

import cn.ahyc.yjz.service.SubjectBalanceService;
import cn.ahyc.yjz.service.VoucherService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

/**
 * Created by sanlli on 15/10/21.
 */
public class LatestInfoListWSHandler extends TextWebSocketHandler {

		private static Logger logger = LoggerFactory.getLogger(LatestInfoListWSHandler.class);

		private static final int BALANCE=1;
		private static final int VOUCHER=2;

		@Autowired
		private SubjectBalanceService subjectBalanceService;

		@Autowired
		private VoucherService voucherService;

		@Override
		public void afterConnectionEstablished(WebSocketSession session) throws Exception {
				logger.debug("Opened new session in instance " + this);
		}

		@Override
		public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
				logger.debug("WebSocket received a message : " + message.getPayload());
				if(message.getPayload()==null){
						return;
				}
				Map map = JSON.parseObject(message.getPayload().toString(),Map.class);
				if(map==null||map.get("ready")==null||!(boolean)map.get("ready")){
						return;
				}

				logger.debug("WebSocket is ready now.Start pushing data to browser.");
				Integer type = (Integer)map.get("type");
				Map m = new HashMap();
				List<Map> list = type==BALANCE?subjectBalanceService.selectLatestBalance(map):
							type==VOUCHER?voucherService.latestVouchers(map):new ArrayList(0);
				m.put(type==1?"latestBalance":"latestVoucher",list);
				session.sendMessage(new TextMessage(JSON.toJSONString(m)));
		}

		@Override
		public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
				session.close(CloseStatus.SERVER_ERROR);
				logger.error("Transport error in instance " + this);
		}

		@Override
		public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
				logger.debug("Closed session in instance " + this);
		}
}
