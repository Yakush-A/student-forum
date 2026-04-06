package app.student.forum.cache;

import org.springframework.data.domain.Pageable;

import java.util.Objects;

public class CommentQueryKey {
    private final Long postId;
    private final Long authorId;
    private final int page;
    private final int size;

    public CommentQueryKey(Long postId, Long userId, Pageable pageable) {
        this.postId = postId;
        this.authorId = userId;
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

        CommentQueryKey that = (CommentQueryKey) o;
        return this.postId.equals(that.postId)
                && this.authorId.equals(that.authorId)
                && this.page == that.page
                && this.size == that.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, authorId, page, size);
    }
}

