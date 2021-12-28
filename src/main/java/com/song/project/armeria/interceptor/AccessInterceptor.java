package com.song.project.armeria.interceptor;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(InterceptorOrder.ACCESS)
public class AccessInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
                                                                 Metadata metadata,
                                                                 ServerCallHandler<ReqT, RespT> serverCallHandler)
    {
        log.info("AccessInterceptor");

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>( serverCallHandler.startCall(new ForwardingServerCall.SimpleForwardingServerCall<>(serverCall) {
            @Override
            public void close(Status status, Metadata trailers) {
                log.info("close");
                super.close(status, trailers);
            }

            @Override
            public void sendMessage(RespT var1)
            {
                log.info("sendMessage");
                super.sendMessage(var1);
            }
        }, metadata ) ) {
            @Override
            public void onMessage(ReqT message) {
                super.onMessage(message);
            }
        } ;
    }
}
