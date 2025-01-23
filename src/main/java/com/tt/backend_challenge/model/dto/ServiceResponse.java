package com.tt.backend_challenge.model.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
public class ServiceResponse {
    HttpStatus status;
    String message;
    Object response;
}
