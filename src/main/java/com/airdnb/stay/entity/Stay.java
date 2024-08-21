package com.airdnb.stay.entity;

import com.airdnb.image.entity.Image;
import com.airdnb.member.entity.Member;
import com.airdnb.reservation.entity.ReservationPeriod;
import com.airdnb.staytag.StayTag;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Stay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer price;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer maxGuests;
    @Embedded
    private Location location;
    @Enumerated(EnumType.STRING)
    private StayStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private Member host;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "img_id")
    private Image image;
    @Enumerated(EnumType.STRING)
    private StayType type;
    @OneToMany(mappedBy = "stay")
    private List<StayTag> stayTags = new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "closed_stay_date", joinColumns = @JoinColumn(name = "stay_id"))
    @Column(name = "date")
    private List<LocalDate> closedStayDates = new ArrayList<>();

    @Builder
    public Stay(String name, Integer price, LocalDateTime startDate, LocalDateTime endDate, Integer maxGuests,
                Location location, StayStatus status, Member host, Image image, StayType type) {
        this.name = name;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxGuests = maxGuests;
        this.location = location;
        this.status = status;
        this.host = host;
        this.image = image;
        this.type = type;
    }

    public void softDelete() {
        this.status = StayStatus.DELETED;
    }

    public boolean hasSameHostId(String hostId) {
        return this.host.getId().equals(hostId);
    }

    public boolean isSatisfyingPeriod(ReservationPeriod reservationPeriod) {
        LocalDateTime checkinAt = reservationPeriod.getCheckinAt();
        LocalDateTime checkoutAt = reservationPeriod.getCheckoutAt();
        if (checkinAt.isBefore(startDate) || checkoutAt.isAfter(endDate)) { // 숙소 게시기간 만족 여부 확인
            return false;
        }
        if (hasClosedDate(reservationPeriod.getReservationDates())) { // 이미 예약되었거나 사용불가 기간인지 확인
            return false;
        }
        return true;
    }

    public void addClosedDates(List<LocalDate> dates) {
        closedStayDates.addAll(dates);
    }

    public void removeClosedDate(List<LocalDate> reservationDates) {
        closedStayDates.removeAll(reservationDates);
    }

    private boolean hasClosedDate(List<LocalDate> reservationDates) {
        return closedStayDates.stream()
                .anyMatch(reservationDates::contains);
    }
}
