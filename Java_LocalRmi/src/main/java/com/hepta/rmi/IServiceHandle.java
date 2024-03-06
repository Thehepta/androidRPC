package com.hepta.rmi;

import java.nio.ByteBuffer;

public interface IServiceHandle {
    public void registerName (String name);

    public void sendMessage (String message);

    public void AndJvmEnv_init();
    public ClassLoader[] AndJvmEnv_getAllClassLoader();
    public void AndJvmEnv_LoadJvmTI();
    public void AndJvmEnv_dumpdex();

    public void FridaLoad();

    public Object invokeStaticMethod(String className,String methodName,Object... args);

    public int HookMethod(String className, String methodName, String[] args);

    public boolean RemoteLoadApk(ByteBuffer args);//    public void HookStaticMethod(String message);

    public boolean dumpdex();

}