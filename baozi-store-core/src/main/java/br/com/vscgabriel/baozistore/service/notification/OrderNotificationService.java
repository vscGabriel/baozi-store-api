package br.com.vscgabriel.baozistore.service.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class OrderNotificationService {

    @Async
    public CompletableFuture<Boolean> notifyOrderCreated(Long orderId) {
        boolean virtualThread = Thread.currentThread().isVirtual();
        log.info("Order {} confirmation scheduled. virtualThread={}", orderId, virtualThread);
        return CompletableFuture.completedFuture(virtualThread);
    }
}
