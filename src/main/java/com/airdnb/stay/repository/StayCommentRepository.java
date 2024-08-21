package com.airdnb.stay.repository;

import com.airdnb.stay.entity.CommentStatus;
import com.airdnb.stay.entity.StayComment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StayCommentRepository extends JpaRepository<StayComment, Long> {
    List<StayComment> findCommentsByStayIdAndStatus(Long stayId, CommentStatus status);

    @Query("SELECT AVG(sc.rating) FROM StayComment sc WHERE sc.stay.id = :stayId AND sc.status = :status")
    Double findCommentRatingAvg(@Param("stayId") Long stayId, @Param("status") CommentStatus status);

    Integer countByStayIdAndStatus(Long stayId, CommentStatus status);
}
