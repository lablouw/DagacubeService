package com.dagacube.util.validation;

import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationUtil {

	private ValidationUtil() {
	}

	private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	public static ResponseEntity<List<String>> validate(Object... objects) {
		return validateInternal(new ArrayList<>(), objects);
	}

	private static ResponseEntity<List<String>> validateInternal(List<String> validationFailures, Object... objects) {
		for (Object o : objects) {
			if (o instanceof Iterable) {
				((Iterable)o).forEach(o1 -> validateInternal(validationFailures, o1));
			} else {
				validateSingleObject(o, validationFailures);
			}
		}

		if (!validationFailures.isEmpty()) {
			return ResponseEntity.badRequest().body(validationFailures);
		}

		return null;
	}

	private static void validateSingleObject(Object o, List<String> validationFailures) {
		if (o == null) {
			return;
		}
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(o);
		if (!CollectionUtils.isEmpty(constraintViolations)) {
			validationFailures.addAll(constraintViolations
					.stream()
					.map(cv -> cv.getLeafBean().toString() + ": " + cv.getMessage())
					.collect(Collectors.toList())
			);
		}
	}

}
