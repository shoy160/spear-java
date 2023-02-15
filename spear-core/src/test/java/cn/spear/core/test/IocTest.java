package cn.spear.core.test;

import cn.spear.core.ioc.IocContext;
import cn.spear.core.ioc.ServiceCollection;
import cn.spear.core.test.model.ServiceDTO;
import cn.spear.core.util.IdentityUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author shay
 * @date 2020/9/8
 */
@Slf4j
public class IocTest {
//    private final ServiceProvider provider;

    public IocTest() {
        ServiceCollection services = ServiceCollection.instance();
        services.addScoped(ServiceDTO.class, serviceProvider -> {
            ServiceDTO dto = new ServiceDTO();
            dto.setId(IdentityUtils.fastId());
            return dto;
        });
        services.build();
    }

    @Test
    public void getServiceTest() {
        for (int i = 0; i < 10; i++) {
            final int index = i;
            IocContext.scope(provider -> {
                for (int j = 0; j < 5; j++) {
                    ServiceDTO service = provider.getServiceT(ServiceDTO.class);
                    log.info("[{}-{}] -> id :{}", index, j, service.getId());
                }
            });
        }
    }
}
