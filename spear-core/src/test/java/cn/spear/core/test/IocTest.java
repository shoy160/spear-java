package cn.spear.core.test;

import cn.spear.core.ioc.ServiceCollection;
import cn.spear.core.ioc.ServiceProvider;
import cn.spear.core.ioc.impl.ServiceCollectionImpl;
import cn.spear.core.test.model.ServiceDTO;
import cn.spear.core.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author shay
 * @date 2020/9/8
 */
@Slf4j
public class IocTest {
    private final ServiceProvider provider;

    public IocTest() {
        ServiceCollection services = new ServiceCollectionImpl();
        services.addSingleton(ServiceDTO.class, serviceProvider -> {
            ServiceDTO dto = new ServiceDTO();
            dto.setId(CommonUtils.fastId());
            return dto;
        });
        this.provider = services.build();
    }

    @Test
    public void getServiceTest() {
        for (int i = 0; i < 100; i++) {
            ServiceDTO service = this.provider.getServiceT(ServiceDTO.class);
            log.info("[{}] -> id :{}", i, service.getId());
        }
    }
}
