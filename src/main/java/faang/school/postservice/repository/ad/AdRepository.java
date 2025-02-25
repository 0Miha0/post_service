package faang.school.postservice.repository.ad;

import faang.school.postservice.model.ad.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AdRepository extends JpaRepository<Ad, Long> {

    @Query("SELECT a FROM Ad a WHERE a.post.id = ?1")
    Optional<Ad> findByPostId(long postId);

    List<Ad> findAllByBuyerId(long buyerId);

    @Query("SELECT a FROM Ad a WHERE a.appearancesLeft = 0 AND a.endDate < ?1")
    Optional<List<Ad>> findExpiredAdsWithNoAppearancesLeft(LocalDateTime currentDateTime);
}
