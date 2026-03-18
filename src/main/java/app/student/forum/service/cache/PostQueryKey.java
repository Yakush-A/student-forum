package app.student.forum.service.cache;

import org.springframework.data.domain.Pageable;

import java.util.Objects;

public class PostQueryKey {
    private final Long categoryId;
    private final Long authorId;
    private final int size;
    private final int page;

    public PostQueryKey(Long categoryId, Long authorId, Pageable pageable) {
        this.categoryId = categoryId;
        this.authorId = authorId;
        this.page = pageable.getPageNumber();
        this.size = pageable.getPageSize();
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
                && Objects.equals(authorId, that.authorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, authorId, page, size);
    }
}
