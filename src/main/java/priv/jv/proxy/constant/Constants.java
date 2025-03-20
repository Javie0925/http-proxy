package priv.jv.proxy.constant;

import io.netty.util.NettyRuntime;

/**
 * 常量
 */
public interface Constants {

    int AVAILABLE_PROCESS = NettyRuntime.availableProcessors();

    String CLIENT_HOST = "";
    int CLIENT_PORT = 9876;

}
