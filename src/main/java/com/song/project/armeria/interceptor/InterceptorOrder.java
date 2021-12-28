package com.song.project.armeria.interceptor;

public interface InterceptorOrder {
    int AUTH = 0;
    int ACCESS = AUTH - 1;
}
