package com.example.jangboo.member.infra.event;

import com.example.jangboo.shared.event.DomainEvent;
import com.example.jangboo.shared.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 스프링 이벤트소싱 사용
 */

@Component
@RequiredArgsConstructor
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Publishes the given domain event using Spring's application event infrastructure.
     *
     * @param event the domain event to be published
     */
    @Override
    public void publish(DomainEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
