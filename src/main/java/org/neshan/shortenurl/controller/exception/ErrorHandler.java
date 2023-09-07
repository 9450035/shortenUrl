package org.neshan.shortenurl.controller.exception;

import org.neshan.shortenurl.services.ShortenUrlService;
import org.neshan.shortenurl.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = {UserService.UserException.class, ShortenUrlService.ShortenUrlException.class})
    ProblemDetail userExceptions(UserService.UserException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

}
