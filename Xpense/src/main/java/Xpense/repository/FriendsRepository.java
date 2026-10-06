package Xpense.repository;

import Xpense.models.Friends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FriendsRepository extends JpaRepository<Friends, UUID> {
    List<Friends> findByUserAOrUserB(UUID userA, UUID userB);
    Optional<Friends> findByUserAAndUserB(UUID userA, UUID userB);
}