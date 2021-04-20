package com.apollo.rpc.core.msg;

import com.apollo.rpc.core.serializable.IDGenerator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


public abstract class RPCReqBase<T extends RPCRspBase> extends MsgBase {

    public T rspBase;
    public long requestTime = 0l;

    public RPCReqBase(){
        this.sequenceNo = IDGenerator.getID();
        this.requestTime = System.currentTimeMillis();
    }

    public T getRspMsg(){

        T resMsg;
        Type type = this.getClass().getGenericSuperclass();
        if(type instanceof ParameterizedType){
            ParameterizedType pt = (ParameterizedType) type;
            Class clazz = (Class<T>) pt.getActualTypeArguments()[0];
            try {
                resMsg =  (T)clazz.newInstance();
                resMsg.init(this);
                return resMsg;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

}
