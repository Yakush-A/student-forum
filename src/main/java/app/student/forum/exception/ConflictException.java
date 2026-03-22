package app.student.forum.exception;

public class ConflictException extends AppException {
    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
}
