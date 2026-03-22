package app.student.forum.exception;

public class UnauthorizedException extends AppException {
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
