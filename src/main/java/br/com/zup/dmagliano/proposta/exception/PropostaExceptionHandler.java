package br.com.zup.dmagliano.proposta.exception;

import br.com.zup.dmagliano.proposta.controller.PropostaController;
import br.com.zup.dmagliano.proposta.validator.dto.FieldErrorOutputDto;
import br.com.zup.dmagliano.proposta.validator.dto.ValidationErrorsOutputDto;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;


@RestControllerAdvice
public class PropostaExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(PropostaExceptionHandler.class);

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationErrorsOutputDto handleValidationError(MethodArgumentNotValidException exception) {

        List<ObjectError> globalErrors = exception.getBindingResult().getGlobalErrors();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();


        return buildValidationErrors(globalErrors, fieldErrors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ValidationErrorsOutputDto handleValidationError(BindException exception) {

        List<ObjectError> globalErrors = exception.getBindingResult().getGlobalErrors();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        return buildValidationErrors(globalErrors,
                fieldErrors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public FieldErrorOutputDto handleIllegalArgumentError(IllegalArgumentException exception) {

        String message = exception.getLocalizedMessage();

        return new FieldErrorOutputDto("",message);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalStateException.class)
    public FieldErrorOutputDto handleIllegalStateError(IllegalStateException exception) {

        String message = exception.getLocalizedMessage();

        return new FieldErrorOutputDto("",message);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public FieldErrorOutputDto handleIllegalStateError(ConstraintViolationException exception) {

        String message = exception.getLocalizedMessage();

        return new FieldErrorOutputDto("",message);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public FieldErrorOutputDto handleEntityNotFound(EntityNotFoundException exception) {

        String message = exception.getLocalizedMessage();

        return new FieldErrorOutputDto("",message);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DateTimeParseException.class)
    public FieldErrorOutputDto handleHttpMessageNotReadable(DateTimeParseException exception) {

        String message = exception.getLocalizedMessage();

        return new FieldErrorOutputDto("",message);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(FeignException.UnprocessableEntity.class)
    public FieldErrorOutputDto handleHttpMessageNotReadable(FeignException exception) {

        logger.warn("Erro, status {} ao processar a request\n {} \n em: {}",
                exception.status(), exception.request(), LocalDateTime.now());

        String message = exception.contentUTF8();

        return new FieldErrorOutputDto("",message);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(FeignException.InternalServerError.class)
    public FieldErrorOutputDto handleHttpMessageNotReadable(FeignException.InternalServerError exception) {

        logger.warn("Erro, status {} ao processar a request\n {} \n em: {}",
                exception.status(), exception.request(), LocalDateTime.now());

        String message = exception.contentUTF8();

        return new FieldErrorOutputDto("",message);
    }



    

    private ValidationErrorsOutputDto buildValidationErrors(List<ObjectError> globalErrors,
                                                            List<FieldError> fieldErrors) {
        ValidationErrorsOutputDto validationErrors = new ValidationErrorsOutputDto();

        globalErrors.forEach(error -> validationErrors.addError(getErrorMessage(error)));

        fieldErrors.forEach(error -> {
            String errorMessage = getErrorMessage(error);
            validationErrors.addFieldErrors(error.getField(), errorMessage);
        });


        return validationErrors;
    }

    private String getErrorMessage(ObjectError error) {
        return messageSource.getMessage(error, LocaleContextHolder.getLocale());
    }
}

