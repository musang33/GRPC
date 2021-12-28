package com.song.project.armeria.config;

import com.linecorp.armeria.server.HttpServiceWithRoutes;
import com.linecorp.armeria.server.docs.DocService;
import com.linecorp.armeria.server.grpc.GrpcService;
import com.linecorp.armeria.spring.ArmeriaAutoConfiguration;
import com.linecorp.armeria.spring.ArmeriaServerConfigurator;
import com.song.project.armeria.Hello;
import com.song.project.armeria.HelloServiceGrpc;
import com.song.project.armeria.HelloServiceImpl;
import com.song.project.armeria.properties.ArmeriaProperty;
import io.grpc.BindableService;
import io.grpc.protobuf.services.ProtoReflectionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArmeriaConfig extends ArmeriaAutoConfiguration {
    @Bean
    public ArmeriaServerConfigurator armeriaServerConfigurator(final ArmeriaProperty property,
                                                               final HttpServiceWithRoutes grpcService) {
        // Customize the server using the given ServerBuilder. For example:
        return builder -> {
            // Add DocService that enables you to send Thrift and gRPC requests
            // from web browser.
            builder.http(property.getHttpPort());

            builder.service(grpcService)
                    .service("prefix:/prefix", grpcService)
                    .serviceUnder("/docs",
                            DocService.builder()
                                    .exampleRequests(
                                            HelloServiceGrpc.SERVICE_NAME,
                                            "Hello",
                                            Hello.HelloRequest.newBuilder().setName("Armeria").build())
                                    .build());

            //builder.serviceUnder("/docs", new DocService());

            // Log every message which the server receives and responds.
            //builder.decorator(LoggingService.newDecorator());

            // Write access log after completing a request.
            //builder.accessLogWriter(AccessLogWriter.combined(), false);

            // You can also bind asynchronous RPC services such as Thrift and gRPC:
            // builder.service(THttpService.of(...));
            // builder.service(GrpcService.builder()...build());
        };
    }
}
