package app.student.forum.cache;

import org.springframework.data.domain.Pageable;

import java.util.Objects;

public class PostQueryKey {
    private final Long categoryId;
    private final String categoryName;
    private final Long authorId;
    private final int size;
    private final int page;

    public PostQueryKey(Long categoryId, String categoryName, Long authorId, Pageable pageable) {
        this.categoryId = categoryId;
        this.authorId = authorId;
        this.page = pageable.getPageNumber();
        this.size = pageable.getPageSize();
        this.categoryName = categoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PostQueryKey that = (PostQueryKey) o;

        return page == that.page
                && size == that.size
                && Objects.equals(categoryId, that.categoryId)
                && Objects.equals(authorId, that.authorId)
                && Objects.equals(categoryName, that.categoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, categoryName, authorId, page, size);
    }
}
