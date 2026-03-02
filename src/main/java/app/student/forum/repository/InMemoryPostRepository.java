package app.student.forum.repository;

import app.student.forum.model.entity.Post;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryPostRepository implements PostRepository {

    private final Map<Long, Post> storage = new HashMap<>();

    InMemoryPostRepository() {
        this.save(new Post("Yakush-A", "ОСиСП ЛР №2. Нужна теория, как подключиться ftp"));
        this.save(new Post("PG_YI", "Тэц (помогите)"));
        this.save(new Post("yakush", "Проблема с Arch linux не работает GRUB"));
    }

    @Override
    public List<Post> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Post findById(Long id) {
        return storage.get(id);
    }

    @Override
    public void save(Post post) {
        storage.put(post.getId(), post);
    }

    @Override
    public List<Post> findByAuthor(String author) {
        return storage.values().stream()
                .filter(post -> post.getAuthor().equals(author))
                .toList();
    }
}
