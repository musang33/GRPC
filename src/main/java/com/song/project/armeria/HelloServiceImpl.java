package com.song.project.armeria;
import com.song.project.armeria.annotation.Interceptor;
import com.song.project.armeria.interceptor.AccessInterceptor;
import com.song.project.armeria.interceptor.AuthInterceptor;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

@Interceptor
({
        AccessInterceptor.class, AuthInterceptor.class
})

@Service
public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {

    @Override
    public void hello(Hello.HelloRequest request, StreamObserver<Hello.HelloResponse> responseObserver) {
        if( request.getName().isEmpty())
        {
            responseObserver.onError(
                    Status.FAILED_PRECONDITION.withDescription("Name can't be empty").asException() );
        }
        else
        {
            responseObserver.onNext(buildReplay(toMessage(request.getName())));
            responseObserver.onCompleted();
        }
    }

    static String toMessage(String name)
    {
        return "Hello, " + name + "!";
    }

    private static Hello.HelloResponse buildReplay(Object message)
    {
        return Hello.HelloResponse.newBuilder().setMessage(String.valueOf(message)).build();
    }
}
