package Xpense.repository;

import Xpense.models.XPEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface XPEventRepository extends JpaRepository<XPEvent, UUID> {
    List<XPEvent> findByUserId(UUID userId);
}