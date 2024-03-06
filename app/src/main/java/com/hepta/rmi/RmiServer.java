package com.hepta.rmi;


import android.content.Context;
import android.util.Log;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;

import java.io.IOException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.HashSet;

//import top.canyie.pine.Pine;
//import top.canyie.pine.callback.MethodHook;


public class RmiServer {


    public  ServiceHandle player;

    public static void entry(Context context, String source,String argument){
        Log.e("Rzx","RmiServer:"+argument);
        LoadSo(context,source);
        String []str = argument.split(",");
        int post = Integer.valueOf(str[0]);
        int flag = Integer.valueOf(str[0]);

        try {
            if(flag == 1){
                new RmiServer(context,source,post);
            }else {
                new Thread("Connect") {
                    public void run () {
                        try {
                            new RmiServer(context,source,post);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void LoadSo(Context context, String source) {
        try {
            LoadEntry.entry(context,source);
            String abi= "arm64-v8a";
            if(!android.os.Process.is64Bit()){
                abi = "armeabi-v7a";
            }
            String str = source+"!/lib/"+abi+"/librmi.so";
            Log.e("RmiServer",str);
            System.load(str);
        }catch (Exception e){
            Log.e("RmiServer",e.getMessage());

        }

    }


    public RmiServer(Context context, String source,int post) throws IOException {
        com.esotericsoftware.minlog.Log.set(0);
        Server server = new Server(){
            protected Connection newConnection () {
                return new ServiceHandle(context,source);
            }
        };
        register(server);
        server.bind(19999);

        server.addListener(new Listener.ThreadedListener(new Listener() {
            public void connected (final Connection connection) {
                player = (ServiceHandle)connection;
//                player.registerName("TEXT");
                System.out.println("connected");
            }

            public void received (Connection connection, Object object) {
                System.out.println("received");
            }

            @Override
            public void disconnected(Connection connection) {
                ServiceHandle serviceHandle = (ServiceHandle)connection;
                serviceHandle.disconnect();
            }
        }));

        server.start();

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
//        kryo.register(Pine.CallFrame.class);
        kryo.register(Member.class);
        kryo.register(Method.class);
        kryo.register(HashSet.class);
//        kryo.register(Pine.HookRecord.class);
//        kryo.register(MethodHook.class);
        kryo.register(Throwable.class);
        kryo.register(ReflectUtils.class);
        kryo.register(NullPointerException.class);
//        kryo.register(ByteBuffer.class);


//        kryo.register(IllegalStateException.class);
//        kryo.register(RuntimeException.class);
//        kryo.register(NoSuchMethodException.class);
    }




}
