package cn.spear.core.service;

import cn.spear.core.lang.Weight;
import cn.spear.core.service.enums.ServiceCodec;
import cn.spear.core.service.enums.ServiceProtocol;
import cn.spear.core.util.CommonUtils;
import lombok.Getter;
import lombok.Setter;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Objects;

/**
 * 服务地址
 *
 * @author shay
 * @date 2020/9/4
 */
@Getter
@Setter
public class ServiceAddress implements Weight {

    private final static String LOCAL_HOST_REG = "^(\\*|(localhost))$";
    private String host;
    private Integer port;
    /**
     * 服务协议
     */
    private ServiceProtocol protocol;

    /**
     * 对外注册的服务地址(ip或DNS)
     */
    private String service;
    /**
     * 对外注册的服务端口
     */
    private Integer servicePort = this.port;
    /**
     * 权重
     */
    private Integer weight;
    /**
     * 是否开启Gzip
     */
    private Boolean gzip;

    /**
     * 服务编码
     */
    private ServiceCodec codec;

    public ServiceAddress() {
        this.gzip = true;
        this.weight = 1;
        this.protocol = ServiceProtocol.Tcp;
    }

    public ServiceAddress(int port) {
        this("localhost", port);
    }

    public ServiceAddress(String host, int port) {
        this();
        this.host = host;
        this.port = port;
        this.servicePort = port;
    }

    public ServiceAddress(String host, int port, ServiceProtocol protocol) {
        this(host, port);
        this.protocol = protocol;
    }


    @Override
    public String toString() {
        String service = CommonUtils.isEmpty(this.service) ? this.host : this.service;
        Integer port = this.servicePort == null || this.servicePort <= 0 ? this.port : this.servicePort;
        return String.format("%s://%s:%d", this.protocol.toString().toLowerCase(), service, port);
    }

    @Override
    public int getWeight() {
        return (this.weight == null || this.weight < 1) ? 1 : this.weight;
    }

    public SocketAddress getClientAddress() {
        String service = CommonUtils.isEmpty(this.service) ? this.host : this.service;
        return new InetSocketAddress(service, this.servicePort);
    }

    public SocketAddress getServerAddress() {
        return new InetSocketAddress(this.host, this.port);
    }

    public boolean isLocal() {
        return CommonUtils.isEmpty(this.host) || this.host.matches(LOCAL_HOST_REG);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceAddress that = (ServiceAddress) o;
        return Objects.equals(host, that.host) &&
                Objects.equals(port, that.port) &&
                protocol == that.protocol &&
                Objects.equals(service, that.service) &&
                Objects.equals(weight, that.weight) &&
                Objects.equals(gzip, that.gzip) &&
                codec == that.codec;
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port, protocol, service, weight, gzip, codec);
    }
}
