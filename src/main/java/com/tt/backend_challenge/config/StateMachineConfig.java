package com.tt.backend_challenge.config;

import com.tt.backend_challenge.enums.EmployeeState;
import com.tt.backend_challenge.enums.EventState;
import com.tt.backend_challenge.exceptions.EventNotAcceptableException;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;


@Configuration
@EnableStateMachineFactory
@Log4j2
public class StateMachineConfig extends StateMachineConfigurerAdapter<EmployeeState, EventState> {

    @Override
    public void configure(StateMachineStateConfigurer<EmployeeState, EventState> states) throws Exception {
        states
                .withStates()
                .initial(EmployeeState.ADDED) // Başlangıç durumu
                .state(EmployeeState.IN_CHECK) // Ana durum
                .fork(EmployeeState.IN_CHECK) // Alt durumlara geçişi başlatır
                .join(EmployeeState.ALL_CHECKS_FINISHED) // Alt durumları birleştirir
                .state(EmployeeState.APPROVED) // Onay durumu
                .end(EmployeeState.ACTIVE) // Bitiş durumu
                .and()
                .withStates()
                .parent(EmployeeState.IN_CHECK)
                .initial(EmployeeState.SECURITY_CHECK_STARTED) // Güvenlik kontrolü başlangıcı
                .end(EmployeeState.SECURITY_CHECK_FINISHED) // Güvenlik kontrolü bitişi
                .and()
                .withStates()
                .parent(EmployeeState.IN_CHECK)
                .initial(EmployeeState.WORK_PERMIT_CHECK_STARTED) // Çalışma izni başlangıcı
                .state(EmployeeState.WORK_PERMIT_CHECK_PENDING_VERIFICATION) // Çalışma izni doğrulama bekleniyor
                .end(EmployeeState.WORK_PERMIT_CHECK_FINISHED); // Çalışma izni bitişi
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<EmployeeState, EventState> transitions) throws Exception {
        transitions
                // ADDED -> IN_CHECK
                .withExternal()
                .source(EmployeeState.ADDED)
                .target(EmployeeState.IN_CHECK)
                .event(EventState.BEGIN_CHECK)
                .and()
                // Alt durumlar (IN_CHECK)
                .withFork()
                .source(EmployeeState.IN_CHECK)
                .target(EmployeeState.SECURITY_CHECK_STARTED)
                .target(EmployeeState.WORK_PERMIT_CHECK_STARTED)
                .and()
                .withExternal()
                .source(EmployeeState.SECURITY_CHECK_STARTED)
                .target(EmployeeState.SECURITY_CHECK_FINISHED)
                .event(EventState.FINISH_SECURITY_CHECK)
                .and()
                .withExternal()
                .source(EmployeeState.WORK_PERMIT_CHECK_STARTED)
                .target(EmployeeState.WORK_PERMIT_CHECK_PENDING_VERIFICATION)
                .event(EventState.COMPLETE_INITIAL_WORK_PERMIT_CHECK)
                .and()
                .withExternal()
                .source(EmployeeState.WORK_PERMIT_CHECK_PENDING_VERIFICATION)
                .target(EmployeeState.WORK_PERMIT_CHECK_FINISHED)
                .event(EventState.FINISH_WORK_PERMIT_CHECK)
                .and()
                .withJoin()
                .source(EmployeeState.SECURITY_CHECK_FINISHED)
                .source(EmployeeState.WORK_PERMIT_CHECK_FINISHED)
                .target(EmployeeState.ALL_CHECKS_FINISHED)
                .and()
                // ALL_CHECKS_FINISHED -> APPROVED
                .withExternal()
                .source(EmployeeState.ALL_CHECKS_FINISHED)
                .target(EmployeeState.APPROVED)
                .and()
                // APPROVED -> ACTIVE
                .withExternal()
                .source(EmployeeState.APPROVED)
                .target(EmployeeState.ACTIVE)
                .event(EventState.ACTIVATE);
    }
}