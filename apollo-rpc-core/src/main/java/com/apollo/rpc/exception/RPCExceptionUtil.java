package com.apollo.rpc.exception;


import com.apollo.rpc.msg.RPCRspBase;
import com.apollo.rpc.msg.impl.RPCAuthRspMsg;
import com.apollo.rpc.msg.impl.RPCResponseMsg;

public class RPCExceptionUtil {

    public static final int NoSuchRemoteServerException = 1;
    public static final int RemoteServerDisabledException = 2;

    public static final int IllegalAccessException = 3;
    public static final int RpcInvocationTargetException = 4;
    public static final int NoSuchMethodException = 5;
    public static final int RequestOutOfTimeException = 6;
    public static final int AuthenticationFailureException = 7;
    public static final int NoAuthorizedException = 8;
    public static final int RemoteServerLimitException = 9;
    public static final int NoSuchServiceException = 10;


    public static RPCException throwException(RPCRspBase rpcRspBase){

        int code = rpcRspBase.responseCode;

        if(IllegalAccessException == code){
            return new RPCMethodIllegalAccessException((RPCResponseMsg)rpcRspBase);
        }else if(NoSuchMethodException == code){
            return new NoSuchRPCMethodException((RPCResponseMsg)rpcRspBase);
        }else if(RpcInvocationTargetException == code){
            return new RPCInvocationTargetException((RPCResponseMsg)rpcRspBase);
        }else if(AuthenticationFailureException == code){
            return new AuthenticationFailureException((RPCAuthRspMsg) rpcRspBase);
        }else if(RequestOutOfTimeException == code){
            return new ResponseOutOfTimeException(rpcRspBase);
        }else if(NoAuthorizedException == code){
            return new NotAuthorizedException(rpcRspBase);
        }else if(RemoteServerLimitException == code){
            return new RemoteServerLimitException(rpcRspBase);
        }else if(NoSuchServiceException == code){
            return new NoSuchServiceException((RPCResponseMsg)rpcRspBase);
        }else{
            return new ResponseCodeErrorException(rpcRspBase);
        }
    }

}
