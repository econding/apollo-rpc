package com.apollo.rpc.server.instance;

import com.apollo.rpc.comm.Constant;
import com.apollo.rpc.msg.RPCReqBase;
import com.apollo.rpc.server.RemoteServerHolder;
import com.apollo.rpc.session.DefaultSessionFactory;
import com.apollo.rpc.session.RpcSession;
import io.netty.channel.Channel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Objects;

public class RemoteServerInstanceImpl implements RemoteServerInstance{

    private static final Log log = LogFactory.getLog(RemoteServerHolder.class);

    private Channel channel;

    private String ip;
    private String port;
    private String serverName;

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getServerName() {
        return serverName;
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    public Object doRequest(RPCReqBase reqBase) {
        reqBase.instanceName = toString();
        RpcSession session = DefaultSessionFactory.instance.createSession(channel);
        return session.doRequest(reqBase);
    }

    public void active(Channel channel) {
        log.info(this.toString()+"has been initialized");
        this.channel = channel;
    }

    public RemoteServerInstanceImpl(String ip,String port,String serverName){
        this.ip = ip;
        this.port = port;
        this.serverName = serverName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoteServerInstance that = (RemoteServerInstance) o;
        return Objects.equals(ip, that.getIp()) && Objects.equals(port, that.getPort());
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip+ Constant.separator+port);
    }

    @Override
    public String toString() {
        return "RemoteServerInstance{" +
                "ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", serverName='" + serverName + '\'' +
                '}';
    }

}
