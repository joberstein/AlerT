package com.jesseoberstein.alert.validation;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Validates text input views by setting the tag on a supplied 'validation aware view'.
 * Sets errors on the input text view.
 * The 'submit' variable must be initialized with 'initializeValidationAwareView'.
 */
public abstract class AbstractValidator {
    private static final String INVALID = "0";
    private static final String VALID = "1";

    private Predicate<String> predicate;
    private TextView input;
    private String errorMsg;
    private Button submit;
    private int inputKey;

    AbstractValidator(TextView input, Predicate<String> predicate,
                      String errorMsg, Button submit, int inputKey) {
        this.input = input;
        this.predicate = predicate;
        this.errorMsg = errorMsg;
        this.submit = submit;
        this.inputKey = inputKey;
    }

    /**
     * Initialize the validation tag on this submit variable ('INVALID',...).
     * @param validationAwareView The view to set the validation tag on. It's enabled/disabled status
     *                            depends on the 'inputsToValidate' other activity elements.
     * @param inputsToValidate The number of inputs to validate.
     */
    public static void initializeValidationAwareView(View validationAwareView, int inputsToValidate) {
        String[] validators = new String[inputsToValidate];
        Arrays.fill(validators, AbstractValidator.INVALID);
        validationAwareView.setTag(createTag(validators));
    }

    /**
     * Create a tag from an array string elements.
     * @param arr An array containing one of: VALID, INVALID.
     * @return A validation tag that can be set on this submit view (no square brackets).
     */
    private static String createTag(String[] arr) {
        return TextUtils.join(",", arr);
    }

    /**
     * Parse the tag from this submit view (no square brackets).
     * @param tag An object set as the tag of this submit view.
     * @return An array containing one of: VALID, INVALID
     */
    private static String[] parseTag(Object tag) {
        return TextUtils.split(tag.toString(), ",");
    }

    /**
     * Get the predicate of this validator.
     * @return A String predicate.
     */
    Predicate<String> getPredicate() {
        return this.predicate;
    }

    /**
     * Update the validation tag for the submit variable, and enable submit if all inputs that are
     * being validated are valid.  Otherwise, show an error on this input and keep this submit
     * disabled.
     * @param isValid Is this input's value valid?
     * @return The result of 'isValid'.
     */
    boolean validate(boolean isValid) {
        String[] tagArray = parseTag(this.submit.getTag());

        // Update tag
        tagArray[inputKey] = isValid ? VALID : INVALID;
        String newTag = createTag(tagArray);
        this.submit.setTag(newTag);

        // Set submit button status
        boolean isAllValid = !newTag.contains(INVALID);
        this.submit.setEnabled(isAllValid);

        // Set error message
        String msg = isValid ? null : this.errorMsg;
        this.input.setError(msg);

        return isValid;
    }
}
