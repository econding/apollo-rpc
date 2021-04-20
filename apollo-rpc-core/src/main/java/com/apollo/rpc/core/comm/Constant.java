package com.apollo.rpc.core.comm;


public class Constant {

    /** ip与port的 分隔符 */
    public static final String separator = ":";

    /**消息类型：方法调用 */
    public static final String method_invocation = "0";

    /**消息类型：服务可用性测试 */
    public static final String server_check = "1";

    /**消息类型：服务鉴权 */
    public static final String server_auth = "2";

    /**服务发现异步任务的线程池大小 */
    public static final int pool_size = 3;

}
