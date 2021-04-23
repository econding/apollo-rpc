package com.apollo.rpc.core.remote.instance;

import com.apollo.rpc.core.comm.Constant;
import com.apollo.rpc.core.msg.RPCReqBase;
import com.apollo.rpc.core.remote.RemoteServerContainer;
import com.apollo.rpc.core.session.DefaultSessionFactory;
import com.apollo.rpc.core.session.RpcSession;
import io.netty.channel.Channel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Objects;

public class RemoteServerInstanceImpl implements RemoteServerInstance{

    private static final Log log = LogFactory.getLog(RemoteServerContainer.class);
    private Channel channel;
    private boolean active = false;

    private String ip;
    private String port;
    private String rpc_port;
    private String serverName;

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getRpcPort() {
        return rpc_port;
    }

    public String getServerName() {
        return serverName;
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    public Object invoke(RPCReqBase reqBase) {
        reqBase.serverName = serverName;
        reqBase.instanceName = toString();
        RpcSession session = DefaultSessionFactory.instance.createSession(channel);
        return session.request(reqBase);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void inActive() {
        this.active = false;
    }

    public void active(Channel channel) {
        this.active = true;
        log.info(this.toString()+"has been initialized");
        this.channel = channel;
    }

    public void destroy(){
        if(active){
            this.active = false;
            if(channel.isActive()){
                channel.close();
            }

        }
    }

    public RemoteServerInstanceImpl(String ip,String port,String rpc_port,String serverName){
        this.ip = ip;
        this.port = port;
        this.rpc_port = rpc_port;
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
