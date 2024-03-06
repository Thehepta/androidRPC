package com.hepta.rmi;

public interface IClientHandle {
    public void addMessage (String message);

    public void setNames (String[] names);

    public void HookMethodafterCall(int hashcode, Object[] args, Object result);

    public void HookMethodbeforeCall(int hashCode, Object[] args, String  throwable);
}
