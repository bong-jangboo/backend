package com.example.jangboo.shared.event;

/**
 * 도메인 이벤트 발행 포트
 */
public interface DomainEventPublisher {
    /**
 * Publishes the specified domain event to interested subscribers.
 *
 * @param event the domain event to be published
 */
void publish(DomainEvent event);
}
