package com.apollo.rpc.core.remote;

import com.apollo.rpc.core.comm.RemoteServerInfo;

import java.util.List;
import java.util.Map;

/**
 * 服务发现
 */
public interface RemoteServerDiscovery {
    Map<String,List<RemoteServerInfo>> getServices();
}
