package com.dg.locationinfo.Services;

public interface ClientConnectionService<T>{
    void connect();
    void logClientStatus();
    T getClient();
}
