package faang.school.postservice.repository;

import faang.school.postservice.model.Like;
import faang.school.postservice.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {

}
