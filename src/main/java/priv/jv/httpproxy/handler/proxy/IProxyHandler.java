package priv.jv.httpproxy.handler.proxy;

import io.netty.channel.ChannelHandlerContext;
import priv.jv.httpproxy.bean.ClientRequest;

/**
 * 代理handler
 *
 * @author puhaiyang
 * created on 2019/10/25 23:07
 */
public interface IProxyHandler {
    /**
     * 发送到server
     *
     * @param clientRequest 客户端请求
     * @param ctx           ChannelHandlerContext
     * @param msg           数据
     */
    void sendToServer(ClientRequest clientRequest, final ChannelHandlerContext ctx, final Object msg);

    /**
     * 发送到client
     */
    void sendToClient(ClientRequest clientRequest, final ChannelHandlerContext ctx, final Object msg);
}
