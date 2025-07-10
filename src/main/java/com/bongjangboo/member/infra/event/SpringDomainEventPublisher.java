package com.bongjangboo.member.infra.event;

import com.bongjangboo.shared.event.DomainEvent;
import com.bongjangboo.shared.event.DomainEventPublisher;
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

    @Override
    public void publish(DomainEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
