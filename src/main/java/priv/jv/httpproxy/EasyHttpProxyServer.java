package priv.jv.httpproxy;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.NettyRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import priv.jv.httpproxy.proxy.handler.HttpProxyClientHandler;
import priv.jv.httpproxy.proxy.handler.SocksProxyHandler;

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
public class EasyHttpProxyServer {
    private static Logger logger = LoggerFactory.getLogger(EasyHttpProxyServer.class);

    int port;

    public EasyHttpProxyServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        printWelcome();
        int port = 8888;
        if (args.length > 0) {
            port = Integer.valueOf(args[0]);
        }
        new EasyHttpProxyServer(port).run();
        logger.info("proxy server start on {} port", port);
    }

    private static void printWelcome() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>> starting proxy server <<<<<<<<<<<<<<<<<<<<<<");
        System.out.println(MessageFormat.format(">>>>>>>>>>>>>>>>>>>>>>>>>> available process: {0} <<<<<<<<<<<<<<<<<<<<<<",
                Constans.AVAILABLE_PROCESS));
        System.out.println();
    }

    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(Constans.AVAILABLE_PROCESS);
        EventLoopGroup workerGroup = new NioEventLoopGroup(NettyRuntime.availableProcessors() * 5);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new LoggingHandler(LogLevel.WARN),
                                    new HttpProxyClientHandler(),
                                    new SocksProxyHandler());
                        }
                    })
                    .bind(port)
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
