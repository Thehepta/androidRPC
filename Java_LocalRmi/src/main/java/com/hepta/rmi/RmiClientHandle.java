package com.hepta.rmi;

import top.canyie.pine.Pine;

import java.awt.*;

public class RmiClientHandle  implements IClientHandle {

    RmiClient.ICallback onHookMethodCall;
    RmiClientHandle(RmiClient.ICallback iCallback){
        onHookMethodCall = iCallback;
    }



    @Override
    public void addMessage(String message) {
        System.out.println(message);
        EventQueue.invokeLater(new Runnable() {
            public void run () {
            }
        });
    }

    @Override
    public void setNames(String[] names) {

    }

    @Override
    public void HookMethodafterCall(int hashcode, Object[] args, Object result) {
        new Thread(){
            @Override
            public void run() {
                onHookMethodCall.afterCall(hashcode,args,result);
            }
        }.start();

    }

    @Override
    public void HookMethodbeforeCall(int hashcode, Object[] args, String throwable) {
        new Thread(){
            @Override
            public void run() {
                onHookMethodCall.beforeCall(hashcode,args,throwable);
            }
        }.start();

    }

}
