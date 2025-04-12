package io.foodapp.server.utils;

import java.lang.reflect.Method;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class ValidationUtils {
    private static final ObjectMapper objectMapper;
    private static final Validator validator;
    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static <T> T validateAndConvertToObject(String json, Class<T> clazz)
            throws JsonMappingException, JsonProcessingException, MethodArgumentNotValidException {
        T object = objectMapper.readValue(json, clazz);
        java.util.Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            BindingResult bindingResult = new BeanPropertyBindingResult(object, clazz.getSimpleName());
            for (ConstraintViolation<T> violation : violations) {
                String fieldName = violation.getPropertyPath().toString();
                String errorMessage = violation.getMessage();
                bindingResult.addError(new FieldError(clazz.getSimpleName(), fieldName, errorMessage));
            }
            MethodParameter methodParameter = getFakeMethodParameter(clazz);
            throw new MethodArgumentNotValidException(methodParameter, bindingResult);
        }
        return object;
    }

    private static MethodParameter getFakeMethodParameter(Class<?> clazz) {
        try {
            Method method = ValidationUtils.class.getDeclaredMethod("fakeMethod", Object.class);
            return new MethodParameter(method, 0);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to create MethodParameter for validation", e);
        }
    }

    @SuppressWarnings("unused")
    private static void fakeMethod(Object param) {
    }

}
