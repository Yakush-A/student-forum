package app.student.forum.cache;

import org.springframework.data.domain.Pageable;

import java.util.Objects;

public class TagQueryKey {
    private final String tagName;
    private final int page;
    private final int size;

    public TagQueryKey(String tagName, Pageable pageable) {
        this.tagName = tagName;
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
        TagQueryKey that = (TagQueryKey) o;
        return page == that.page
                && size == that.size
                && Objects.equals(tagName, that.tagName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagName, page, size);
    }
}
