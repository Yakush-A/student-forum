package app.student.forum.service.cache;

import org.springframework.data.domain.Pageable;

import java.util.Objects;

public class CategoryQueryKey {
    private final String categoryName;
    private final int page;
    private final int size;

    public CategoryQueryKey(String categoryName, Pageable pageable) {
        this.categoryName = categoryName;
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
        CategoryQueryKey that = (CategoryQueryKey) o;
        return page == that.page
                && size == that.size
                && Objects.equals(categoryName, that.categoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryName, page, size);
    }

}
