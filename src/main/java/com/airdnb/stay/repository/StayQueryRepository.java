package com.airdnb.stay.repository;

import com.airdnb.stay.dto.StayQueryCondition;
import com.airdnb.stay.entity.Stay;
import java.util.List;

public interface StayQueryRepository {
    List<Stay> findAll(StayQueryCondition condition);
}
