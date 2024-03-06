package com.hepta.rmi;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexFile;
//import top.custom.hepta.Pine;
//import top.custom.hepta.callback.MethodHook;

public class LoadEntry {

    static String TAG = LoadEntry.class.getName();

    static boolean dump = false;

    public static native void dumpMethod(Member method);
    public static native ClassLoader[] getClassLoaderList();
    public static native ClassLoader[] getBaseDexClassLoaderList();
    public static native void AutodumpDex();

    public static Context context;
    public static native void dumpDexByCookie(long[] cookie,String dumpDir);

    public static void entry(Context cxt,String source){
        context = cxt;
        PreLoadNativeSO(context,source);
        Log.e("Rzx","entry dumpdex");
//        dumpdex();
    }


    public static void PreLoadNativeSO(Context context, String source) {
        try {
            String abi= "arm64-v8a";
            if(!android.os.Process.is64Bit()){
                abi = "armeabi-v7a";
            }
            String libdump = source+"!/lib/"+abi+"/libDavilkRuntime.so";
            System.load(libdump);
        }catch (Exception e){
            Log.e("LoadEntry","LoadSo error");
        }
    }



    public static void dumpdex() {


        File pathFile=new File(context.getFilesDir().getAbsolutePath()+"/dump");
        if(!pathFile.exists()){
            pathFile.mkdirs();
        }
        BaseDexClassLoader[] classLoaders = (BaseDexClassLoader[]) getBaseDexClassLoaderList();
        try {
            Log.e(TAG,"class xunhuan");
            //TODO:to get 'pathList' field and 'dexElements' field by reflection.
            //private final DexPathList pathList;
            Class<?> baseDexClassLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader");
            Field pathListField = baseDexClassLoaderClass.getDeclaredField("pathList");

            //private Element[] dexElements;
            Class<?> dexPathListClass = Class.forName("dalvik.system.DexPathList");
            Class<?> Element = Class.forName("dalvik.system.DexPathList$Element");
            Field dexElementsField = dexPathListClass.getDeclaredField("dexElements");
            Field DexFile_mCookie = DexFile.class.getDeclaredField("mCookie");
            Field DexFile_mFileName = DexFile.class.getDeclaredField("mFileName");
            Field path_filed = Element.getDeclaredField("path");
            Field dexFile_filed = Element.getDeclaredField("dexFile");
            pathListField.setAccessible(true);
            DexFile_mCookie.setAccessible(true);
            DexFile_mFileName.setAccessible(true);
            dexElementsField.setAccessible(true);
            dexFile_filed.setAccessible(true);
            for (ClassLoader classLoader:classLoaders) {

                if (classLoader instanceof BaseDexClassLoader) {
                    Object BaseDexClassLoad_PathList = pathListField.get(classLoader);
                    Object[] DexPathList_dexElements = (Object[]) dexElementsField.get(BaseDexClassLoad_PathList);
                    int i = 0;
                    if (DexPathList_dexElements != null) {
                        for (Object dexElement : DexPathList_dexElements) {
                            DexFile dexFile = (DexFile) dexFile_filed.get(dexElement);
                            if (dexFile != null) {
                                //这个cookie 在android 13是一个智能指针，保存的是一个native 的 DexFile 指针
                                long[] cookie = (long[]) DexFile_mCookie.get(dexFile);
                                dumpDexByCookie(cookie, pathFile.getAbsolutePath());
                            }
                        }
                    }
                } else {
                    Log.e(TAG, "class not instanceof BaseDexClassLoader");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dumpClass(String className){
        ClassLoader[] classLoaders =  getClassLoaderList();
        for (ClassLoader classLoader:classLoaders) {
            try {
                Class LoadEntry_cls =  classLoader.loadClass(className);
                Method[] Declaredmethods =  LoadEntry_cls.getDeclaredMethods();
                for (Method method :Declaredmethods ) {
                    dumpMethod(method);
                }
                Constructor[] DeclaredConstructors =  LoadEntry_cls.getDeclaredConstructors();
                for (Constructor method :DeclaredConstructors ) {
                    dumpMethod(method);
                }
            } catch (ClassNotFoundException e) {
            }
        }
    }


    public static void dumpMethodTest(){
        ClassLoader[] classLoaders =  getClassLoaderList();
        for (ClassLoader classLoader:classLoaders) {
            try {
                Class LoadEntry_cls =  classLoader.loadClass("com.hepta.dumpdex.LoadEntry");
                Method Entry_mtd =  LoadEntry_cls.getDeclaredMethod("dumpdex",Context.class,String.class);
                dumpMethod(Entry_mtd);

            } catch (ClassNotFoundException e) {
                continue;
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
