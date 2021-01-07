package com.apollo.rpc.server;

import com.apollo.rpc.comm.RemoteServerInfo;

import java.util.List;
import java.util.Map;

/**
 * 服务发现
 */
public interface RemoteServerDiscovery {
    Map<String,List<RemoteServerInfo>> getServerInfo();
}
