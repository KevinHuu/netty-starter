package com.h.api;

/**
 * @author ：Hukaiwen
 * @description ：传输协议
 * @date ：2022/1/24 10:17
 */
public enum TransferProtocol {
    /**
     * 传输协议TCP
     */
    TCP,
    /**
     * 传输协议UDP-单播
     */
    UDP,

    /**
     * 传输协议UDP-广播
     */
    UDP_BROADCAST,

    /**
     * 传输协议UDP-组播
     */
    UDP_MULTICAST
}
