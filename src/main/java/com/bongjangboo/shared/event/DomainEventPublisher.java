package com.bongjangboo.shared.event;

/**
 * 도메인 이벤트 발행 포트
 */
public interface DomainEventPublisher {
    void publish(DomainEvent event);
}
