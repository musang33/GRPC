package com.song.project.armeria.config;

import com.linecorp.armeria.server.HttpServiceWithRoutes;
import com.linecorp.armeria.server.grpc.GrpcService;
import com.song.project.armeria.annotation.Interceptor;
import com.song.project.armeria.utils.Orderer;
import io.grpc.BindableService;
import io.grpc.ServerInterceptor;
import io.grpc.ServerInterceptors;
import io.grpc.protobuf.services.ProtoReflectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;

@Slf4j
@Configuration
public class GrpcConfig  {
    @Bean
    public HttpServiceWithRoutes grpcService(final List<ServerInterceptor> interceptors,
                                             final List<BindableService> services) {

        interceptors.sort(Orderer.getInstance());

        final var grpcServiceBuilder = GrpcService.builder();
        services.forEach( service -> {

            final var interceptOfService = service.getClass().getAnnotation(Interceptor.class);
            if(Objects.isNull(interceptOfService))
            {
                log.info("Interceptor not found : {} ", service);
                return;
            }

            final var targetInterceptors = List.of(interceptOfService.value());
            final var serviceInterceptors = interceptors.stream()
                    .filter( interceptor -> targetInterceptors.contains(interceptor.getClass()))
                    .toArray(ServerInterceptor[]::new);

            grpcServiceBuilder.addService(ServerInterceptors.intercept( service, serviceInterceptors ));
        });
        grpcServiceBuilder.addService(ProtoReflectionService.newInstance())
                //.supportedSerializationFormats(GrpcSerializationFormats.values())
                .enableUnframedRequests(true);

        return grpcServiceBuilder.build();
    }
}
