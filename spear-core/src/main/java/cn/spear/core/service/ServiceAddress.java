package cn.spear.core.service;

import cn.spear.core.service.enums.ServiceCodec;
import cn.spear.core.service.enums.ServiceProtocol;
import cn.spear.core.util.CommonUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * 服务地址
 *
 * @author shay
 * @date 2020/9/4
 */
@Getter
@Setter
public class ServiceAddress {

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
    }

    public ServiceAddress(String host, int port) {
        this();
        this.host = host;
        this.port = port;
    }

    @Override
    public String toString() {
        String service = CommonUtils.isEmpty(this.service) ? this.host : this.service;
        return String.format("%s://%s:%d", this.protocol.toString().toLowerCase(), service, this.port);
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