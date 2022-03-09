package com.h.autoconfigure;

import com.h.Strategy.BootstrapStrategy;
import com.h.api.TransferProtocol;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：Hukaiwen
 * @description ：配置连接集合
 * @date ：2022/1/24 10:04
 */
@ConfigurationProperties(prefix = "netty.config")
public class NettyConfigProperties {

    private final List<Configuration> configurations;

    public NettyConfigProperties(){
        configurations = new ArrayList<>();
    }

    public List<Configuration> getConfigurations() {
        return configurations;
    }

    public static class Configuration{
        private String localAddress;
        private String name;
        private TransferProtocol protocol;
        private Integer port;
        private String multicastAddress;
        private String broadcastAddress;
        private boolean available;
        private boolean autoStart;
        private Class<? extends BootstrapStrategy<?,?>> bootstrapStrategy;

        public Configuration(){
            this.localAddress = "127.0.0.1";
            this.port = 0;
            this.available = true;
            this.autoStart = true;
        }

        @Override
        public String toString() {
            return "Configuration{" +
                    "localAddress='" + localAddress + '\'' +
                    ", name='" + name + '\'' +
                    ", protocol=" + protocol +
                    ", port=" + port +
                    ", multicastAddress='" + multicastAddress + '\'' +
                    ", broadcastAddress='" + broadcastAddress + '\'' +
                    ", available=" + available +
                    ", autoStart=" + autoStart +
                    ", bootstrapStrategy=" + bootstrapStrategy +
                    '}';
        }

        public String getLocalAddress() {
            return localAddress;
        }

        public String getName() {
            return name;
        }

        public TransferProtocol getProtocol() {
            return protocol;
        }

        public Integer getPort() {
            return port;
        }

        public String getMulticastAddress() {
            return multicastAddress;
        }

        public String getBroadcastAddress() {
            return broadcastAddress;
        }

        public boolean isAvailable() {
            return available;
        }

        public boolean isAutoStart() {
            return autoStart;
        }

        public Class<? extends BootstrapStrategy<?, ?>> getBootstrapStrategy() {
            return bootstrapStrategy;
        }

        public void setLocalAddress(String localAddress) {
            this.localAddress = localAddress;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setProtocol(TransferProtocol protocol) {
            this.protocol = protocol;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public void setMulticastAddress(String multicastAddress) {
            this.multicastAddress = multicastAddress;
        }

        public void setBroadcastAddress(String broadcastAddress) {
            this.broadcastAddress = broadcastAddress;
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }

        public void setAutoStart(boolean autoStart) {
            this.autoStart = autoStart;
        }

        public void setBootstrapStrategy(Class<? extends BootstrapStrategy<?, ?>> bootstrapStrategy) {
            this.bootstrapStrategy = bootstrapStrategy;
        }
    }
}
