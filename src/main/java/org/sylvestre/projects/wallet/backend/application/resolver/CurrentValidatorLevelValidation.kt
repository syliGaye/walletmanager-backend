package org.sylvestre.projects.wallet.backend.application.resolver
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class CurrentValidatorLevelValidation: ConstraintValidator<CurrentValidatorLevelContraint, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        // Implement your custom validation logic here
        // Return true if value is valid, false otherwise
        return value != null && value.startsWith("Custom");
    }
}