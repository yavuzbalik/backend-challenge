package com.tt.backend_challenge.exceptions;

import org.springframework.messaging.Message;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class CustomStateMachineListener extends StateMachineListenerAdapter<String, String> {

    @Override
    public void eventNotAccepted(Message<String> event) {
        // Olay kabul edilmediğinde özel bir istisna fırlat
        throw new EventNotAcceptableException("Event '" + event.getPayload() + "' not accepted in the current state.");
    }
}
