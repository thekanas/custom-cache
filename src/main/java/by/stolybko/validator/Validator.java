package by.stolybko.validator;

public interface Validator<T> {

    ValidationResult validate(T object);
}
