package com.airdnb.reservation.repository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ReservationLockRepository {
    private static final String LOCKING_KEY = "locked_reservation";
    private static final String LOCKING_VALUE_FORMAT = "%s:%s";
    private final StringRedisTemplate redisTemplate;

    public boolean lock(Long stayId, List<LocalDate> dates) {
        Set<String> addedDates = new HashSet<>();

        for (LocalDate date : dates) {
            String lockingValue = getLockingValue(stayId, date);
            Long added = redisTemplate.opsForSet().add(LOCKING_KEY, lockingValue);
            if (added != 1) { // 이미 다른 사람이 점유하였을 경우
                rollback(addedDates);
                return false;
            }
            addedDates.add(lockingValue);
        }
        return true;
    }

    private String getLockingValue(Long stayId, LocalDate date) {
        return String.format(LOCKING_VALUE_FORMAT, stayId, date);
    }

    private void rollback(Set<String> addedDates) {
        for (String value : addedDates) {
            redisTemplate.opsForSet().remove(LOCKING_KEY, value);
        }
    }
    
}
