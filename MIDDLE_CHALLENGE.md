# 🚀 Coding Challenge - Backend Engineer 🚀

## 🧑‍💻 🤖 About the challenge

As a member of our backend engineering team, you will be responsible for building our core platform including an employee management system.
An employee entity (Employee) in this system contains very basic employee details including name, contract information, age, etc...

Employee also has a state. The state of Employee can be changed according to the predefined state transition rules.
We refer to the combination of states and state transition rules as State machine. 

### 👾 State machine definition

The states for a given Employee are:
- `ADDED`
- `IN-CHECK`
- `APPROVED`
- `ACTIVE`

Initially when Employee is added, it is assigned `ADDED` state automatically.


State transitions rules are defined as follows:

![state diagram](diagrams/middle_challenge_state_transition_diagram.png?raw=true "Statemachine diagram")

Where `BEGIN CHECK`, `APPROVE`, `UNAPPROVE`, `ACTIVATE` are state transition events.


### ‍💻🧑‍🔬 Task

Your task is to build  Restful API doing the following:
- An Endpoint to add an employee
- An Endpoint to change the state of a given employee according to the state machine rules 
- An Endpoint to fetch employee details

## 🏗 What else you need to know

Our backend stack is:
- Java 11 or above
- Spring Framework

Please provide a solution with the  above features with the following consideration.

- Being simply executable with minimum effort. Ideally using Docker and docker-compose or any similar approach
- For state management (State machine) you can use any library or data structure you consider appropriate
  - Some suggestions from our side (these are only suggestions, feel free to use something else if you want):
    - ENUM with states
    - Stateless4j library: https://github.com/stateless4j/stateless4j 
    - Spring state machine: https://projects.spring.io/spring-statemachine/
- Please provide testing for your solution
- Providing an API Contract e.g. OPENAPI spec. is a big plus







