package com.mateusgomes.luizalabs.handler;

import org.springframework.validation.BindingResult;

public class ErrorHandler {

    public String buildValidationErrorMessage(BindingResult bindingResult){
        int listErrorSize = bindingResult.getAllErrors().size();
        StringBuilder errorMessage = new StringBuilder("The following errors were found: \n");

        for(int i = 0; i < listErrorSize; i++){
            errorMessage
                    .append(i)
                    .append(" - ")
                    .append(bindingResult.getAllErrors().get(i).getDefaultMessage())
                    .append(";\n");
        }

        return errorMessage.toString();
    }
}
