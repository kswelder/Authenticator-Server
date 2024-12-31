package com.welderhayne.Oauth.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionsMessager {
    private HttpStatus status;
    private String message;
    private LocalDateTime date;
}
