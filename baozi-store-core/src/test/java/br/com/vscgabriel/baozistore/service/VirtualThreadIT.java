package br.com.vscgabriel.baozistore.service;

import br.com.vscgabriel.baozistore.service.notification.OrderNotificationService;
import br.com.vscgabriel.baozistore.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class VirtualThreadIT extends IntegrationTestSupport {

    @Autowired
    private OrderNotificationService orderNotificationService;

    @Test
    void shouldExecuteAsyncNotificationUsingVirtualThread() throws Exception {
        boolean virtualThread = orderNotificationService.notifyOrderCreated(1L).get(5, TimeUnit.SECONDS);

        assertThat(virtualThread).isTrue();
    }
}
