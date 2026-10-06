package Xpense.repository;

import Xpense.models.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserChallengeRepository extends JpaRepository<UserChallenge, UUID> {
    List<UserChallenge> findByUserId(UUID userId);
    Optional<UserChallenge> findByUserIdAndChallengeId(UUID userId, UUID challengeId);
}