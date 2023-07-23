package priv.jv.httpproxy.proxy.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import priv.jv.httpproxy.proxy.handler.bean.ClientRequest;
import priv.jv.httpproxy.proxy.handler.response.SocksResponseHandler;

import static priv.jv.httpproxy.Constans.CLIENTREQUEST_ATTRIBUTE_KEY;


/**
 * socks的代理handler
 *
 * @author puhaiyang
 * created on 2019/10/25 20:56
 */
public class SocksProxyHandler extends ChannelInboundHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(SocksProxyHandler.class);

    private ChannelFuture socketChannelFuture;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            super.channelRead(ctx, msg);
        }
        logger.debug("[SocksProxyHandler]");
        Attribute<ClientRequest> clientRequestAttribute = ctx.channel().attr(CLIENTREQUEST_ATTRIBUTE_KEY);
        ClientRequest clientRequest = clientRequestAttribute.get();
        sendToServer(clientRequest, ctx, msg);
    }

    public void sendToServer(ClientRequest clientRequest, ChannelHandlerContext ctx, Object msg) {
        //不是http请求就不管，全转发出去
        if (socketChannelFuture == null) {
            //连接至目标服务器
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(ctx.channel().eventLoop())
                    // 复用客户端连接线程池
                    .channel(ctx.channel().getClass())
                    // 使用NioSocketChannel来作为连接用的channel类
                    .handler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new SocksResponseHandler(ctx.channel()));
                        }
                    });
            socketChannelFuture =
                    bootstrap
                            .connect(clientRequest.getHost(), clientRequest.getPort())
                            .addListener(new ChannelFutureListener() {
                                @Override
                                public void operationComplete(ChannelFuture future) throws Exception {
                                    if (future.isSuccess()) {
                                        future.channel().writeAndFlush(msg);
                                    } else {
                                        ctx.channel().close();
                                    }
                                }
                            });
        } else {
            socketChannelFuture.channel().writeAndFlush(msg);
        }
    }
}
