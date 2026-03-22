package app.student.forum.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND("USER_NOT_FOUND", "User not found"),
    TAG_NOT_FOUND("TAG_NOT_FOUND", "Tag not found"),
    CATEGORY_NOT_FOUND("CATEGORY_NOT_FOUND", "Category not found"),
    COMMENT_NOT_FOUND("COMMENT_NOT_FOUND", "Comment not found"),
    POST_NOT_FOUND("POST_NOT_FOUND", "Post not found"),
    ACCESS_DENIED("ACCESS_DENIED", "Access denied"),
    UNAUTHORIZED("UNAUTHORIZED", "Unauthorized"),
    VALIDATION_FAILED("VALIDATION_FAILED", "Validation failed"),
    INVALID_REQUEST("INVALID_REQUEST", "Invalid request"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Internal server error"),
    PASSWORD_MISMATCH("PASSWORD_MISMATCH", "Password does not match"),
    PASSWORD_SAME_AS_OLD("PASSWORD_SAME_AS_OLD", "New password is same as old"),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", "Invalid email or password"),
    TAG_ALREADY_EXISTS("TAG_ALREADY_EXISTS", "Tag already exists"),
    EMAIL_ALREADY_EXISTS("EMAIL_ALREADY_EXISTS", "Email already exists"),
    CATEGORY_ALREADY_EXISTS("CATEGORY_ALREADY_EXISTS", "Category already exists");

    private final String message;
    private final String code;

    ErrorCode(String message, String code) {
        this.message = message;
        this.code = code;
    }
}
