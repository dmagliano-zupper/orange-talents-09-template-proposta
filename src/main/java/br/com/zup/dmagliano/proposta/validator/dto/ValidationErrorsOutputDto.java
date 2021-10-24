package br.com.zup.dmagliano.proposta.validator.dto;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorsOutputDto {

    private List<String> globalErrorsMessages = new ArrayList<>();
    private List<FieldErrorOutputDto> fieldErrors = new ArrayList<>();

    public void addError(String message){
        globalErrorsMessages.add(message);
    }

    public void addFieldErrors(String field, String message) {
        FieldErrorOutputDto fieldError = new FieldErrorOutputDto(field, message);
        fieldErrors.add(fieldError);
    }

    public List<String> getGlobalErrorsMessages() {
        return globalErrorsMessages;
    }

    public List<FieldErrorOutputDto> getFieldErrors() {
        return fieldErrors;
    }
}
