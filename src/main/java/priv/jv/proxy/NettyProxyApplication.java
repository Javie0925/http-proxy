package priv.jv.proxy;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import priv.jv.proxy.server.NettyProxyServer;

@SpringBootApplication(exclude = {JmxAutoConfiguration.class})
public class NettyProxyApplication {



    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(NettyProxyApplication.class, args);
        System.out.println();
        context.getBean(NettyProxyServer.class).start();
    }
}
