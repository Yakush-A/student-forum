package app.student.forum.validation;

public final class ValidationConstants {
    public static final int MAX_NAME_LENGTH = 50;
    public static final int MIN_NAME_LENGTH = 1;

    public static final int MAX_CONTENT_LENGTH = 5000;

    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_PASSWORD_LENGTH = 60;

    public static final int MIN_CATEGORY_LENGTH = 1;
    public static final int MAX_CATEGORY_LENGTH = 50;

    public static final int MIN_TAGS_LENGTH = 1;
    public static final int MAX_TAGS_LENGTH = 50;

    public static final int MIN_DESCRIPTION_LENGTH = 10;
    public static final int MAX_DESCRIPTION_LENGTH = 1000;

    private ValidationConstants() {
    }
}
