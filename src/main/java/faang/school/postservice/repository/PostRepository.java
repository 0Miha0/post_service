package faang.school.postservice.repository;

import faang.school.postservice.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByAuthorId(long authorId);

    List<Post> findByProjectId(long projectId);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.likes WHERE p.projectId = :projectId")
    List<Post> findByProjectIdWithLikes(long projectId);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.likes WHERE p.authorId = :authorId")
    List<Post> findByAuthorIdWithLikes(long authorId);

    @Query("SELECT p FROM Post p WHERE p.published = false AND p.deleted = false AND p.scheduledAt <= CURRENT_TIMESTAMP")
    List<Post> findReadyToPublish();

    @Query("SELECT p FROM Post p WHERE p.published = false AND p.deleted = false AND p.scheduledAt >= CURRENT_TIMESTAMP")
    List<Post> findExceedingPosts();

    @Query("SELECT p FROM Post p WHERE p.published = false AND p.deleted = false")
    List<Post> findPublishedFalse(Long useId);

    @Query("SELECT p FROM Post p WHERE p.published = true AND p.deleted = false")
    List<Post> findPublishedTrue(Long useId);

    @Query("select p from Post p " +
            "left join fetch p.comments c " +
            "where p.published = true " +
            "and p.deleted = false " +
            "and p.authorId in :authorIds " +
            "and p.id >= :startPostId " +
            "order by p.id desc")
    List<Post> findPostsByAuthorIds(@Param("authorIds") List<Long> menteesIds, @Param("startPostId") long startPostId,
                                    Pageable pageable);
}
