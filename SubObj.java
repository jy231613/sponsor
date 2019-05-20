package com.innerchic.common.jax_bus;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Vector;

/**
 * 作者:贾恒飞
 * 创建:2019/4/11 0011
 * 所属包:com.yswl.tool.sponsor_bus
 * 描述: 绑定对象
 **/
public class SubObj {

    private Object obj;
    private Vector<Method> methods;

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Vector<Method> getMethods() {
        return methods;
    }

    public void setMethods(Vector<Method> methods) {
        this.methods = methods;
    }

    public void add(Method method){
        if (methods==null)methods = new Vector<>();
        methods.add(method);
    }

    public Iterator<Method> getIterable(){
        if (methods==null)methods = new Vector<>();
        return methods.iterator();
    }

}
