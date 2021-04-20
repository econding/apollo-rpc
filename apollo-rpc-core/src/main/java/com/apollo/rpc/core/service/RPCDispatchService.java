package com.apollo.rpc.core.service;

import com.apollo.rpc.core.handler.holder.MsgHolder;
import com.apollo.rpc.core.handler.RPCMsgHandler;
import com.apollo.rpc.core.msg.MsgBase;
import com.apollo.rpc.core.msg.impl.RPCAuthReqMsg;
import com.apollo.rpc.core.msg.impl.RPCAuthRspMsg;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;

public class RPCDispatchService {

    private static final Log log = LogFactory.getLog(RPCDispatchService.class);
    private static final HashMap<Class, RPCMsgHandler> msgHandlerHashMap = new HashMap<>();
    private AuthenticationService authenticationService;

    public void dispatch(MsgHolder msgHolder){
        if(hasPermit(msgHolder)){
            RPCMsgHandler handler = getHandler(msgHolder);
            handler.doHandle(msgHolder);
        }
    }

    public void setAuthenticationService(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    public RPCMsgHandler getHandler(MsgHolder requestMsgHolder){
        return msgHandlerHashMap.get(requestMsgHolder.getMsg().getClass());
    }

    public boolean hasPermit(MsgHolder msgHolder){
        if(needPermit(msgHolder)){
            return authenticationService.isAuth(msgHolder.getChannel());
        }else{
            return true;
        }
    }

    public boolean needPermit(MsgHolder msgHolder){
        if(msgHolder.getMsg() instanceof RPCAuthReqMsg){
            return false;
        }
        if(msgHolder.getMsg() instanceof RPCAuthRspMsg){
            return false;
        }
        return true;
    }

    public void registerHandler(RPCMsgHandler handler){
        Class clazz = getParameterizedType(handler.getClass());
        if(clazz != null){
            msgHandlerHashMap.put(clazz,handler);
        }
    }

    public Class getParameterizedType(Class clazz) {

        Type[] types = clazz.getGenericInterfaces();      //所有接口
        if(types != null){
            for(Type type:types){
                if(type instanceof ParameterizedType){    //参数化类型接口
                    ParameterizedType parameterizedType = (ParameterizedType)type;
                    if(parameterizedType.getRawType().getTypeName().equals(RPCMsgHandler.class.getName())){
                        Type[] actTypes = parameterizedType.getActualTypeArguments();
                        for(Type actType:actTypes){       //所有的实际类型参数
                            if(actType instanceof Class){
                                Class c = (Class)actType;
                                if(MsgBase.class.isAssignableFrom(c)){
                                    return c;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
