package com.apollo.rpc.exception;


import com.apollo.rpc.msg.RPCRspBase;
import com.apollo.rpc.msg.impl.RPCAuthRspMsg;
import com.apollo.rpc.msg.impl.RPCResponseMsg;

public class RPCExceptionUtil {

    /**远程方法执行异常*/
    public static final int RemoteMethodInvocationException = 1;

    public static final int RequestOutOfTimeException = 2;
    public static final int AuthenticationFailureException = 3;
    public static final int NoAuthorizedException = 4;
    public static final int RemoteServerLimitException = 5;
    public static final int NoSuchServiceException = 6;

    public static RPCException throwException(RPCRspBase rpcRspBase){
        int code = rpcRspBase.responseCode;
        switch(code) {
            case RemoteMethodInvocationException: return getException(rpcRspBase);
            case AuthenticationFailureException: return new AuthenticationFailureException((RPCAuthRspMsg) rpcRspBase);
            case RequestOutOfTimeException: return new ResponseOutOfTimeException(rpcRspBase);
            case NoAuthorizedException: return new NotAuthorizedException(rpcRspBase);
            case RemoteServerLimitException: return new RemoteServerLimitException(rpcRspBase);
            case NoSuchServiceException: return new NoSuchServiceException((RPCResponseMsg)rpcRspBase);
            default: return new ResponseCodeErrorException(rpcRspBase);
        }
    }

    public static RPCException getException(RPCRspBase rpcRspBase){
        return new RemoteMethodInvocationException("the remote server has reported an error: "+rpcRspBase.instanceName,rpcRspBase.exception);
    }

}
