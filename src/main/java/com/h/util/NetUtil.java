package com.h.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ：Hukaiwen
 * @description ：网络工具类
 * @date ：2022/2/28 15:58
 */
public final class NetUtil {

    private final static Logger log = LoggerFactory.getLogger(NetUtil.class);

    private static Enumeration<NetworkInterface> networkInterfaces;

    private static final Map<String, InetSocketAddress> INET_SOCKET_ADDRESS_MAP = new ConcurrentHashMap<>();

    private static final Map<String, InetAddress> INET_ADDRESS_MAP = new ConcurrentHashMap<>();

    private static final Map<InetAddress, NetworkInterface> NETWORK_INTERFACE_MAP = new ConcurrentHashMap<>();

    static {
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            log.error("获取网络接口失败", e);
        }
    }

    /**
     * @param : inetSocketAddress
     * @return : java.net.InetSocketAddress
     * @author : Hukaiwen
     * @description : 获取套接字地址
     * @date : 2022/3/9 9:40
     */
    public static InetSocketAddress getInetSocketAddress(String inetSocketAddress) throws UnknownHostException {
        if (!INET_SOCKET_ADDRESS_MAP.containsKey(inetSocketAddress)) {
            final String[] s = inetSocketAddress.split(":");
            final InetAddress inetAddress = getInetAddress(s[0]);
            INET_SOCKET_ADDRESS_MAP.put(inetSocketAddress, new InetSocketAddress(inetAddress, Integer.parseInt(s[1])));
            INET_ADDRESS_MAP.putIfAbsent(s[0],inetAddress);
        }
        return INET_SOCKET_ADDRESS_MAP.get(inetSocketAddress);
    }

    /**
     * @param : [java.lang.String, java.lang.String]
     * @return : java.net.InetSocketAddress
     * @author : Hukaiwen
     * @description : 获取套接字地址
     * @date : 2022/3/9 9:37
     */
    public static InetSocketAddress getInetSocketAddress(String inetAddress, String port) throws UnknownHostException {
        String inetSocketAddress = inetAddress + ":" + port;
        return getInetSocketAddress(inetSocketAddress);
    }

   /**
    * @param : [java.lang.String, java.lang.Integer]
    * @return : java.net.InetSocketAddress
    * @author : Hukaiwen
    * @description : 获取套接字地址
    * @date : 2022/3/9 9:59
    */
    public static InetSocketAddress getInetSocketAddress(String inetAddress, Integer port) throws UnknownHostException {
        String inetSocketAddress = inetAddress + ":" + port;
        return getInetSocketAddress(inetSocketAddress);
    }

    /**
     * @param : [java.lang.String]
     * @return : java.net.InetAddress
     * @author : Hukaiwen
     * @description : 获取IP地址
     * @date : 2022/3/9 9:29
     */
    public static InetAddress getInetAddress(String inetAddress) throws UnknownHostException {
        if (!INET_ADDRESS_MAP.containsKey(inetAddress)) {
            INET_ADDRESS_MAP.put(inetAddress, InetAddress.getByName(inetAddress));
        }
        return INET_ADDRESS_MAP.get(inetAddress);
    }

    /**
     * @param : [java.net.InetAddress]
     * @return : java.net.NetworkInterface
     * @author : Hukaiwen
     * @description : 获取网络接口
     * @date : 2022/3/9 9:17
     */
    public static NetworkInterface getNetworkInterface(InetAddress local) {
        if (NETWORK_INTERFACE_MAP.containsKey(local)){
            return NETWORK_INTERFACE_MAP.get(local);
        }
        NetworkInterface ni = io.netty.util.NetUtil.LOOPBACK_IF;
        findNi:
        while (networkInterfaces.hasMoreElements()) {
            final NetworkInterface networkInterface = networkInterfaces.nextElement();
            final Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                final InetAddress inetAddress = inetAddresses.nextElement();
                if (inetAddress.equals(local)) {
                    ni = networkInterface;
                    NETWORK_INTERFACE_MAP.put(local, ni);
                    break findNi;
                }
            }
        }
        return ni;
    }
}
