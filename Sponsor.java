package com.innerchic.common.jax_bus;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Vector;

/**
 * 作者:贾恒飞
 * 创建:2019/4/11 0011
 * 所属包:com.yswl.tool.sponsor_bus
 * 描述: 发起者[单例]
 **/
public class Sponsor {
    private Sponsor(){}
    private static Sponsor mInstance;
    public static Sponsor getInstance() {
        if (mInstance==null){
            synchronized (Sponsor.class){
                mInstance = new Sponsor();
            }
        }
        return mInstance;
    }

    private final String TAG = "JaxLogger";

    //构建数据存储
    private Vector<SubObj> subs = new Vector<>();

    /**
     * 推送数据
     * @param event 事件
     */
    public void post(Object event){
        for (SubObj sub : subs) {
            Iterator<Method> iterator = sub.getIterable();
            while (iterator.hasNext()){
                Method method = iterator.next();
                Class<?>[] cls = method.getParameterTypes();
                if (cls.length==0)continue;
                if (cls[0].equals(event.getClass())){
                    //执行
                    try {
                        method.invoke(sub.getObj(),event);
                    } catch (IllegalAccessException e) {
                        Log.e(TAG, "post: IllegalAccessException >>>", e);
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        Log.e(TAG, "post: InvocationTargetException >>>", e);
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 绑定
     * @param subject 绑定的对象
     */
    public void register(Object subject){
        if (subject==null)return;
        Log.d(TAG, "Sponsor.register: "+subject.getClass().getName());
        Vector<Method> vector = new Vector<>();
        Method[] methods = subject.getClass().getDeclaredMethods();
        for (Method method : methods) {
            Subject subjectAdd = method.getAnnotation(Subject.class);
            if (subjectAdd==null)continue;
            vector.add(method);
        }
        if (vector.size()==0){
            Log.d(TAG, "register: 当前对象绑定失败,无法绑定没有Subject的对象");
            return;
        }
        SubObj SUB = new SubObj();
        SUB.setObj(subject);
        SUB.setMethods(vector);
        subs.add(SUB);
    }

    /**
     * 解除绑定
     * @param subject 解除绑定者对象
     */
    public void unregister(Object subject){
        if (subject==null)return;
        Log.d(TAG, "unregister: Sponsor销毁对象:"+subject.getClass().getName());
        SubObj subObj = null;
        for (SubObj sub : subs) {
            if (sub.getObj().equals(subject)){
                subObj = sub;
                break;
            }
        }
        if (subObj==null){
            Log.d(TAG, "unregister: 当前对象解除绑定失败,当前对像未绑定");
            return;
        }
        subs.remove(subObj);
    }

}
