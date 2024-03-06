package com.hepta.rmi;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.esotericsoftware.minlog.Log;
import top.canyie.pine.Pine;

import javax.security.auth.callback.Callback;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class RmiClient {


    Map<Integer,ICallback> iCallbackMap;

    IServiceHandle iServiceHandle;
    RmiClientHandle chatFrame;



    public interface ICallback {

        public void afterCall(int hashcode, Object[] args, Object result);
        public void beforeCall(int hashCode, Object[] args, String throwable);
    }
    public RmiClient() {
        iCallbackMap = new  HashMap<Integer,ICallback>();
        final Client client = new Client();
        client.start();
        register(client);
        client.addListener(new Listener.ThreadedListener(new Listener() {
            public void connected (final Connection connection) {
                System.out.println("connected");
                onConnected();

            }

            public void received (Connection connection, Object object) {

            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
            }
        }));
        iServiceHandle = ObjectSpace.getRemoteObject(client, 1, IServiceHandle.class);
        chatFrame = new RmiClientHandle(new ICallback(){

            @Override
            public void afterCall(int hashcode, Object[] args, Object result) {
                System.out.println("iCallbackMap afterCall"+hashcode);
                iCallbackMap.get(hashcode).afterCall(hashcode,args,result);
            }

            @Override
            public void beforeCall(int hashcode, Object[] args, String throwable) {
                System.out.println("iCallbackMap beforeCall");
                iCallbackMap.get(hashcode).beforeCall(hashcode,args,throwable);
            }
        });
        new ObjectSpace(client).register(2, chatFrame);

        new Thread("Connect") {
            public void run () {
                try {
                    client.connect(50000, "192.168.31.152", 19999);

                    // Server communication after connection can go here, or in Listener#connected().
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.exit(1);
                }
            }
        }.start();
        while(true);
    }




    static public void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        // This must be called in order to use ObjectSpaces.
        ObjectSpace.registerClasses(kryo);
        // The interfaces that will be used as remote objects must be registered.
        kryo.register(IServiceHandle.class);
        kryo.register(IClientHandle.class);
        // The classes of all method parameters and return values
        // for remote objects must also be registered.
        kryo.register(String[].class);
//        kryo.register(ByteBuffer.class);


    }

    public void HookMethod(String className, String methodName, String[] args , ICallback callback){

//        String []arg={"java.lang.String","java.lang.String"};
        int method_hashcode =  iServiceHandle.HookMethod(className,methodName,args);
        System.out.println("HookMethod"+method_hashcode);
        iCallbackMap.put(method_hashcode,callback);
    }

    public void RemoteLoadApk(String filepath){
        System.out.println("RemoteLoadApk:"+filepath);
        File file = new File(filepath);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            byte[] fileData = new byte[(int) file.length()];
            fileInputStream.read(fileData);
            fileInputStream.close();
            ByteBuffer buffer = ByteBuffer.wrap(fileData);
            Boolean ret =  iServiceHandle.RemoteLoadApk(buffer);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }





    }

    public boolean dumpdex(){
        return iServiceHandle.dumpdex();
    }

    public void invokStaticMethod(String className, String methodName,  Object[] objArr){
        iServiceHandle.invokeStaticMethod(className,methodName,objArr);
    }

    public void onConnected(){
//        iServiceHandle.AndJvmEnv_LoadJvmTI();
//        iServiceHandle.AndJvmEnv_dumpdex();
//        String []arg={"int","byte[]"};
//        HookMethod("com.bytedance.sdk.component.panglearmor.SoftDecTool", "bc", arg, new ICallback() {
//            @Override
//            public void afterCall(int hashcode, Object[] args, Object result) {
//                System.out.println("SoftDecTool afterCall");
//            }
//
//            @Override
//            public void beforeCall(int hashCode, Object[] args, String throwable) {
//
//                System.out.println("SoftDecTool  beforeCall"+throwable);
//            }
//        });

//        RemoteLoadApk("D:\\git\\hide-apk\\fridaload\\build\\outputs\\apk\\debug\\fridaload-debug.apk");
        dumpdex();




//        String []arg={"java.lang.String","java.lang.String"};
//        HookMethod("android.util.Log", "e", arg, new ICallback() {
//            @Override
//            public void afterCall(int hashcode, Object[] args, Object result) {
//                System.out.println("android.util.Log afterCall");
//            }
//
//            @Override
//            public void beforeCall(int hashCode, Object[] args, String throwable) {
//
//                System.out.println("android.util.Log beforeCall"+throwable);
//            }
//        });

//        String []arg={"rzx","java.lang.String"};
//        invokStaticMethod("android.util.Log","e",arg);
    }


    public static String getCallStatck() {
        Throwable ex = new Throwable();
        StackTraceElement[] stackElements = ex.getStackTrace();
        String result = "";
        for (int i = 4; i < stackElements.length; i++) {
            result = result +"\n" +stackElements[i].getClassName()+"."+stackElements[i].getFileName()+":"+stackElements[i].getMethodName()+"("+stackElements[i].getLineNumber()+")";
        }
        return result;
    }


    public static void main (String[] args) throws IOException {
        Log.set(Log.LEVEL_DEBUG);
        RmiClient rmiClient = new RmiClient();

    }
}

