package com.airdnb.stay.repository;

import static com.airdnb.stay.entity.QStay.stay;
import static com.querydsl.core.types.dsl.Expressions.booleanTemplate;

import com.airdnb.stay.dto.StayQueryCondition;
import com.airdnb.stay.entity.Stay;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class StayQueryRepositoryImpl implements StayQueryRepository {
    private final JPAQueryFactory query;

    @Override
    public List<Stay> findAll(StayQueryCondition condition) {
        BooleanBuilder builder = new BooleanBuilder();
        addDateCondition(condition, builder);
        addGuestCountCondition(condition, builder);
        addPriceCondition(condition, builder);
        addLocationCondition(condition, builder);

        return query.selectFrom(stay)
                .where(builder)
                .fetch();
    }

    private void addDateCondition(StayQueryCondition condition, BooleanBuilder builder) {
        if (condition.hasDateCondition()) {
            List<LocalDate> reservationDates = condition.getReservationDates();
            builder.andNot(stay.closedStayDates.any().in(reservationDates));
        }
    }

    private void addGuestCountCondition(StayQueryCondition condition, BooleanBuilder builder) {
        if (condition.hasGuestCountCondition()) {
            Integer guestCount = condition.getGuestCount();
            builder.and(stay.maxGuests.goe(guestCount));
        }
    }

    private void addPriceCondition(StayQueryCondition condition, BooleanBuilder builder) {
        if (condition.hasPriceCondition()) {
            Integer minPrice = condition.getMinPrice();
            Integer maxPrice = condition.getMaxPrice();
            builder.and(stay.price.goe(minPrice))
                    .and(stay.price.loe(maxPrice));
        }
    }

    private void addLocationCondition(StayQueryCondition condition, BooleanBuilder builder) {
        if (condition.hasLocationCondition()) {
            Double latitude = condition.getLatitude();
            Double longitude = condition.getLongitude();
            Integer distance = condition.getDistance();

            BooleanTemplate distanceCondition = booleanTemplate(
                    "ST_Distance_Sphere(point({0}, {1}), point({2}, {3})) < {4}",
                    longitude, latitude, stay.location.longitude, stay.location.latitude, distance);
            builder.and(distanceCondition);
        }
    }
}
