package com.swiftmarket.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditTextValidator {
  // Regular expression for a simple email validation
  private static final String EMAIL_REGEX ="^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
  // Pattern object for the email regex
  private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);
  /**
   * Validates an email address.
   *
   * @param email The email address to validate.
   * @return true if the email is valid, false otherwise.
   */
  public static boolean isValidEmail(String email) {
    if (!isTextFieldValid(email)) { return false; }
    // Create matcher object
    Matcher matcher = pattern.matcher(email);
    // Check if the email matches the pattern
    return matcher.matches();
  }
	 /**
   * Checks if a text field is not null or empty.
   *
   * @param textField The text field to validate.
   * @return true if the text field is not null and not empty, false otherwise.
   */
  public static boolean isTextFieldValid(String textField) {
    return textField != null && !textField.trim().isEmpty();
  }

  /**
   * Checks if any of the provided text fields is null or empty.
   *
   * @param textFields The text fields to validate.
   * @return true if any of the text fields is not null and not empty, false otherwise.
   */
  public static boolean areTextFieldsValid(String... textFields) {
    if (textFields == null) {
      return false;
    }

    for (String textField : textFields) {
      if (textField == null || textField.trim().isEmpty()) {
        return false; // If any field is null or empty, return false
      }
    }

    return true; // All fields are not null and not empty
  }
}






