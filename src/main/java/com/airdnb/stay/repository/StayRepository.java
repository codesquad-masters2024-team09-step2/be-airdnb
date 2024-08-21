package com.airdnb.stay.repository;

import com.airdnb.stay.entity.Stay;
import com.airdnb.stay.entity.StayStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StayRepository extends JpaRepository<Stay, Long>, StayQueryRepository {
    Optional<Stay> findByIdAndStatus(Long id, StayStatus status);

    @Query("SELECT s.price FROM Stay s")
    List<Integer> findPriceAll();
}
