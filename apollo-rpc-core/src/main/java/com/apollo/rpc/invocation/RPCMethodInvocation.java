package com.apollo.rpc.invocation;

import com.apollo.rpc.msg.impl.RPCRequestMsg;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RPCMethodInvocation {

    private Object service;

    public RPCMethodInvocation(Object service){
        this.service = service;
    }

    public Object invokeMethod(RPCRequestMsg request) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Object[] arg = request.requestParameter;
        Method methodT = null;

        for(Method method:service.getClass().getMethods() ){

            if(method.getName().equals(request.serverMethod) && (arg == null || method.getParameterTypes().length == arg.length)){
                methodT = method;
                if(arg != null){
                    for(int i = 0 ;i<arg.length;i++){
                        Class clazz1 = getClazzForBaseType(method.getParameterTypes()[i]);
                        Class clazz2 = arg[i].getClass();
                        if(!clazzEquals(clazz1,clazz2)){
                            methodT = null;
                            continue;
                        }
                    }
                }
                if(methodT != null){
                    break;
                }
            }
        }

        if(methodT != null){
            return methodT.invoke(service,arg);
        }

        throw new NoSuchMethodException();
    }

    public Class getClazzForBaseType(Class clazz){
        if(clazz == int.class){
            return Integer.class;
        }
        else if(clazz == long.class){
            return Long.class;
        }
        else if(clazz == short.class){
            return Short.class;
        }
        else if(clazz == double.class){
            return Double.class;
        }
        else if(clazz == float.class){
            return Float.class;
        }
        else if(clazz == boolean.class){
            return Boolean.class;
        }
        else if(clazz == char.class){
            return Character.class;
        }
        else if(clazz == byte.class){
            return Byte.class;
        }else{
            return clazz;
        }
    }

    /**
     * 判断clazz2 是否可以转型为 clazz1
     * @param clazz1
     * @param clazz2
     * @return
     */
    public boolean clazzEquals(Class clazz1,Class clazz2){

        if(clazz1 == clazz2){
            return true;
        }else if(clazz1.isAssignableFrom(clazz2)){
            return true;
        }else{
            if(clazz1.isArray() && clazz2.isArray()){
                Class clazz11 = clazz1.getComponentType();
                Class clazz22 = clazz2.getComponentType();
                if(clazz11.isAssignableFrom(clazz22)){
                    return true;
                }
            }
        }
        return false;
    }

}
