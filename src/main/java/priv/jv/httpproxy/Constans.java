package priv.jv.httpproxy;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.AttributeKey;
import io.netty.util.NettyRuntime;
import priv.jv.httpproxy.proxy.handler.bean.ClientRequest;

/**
 * 常量
 *
 * @author puhaiyang
 * created on 2019/10/25 22:51
 */
public interface Constans {
    /**
     * connect类型请求名称
     */
    String CONNECT_METHOD_NAME = "CONNECT";
    /**
     * https连接协议名
     */
    String HTTPS_PROTOCOL_NAME = "https";
    /**
     * 代理握手成功响应status
     */
    HttpResponseStatus CONNECT_SUCCESS = new HttpResponseStatus(200, "Connection established");
    /**
     * channel中的clientReuqest
     */
    AttributeKey<ClientRequest> CLIENTREQUEST_ATTRIBUTE_KEY = AttributeKey.valueOf("clientRequest");

    int AVAILABLE_PROCESS = NettyRuntime.availableProcessors();


}
