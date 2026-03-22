package app.student.forum.exception;

public class AccessDeniedException extends AppException {
    public AccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
