package app.student.forum.repository;

import app.student.forum.model.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    final Map<Long, User> storage = new ConcurrentHashMap<>();
    final AtomicLong idGenerator = new AtomicLong(1);


    public default Optional<User> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public default User save(User user) {
        if(user.getId() == null) {
            user.setId(idGenerator.getAndIncrement());
        }
        storage.put(user.getId(), user);
        return user;
    }
}
