package priv.jv.proxy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;

@ConfigurationProperties(prefix = "netty")
@Configuration
@Data
public class RoutesConfig {

    private List<Route> routes;

    @Data
    public static class Route {
        private String id;
        private String host;
        private int port;
        private Set<String> match_hosts;
    }
}
