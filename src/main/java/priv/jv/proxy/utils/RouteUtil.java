package priv.jv.proxy.utils;

import org.springframework.util.CollectionUtils;
import priv.jv.proxy.config.RoutesConfig;

import java.util.List;
import java.util.Objects;

public class RouteUtil {

    public static String routeHost(String targetHost) {
        String resultHost = targetHost;
        RoutesConfig routesProperty = ApplicationContextUtil.getBean(RoutesConfig.class);
        List<RoutesConfig.Route> routes = routesProperty.getRoutes();
        if (!CollectionUtils.isEmpty(routes)) {
            RoutesConfig.Route match =
                    routes.stream()
                            .filter(route -> !CollectionUtils.isEmpty(route.getMatch_hosts()) && route.getMatch_hosts().contains(targetHost))
                            .findFirst()
                            .orElse(null);
            if (Objects.nonNull(match)) resultHost = match.getHost();
        }
        return resultHost;
    }

    public static int routePort(String targetHost, int defaultPort) {
        int resultPort = defaultPort;
        RoutesConfig routesProperty = ApplicationContextUtil.getBean(RoutesConfig.class);
        List<RoutesConfig.Route> routes = routesProperty.getRoutes();
        if (!CollectionUtils.isEmpty(routes)) {
            RoutesConfig.Route match =
                    routes.stream()
                            .filter(route -> !CollectionUtils.isEmpty(route.getMatch_hosts()) && route.getMatch_hosts().contains(targetHost))
                            .findFirst()
                            .orElse(null);
            if (Objects.nonNull(match)) resultPort = match.getPort();
        }
        return resultPort;
    }
}
