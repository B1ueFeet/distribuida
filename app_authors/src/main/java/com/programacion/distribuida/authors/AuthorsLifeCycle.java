package com.programacion.distribuida.authors;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.vertx.ext.consul.CheckOptions;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.ext.consul.ServiceOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.consul.ConsulClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.InetAddress;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class AuthorsLifeCycle {

    @Inject
    @ConfigProperty(name = "consul.host", defaultValue = "127.0.0.1")
    String consultIp;

    @Inject
    @ConfigProperty(name = "consul.port", defaultValue = "8500")
    Integer consultPort;

    @Inject
    @ConfigProperty(name = "quarkus.http.port")
    Integer appPort;

    String serviceId;

    public void init(@Observes StartupEvent event, Vertx vertx) throws Exception {
        System.out.println("Authors service starting...");

        ConsulClient client = ConsulClient.create(vertx,
                new ConsulClientOptions()
                        .setHost(consultIp)
                        .setPort(consultPort)
        );

        serviceId = UUID.randomUUID().toString();

        var ipAddress = InetAddress.getLocalHost();

        client.registerServiceAndAwait(
                new ServiceOptions()
                        .setName("app-authors")
                        .setId(serviceId)
                        .setAddress(ipAddress.getHostAddress())
                        .setPort(appPort)
                        .setTags(
                                List.of(
                                        "traefik.enable=true", //que la aplicaion se registre en treafik
                                        "traefik.http.routers.router-app-authors.rule=PathPrefix(`/app-authors`)",
                                        "traefik.http.routers.router-app-authors.middlewares=middleware-authors",
                                        "traefik.http.middlewares.middleware-authors.stripPrefix.prefixes=/app-authors"
                                )
                        )
                        .setCheckOptions(
                                new CheckOptions() // GET http://localhost:9090/q/health/live
                                        .setHttp("http://"+ ipAddress.getHostAddress() + ":" + appPort+ "/q/health/live")
                                        .setInterval("10s") // cantidad de tiempo en el que verificara si el servicio está bueno
                                        .setDeregisterAfter("20s")
                        )
        );
    }

    public void stop(@Observes ShutdownEvent event, Vertx vertx) throws Exception {
        System.out.println("Authors service stoping...");

        ConsulClient client = ConsulClient.create(vertx,
                new ConsulClientOptions()
                        .setHost(consultIp)
                        .setPort(consultPort)
        );

        client.deregisterServiceAndAwait(serviceId);

    }
}
