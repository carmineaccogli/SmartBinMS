package it.unisalento.pas.smartcitywastemanagement.smartbinms.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidGeoJSONPointValidator.class)

public @interface ValidGeoJSONPoint {
    String message() default "Invalid GeoJSONPoint";
    Class<?>[] groups() default  {};
    Class<? extends Payload>[] payload() default {};
}
