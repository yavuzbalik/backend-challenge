package com.tt.backend_challenge.machine;

import com.tt.backend_challenge.enums.EmployeeState;
import com.tt.backend_challenge.enums.EventState;
import com.tt.backend_challenge.exceptions.EventNotAcceptableException;
import com.tt.backend_challenge.model.Employee;
import lombok.AllArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.access.StateMachineAccess;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class EmployeeStateMachine {


    private static final String EMPLOYEE_ID = "EMPLOYEE_ID";

    private final StateMachineFactory<EmployeeState, EventState> stateMachineFactory;

    public Collection<EmployeeState> getEmployeeStatesFromStateMachine(Employee employee, EventState event) {
        StateMachine<EmployeeState, EventState> stateMachine = buildStateMachine(employee);
        Mono<Message<EventState>> message = Mono.just(MessageBuilder.withPayload(event).setHeader(EMPLOYEE_ID, employee.getId()).build());
        stateMachine.sendEvent(message).subscribe();
        return stateMachine.getState().getIds();
    }

    private StateMachine<EmployeeState, EventState> buildStateMachine(Employee employee) {
        StateMachine<EmployeeState, EventState> stateMachine = this.stateMachineFactory.getStateMachine(employee.getId().toString());
        stateMachine.stopReactively().subscribe();
        List<StateMachineAccess<EmployeeState, EventState>> stateMachineAccesses = stateMachine.getStateMachineAccessor().withAllRegions();
        for (StateMachineAccess<EmployeeState, EventState> stateMachineAccess : stateMachineAccesses) {
            DefaultStateMachineContext<EmployeeState, EventState> defaultStateMachineContext;
            if (!employee.getStates().contains(EmployeeState.IN_CHECK)) {
                defaultStateMachineContext = new DefaultStateMachineContext<>(employee.getStates().iterator().next(), null, null, null, null);
            } else {
                List<StateMachineContext<EmployeeState, EventState>> children = createChildrenStateMachineContexts(employee.getStates(), EmployeeState.IN_CHECK);
                defaultStateMachineContext = new DefaultStateMachineContext<EmployeeState, EventState>(children, EmployeeState.IN_CHECK, null, null, null);
            }
            stateMachineAccess.resetStateMachineReactively(defaultStateMachineContext).subscribe();
        }
        stateMachine.startReactively().subscribe();
        return stateMachine;
    }

    private List<StateMachineContext<EmployeeState, EventState>> createChildrenStateMachineContexts(Set<EmployeeState> employeeStates, EmployeeState rootState) {
        if (!employeeStates.contains(rootState)) {
            return List.of();
        }
        List<StateMachineContext<EmployeeState, EventState>> result = new ArrayList<>();
        for (EmployeeState employeeState : employeeStates) {
            if (!employeeState.equals(rootState)) {
                result.add(new DefaultStateMachineContext<>(employeeState, null, null, null));
            }
        }
        return result;
    }
}
