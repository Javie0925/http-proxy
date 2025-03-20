package priv.jv.proxy.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.NettyRuntime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import priv.jv.proxy.constant.Constants;
import priv.jv.proxy.handler.http.HttpProxyClientHandler;
import priv.jv.proxy.handler.socks.SocksServerInitializer;

import java.text.MessageFormat;


/**
 * HTTP/HTTPS代理服务器
 * 三个角色：真实客户端，代理客户端，目标主机
 * <p>
 * 数据流向：
 * <p>
 * ------->> 代理客户端  --------->>
 * 真实客户端                                  目标主机
 * <<-------- 代理客户端  <<--------
 */

@Service
@Slf4j
public class NettyProxyServer {

    @Value("${netty.port:}")
    private int port;

    private static void printWelcome() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>> starting proxy server <<<<<<<<<<<<<<<<<<<<<<");
        System.out.println(MessageFormat.format(">>>>>>>>>>>>>>>>>>>>>>>>>> available process: {0} <<<<<<<<<<<<<<<<<<<<<<",
                Constants.AVAILABLE_PROCESS));
        System.out.println();
    }

    public void start() {
        printWelcome();
        EventLoopGroup bossGroup = new NioEventLoopGroup(Constants.AVAILABLE_PROCESS);
        EventLoopGroup workerGroup = new NioEventLoopGroup(NettyRuntime.availableProcessors() * 5);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(
                                    new HttpProxyClientHandler(),
                                    new SocksServerInitializer());
                        }
                    })
                    .bind(port)
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
