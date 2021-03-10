package com.apollo.rpc.exception;

import com.apollo.rpc.msg.RPCRspBase;
import com.apollo.rpc.msg.impl.RPCAuthRspMsg;
import com.apollo.rpc.msg.impl.RPCResponseMsg;

public class RPCException extends RuntimeException{

    /**远程方法执行异常*/
    public static final int RemoteMethodInvocationException = 1;
    /** 请求超时*/
    public static final int RequestOutOfTimeException = 2;
    /** 签权失败*/
    public static final int AuthenticationFailureException = 3;
    /** 非法请求*/
    public static final int NoAuthorizedException = 4;
    /** 服务器限流*/
    public static final int RemoteServerLimitException = 5;
    /** 服务不存在*/
    public static final int NoSuchServiceException = 6;

    public RPCException(){
        super();
    }

    public RPCException(Throwable cause){
        super(cause);
    }

    public RPCException(String msg,Throwable cause) {
        super(msg,cause);
    }

    public RPCException(String msg){
        super(msg);
    }

    public static RPCException throwException(RPCRspBase rpcRspBase){
        int code = rpcRspBase.responseCode;
        switch(code) {
            case RemoteMethodInvocationException: return throwMethodInvocationException(rpcRspBase);
            case AuthenticationFailureException: return new AuthenticationFailureException((RPCAuthRspMsg) rpcRspBase);
            case RequestOutOfTimeException: return new ResponseOutOfTimeException(rpcRspBase);
            case NoAuthorizedException: return new NotAuthorizedException(rpcRspBase);
            case RemoteServerLimitException: return new RemoteServerLimitException(rpcRspBase);
            case NoSuchServiceException: return new NoSuchServiceException((RPCResponseMsg)rpcRspBase);
            default: return new ResponseCodeErrorException(rpcRspBase);
        }
    }

    public static RPCException throwMethodInvocationException(RPCRspBase rpcRspBase){
        return new RemoteMethodInvocationException("the remote server has reported an error: "+rpcRspBase.instanceName,rpcRspBase.exception);
    }

}
