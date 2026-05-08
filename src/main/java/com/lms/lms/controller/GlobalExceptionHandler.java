package com.lms.lms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;

import java.util.logging.Logger;

/**
 * Global Exception Handler for the entire application
 * Handles all uncaught exceptions and provides user-friendly error pages
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());

    /**
     * Handle NullPointerException
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleNullPointerException(NullPointerException e) {
        logger.severe("NullPointerException: " + e.getMessage());
        e.printStackTrace();
        
        ModelAndView modelAndView = new ModelAndView("error/500");
        modelAndView.addObject("errorMessage", "Data processing error occurred. Please try again later.");
        modelAndView.addObject("errorCode", "500");
        return modelAndView;
    }

    /**
     * Handle IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warning("IllegalArgumentException: " + e.getMessage());
        
        ModelAndView modelAndView = new ModelAndView("error/400");
        modelAndView.addObject("errorMessage", "Invalid request: " + e.getMessage());
        modelAndView.addObject("errorCode", "400");
        return modelAndView;
    }

    /**
     * Handle Resource Not Found Exception
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleResourceNotFoundException(ResourceNotFoundException e) {
        logger.warning("ResourceNotFoundException: " + e.getMessage());
        
        ModelAndView modelAndView = new ModelAndView("error/404");
        modelAndView.addObject("errorMessage", e.getMessage());
        modelAndView.addObject("errorCode", "404");
        return modelAndView;
    }

    /**
     * Handle Access Denied Exception
     */
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handleAccessDeniedException(org.springframework.security.access.AccessDeniedException e) {
        logger.warning("AccessDeniedException: " + e.getMessage());
        
        ModelAndView modelAndView = new ModelAndView("error/403");
        modelAndView.addObject("errorMessage", "You don't have permission to access this resource.");
        modelAndView.addObject("errorCode", "403");
        return modelAndView;
    }

    /**
     * Handle all other exceptions
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleException(Exception e) {
        logger.severe("Unexpected Exception: " + e.getClass().getName() + " - " + e.getMessage());
        e.printStackTrace();
        
        ModelAndView modelAndView = new ModelAndView("error/500");
        modelAndView.addObject("errorMessage", "An unexpected error occurred. Our team has been notified.");
        modelAndView.addObject("errorCode", "500");
        return modelAndView;
    }
}

/**
 * Custom exception for when a resource is not found
 */
class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
