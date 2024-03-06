package top.canyie.pine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class Pine {

    public static class CallFrame {
        /**
         * The calling method.
         */
        public final Member method;

        /**
         * The "this" object of this call, {@code null} if executing a static method.
         * Change it in {@code beforeCall} to set new object as "this" when calling original method.
         */
        public Object thisObject;

        /**
         * The arguments passed to the method in this call. Will never be null.
         * Change it or its value in {@code beforeCall} to change arguments when calling original method.
         */
        public Object[] args;
        private Object result;
        private Throwable throwable;
        /* package */ boolean returnEarly;
        private HookRecord hookRecord;

        public CallFrame(HookRecord hookRecord, Object thisObject, Object[] args) {
            this.hookRecord = hookRecord;
            this.method = hookRecord.target;
            this.thisObject = thisObject;
            this.args = args;
        }

        /**
         * Get the result that will be returned in this method call.
         * @return The result that will be returned in this method call
         */
        public Object getResult() {
            return result;
        }

        /**
         * Set a result that will be returned in this method call.
         * If you call it {@code beforeCall}, the original method call will be prevented, and next
         * hooks will not be called.
         * @param result The return value you want to set.
         */
        public void setResult(Object result) {
            this.result = result;
            this.throwable = null;
            this.returnEarly = true;
        }

        /**
         * Like {@link CallFrame#setResult(Object)} but only set the return value if no exception will be thrown.
         * @param result The return value you want to set.
         */
        public void setResultIfNoException(Object result) {
            if (this.throwable == null) {
                this.result = result;
                this.returnEarly = true;
            }
        }

        /**
         * Get the exception that will be thrown in this method call.
         * @return The exception that will be thrown in this method call.
         */
        public Throwable getThrowable() {
            return throwable;
        }

        /**
         * Return whether an exception will be thrown as the result of this method call.
         * @return {@code true} If there is an exception will be thrown, {@code false} otherwise.
         */
        public boolean hasThrowable() {
            return throwable != null;
        }

        /**
         * Set the exception that will be thrown in this method call.
         * If you call it {@code beforeCall}, the original method call will be prevented, and next
         * hooks will not be called.
         * @param throwable The exception you want to throw.
         */
        public void setThrowable(Throwable throwable) {
            this.throwable = throwable;
            this.result = null;
            this.returnEarly = true;
        }

        /**
         * Like {@link CallFrame#getResult()} but throwing an exception if there is an exception set.
         * @return The result of this method call
         * @throws Throwable The exception happened in this method call
         */
        public Object getResultOrThrowable() throws Throwable {
            if (throwable != null)
                throw throwable;
            return result;
        }

        /**
         * Reset any previous result or exception, and allows the original method to be executed.
         */
        public void resetResult() {
            this.result = null;
            this.throwable = null;
            this.returnEarly = false;
        }


    }



    public static final class HookRecord {
        public final Member target;
        public final long artMethod;
        public Method backup;
        public boolean isStatic;
        public int paramNumber;
        public Class<?>[] paramTypes;
        private Set<Object> callbacks = new HashSet<>();
        public volatile Object paramTypesCache;

        public HookRecord(Member target, long artMethod) {
            this.target = target;
            this.artMethod = artMethod;
        }


        public boolean isPending() {
            return backup == null;
        }
    }


}
