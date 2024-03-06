package com.hepta.rmi;


import android.content.Context;
import android.util.Log;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;

import java.nio.ByteBuffer;
//import com.hepta.andjvmenv.AndJvmEnv;
//import com.hepta.fgadget.FridaLoadEntry;


public class ServiceHandle extends Connection implements IServiceHandle {
    IClientHandle iClientHandle;
    Context context;
    String Apk_source;

//    Map<Integer, MethodHook.Unhook> unhook_maps;
    ServiceHandle(Context context, String source){
        this.context = context;
        this.Apk_source = source;
        new ObjectSpace(this).register(1, this);
        iClientHandle = ObjectSpace.getRemoteObject(this, 2, IClientHandle.class);
//        unhook_maps = new HashMap<>();
    }
    @Override
    public void registerName(String name) {
        Log.i("Rzx",name);
        Thread callThread =  new Thread(){
            @Override
            public void run() {
                String message = name + " connected.";
                iClientHandle.addMessage(message);
            }
        };
        callThread.start();
//        try {
//            callThread.join();                      不能等待，也就是说，不能连续互相远程调用，更不能在一个线程互相远程调用
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

    }

    public void disconnect() {
        Log.i("Rzx","disconnect");
//        for (int key : unhook_maps.keySet()) {
//            MethodHook.Unhook value = unhook_maps.get(key);
//            value.unhook();
//        }
    }


    @Override
    public void sendMessage(String message) {
        Log.e("Rzx",message);
    }

    @Override
    public void AndJvmEnv_init() {
//        AndJvmEnv.Entry(context,Apk_source);
    }

    @Override
    public ClassLoader[] AndJvmEnv_getAllClassLoader() {
//        return AndJvmEnv.getAllClassLoader();
        return null;
    }

    @Override
    public void AndJvmEnv_LoadJvmTI() {
//        AndJvmEnv.Entry(context,Apk_source);
//        AndJvmEnv.LoadJvmTI();
    }

    @Override
    public void AndJvmEnv_dumpdex() {
//        AndJvmEnv.Entry(context,Apk_source);
//        AndJvmEnv.dumpdex(context, context.getDataDir().getAbsolutePath());
    }

    @Override
    public void FridaLoad() {
//        FridaLoadEntry.Entry(context,Apk_source);
    }

    @Override
    public Object invokeStaticMethod(String className, String methodName,Object... args) {
//        AndJvmEnv.Entry(context,Apk_source);
//        Class<?> cls = AndJvmEnv.FindClass(className);
//        return ReflectUtils.on(cls).call(methodName,args);

        return null;
    }


    @Override
    public int HookMethod(String className, String methodName, String[] args) {
        Log.d("Rzx","0");
//        AndJvmEnv.Entry(context,Apk_source);
//        Class<?> cls = AndJvmEnv.FindClass(className);
//        Class<?>[] types = new Class[args.length];
//        for(int i=0;i<args.length;i++){
//            Class<?> arg_cls;
//            if(args[i].equals("int")){
//                arg_cls = int.class;
//            }
//            else if(args[i].equals("int[]")){
//                arg_cls = int[].class;
//            }
//            else if(args[i].equals("byte")){
//                arg_cls = byte.class;
//            }
//            else if(args[i].equals("byte[]")){
//                arg_cls = byte[].class;
//            }
//            else if(args[i].equals("short")){
//                arg_cls = short.class;
//            }
//            else if(args[i].equals("short[]")){
//                arg_cls = short[].class;
//            }
//            else if(args[i].equals("long[]")){
//                arg_cls = long[].class;
//            }
//            else if(args[i].equals("long")){
//                arg_cls = long.class;
//            }
//            else if(args[i].equals("float")){
//                arg_cls = float.class;
//            }
//            else if(args[i].equals("float[]")){
//                arg_cls = float[].class;
//            }
//            else if(args[i].equals("double")){
//                arg_cls = double.class;
//            }
//            else if(args[i].equals("double[]")){
//                arg_cls = double[].class;
//            }
//            else if(args[i].equals("boolean")){
//                arg_cls = boolean.class;
//            }
//            else if(args[i].equals("boolean[]")){
//                arg_cls = boolean[].class;
//            }
//            else if(args[i].equals("char[]")){
//                arg_cls = char[].class;
//            }
//            else if(args[i].equals("char")){
//                arg_cls = char.class;
//            }else {
//                arg_cls = AndJvmEnv.FindClass(args[i]);
//            }
//            types[i] =arg_cls;
//        }
//        try {
//            Method method = ReflectUtils.on(cls).exactMethod(methodName,types);
//            MethodHook.Unhook unhook = Pine.hook(method, new MethodHook() {
//                @Override
//                public void beforeCall(Pine.CallFrame callFrame) throws Throwable {
//                    Log.d("Rzx","beforeCall");
//                    String finalResult = getCallStatck();
//                    new Thread(){
//                        @Override
//                        public void run() {
////                            iClientHandle.HookMethodbeforeCall(callFrame.method.hashCode());
//
//                            iClientHandle.HookMethodbeforeCall(callFrame.method.hashCode(),callFrame.args, finalResult);
//                        }
//                    }.start();
//
//                }
//
//                @Override
//                public void afterCall(Pine.CallFrame callFrame) throws Throwable {
//                    Log.d("Rzx","afterCall");
//                    new Thread(){
//                        @Override
//                        public void run() {
//                            iClientHandle.HookMethodafterCall(callFrame.method.hashCode(),callFrame.args,callFrame.getResult());
//                        }
//                    }.start();
//
//                }
//            });
//            unhook_maps.put(unhook.hashCode(), unhook);
//            return method.hashCode();
//        } catch (NoSuchMethodException e) {
//
//        }
        return -1;
    }

    @Override
    public boolean RemoteLoadApk(ByteBuffer args) {
        Log.e("Rzx","RemoteLoadApk");
        //        return memLoadApk(args);
        return false;
    }

    @Override
    public boolean dumpdex() {
        Log.e("Rzx","dumpdex");
        LoadEntry.dumpdex();
        return false;
    }

    public native boolean memLoadApk(byte[] args);

    public static String getCallStatck() {
        Throwable ex = new Throwable();
        StackTraceElement[] stackElements = ex.getStackTrace();
        String result = "";
        for (int i = 4; i < stackElements.length; i++) {
            result = result +"\n" +stackElements[i].getClassName()+"."+stackElements[i].getFileName()+":"+stackElements[i].getMethodName()+"("+stackElements[i].getLineNumber()+")";
        }
        return result;
    }
}