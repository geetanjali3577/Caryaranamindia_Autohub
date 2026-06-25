package com.autohub.configuration;

import com.autohub.repository.UserPresenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final UserPresenceRepository repository;

    @EventListener
    public void handleConnect(
            SessionConnectedEvent event
    ) {

        System.out.println(
                "User Connected"
        );
    }

    @EventListener
    public void handleDisconnect(
            SessionDisconnectEvent event
    ) {

        System.out.println(
                "User Disconnected"
        );
    }
}
