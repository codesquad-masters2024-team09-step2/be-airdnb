package com.airdnb.reservation;

import com.airdnb.global.ApiResponse;
import com.airdnb.global.UriMaker;
import com.airdnb.reservation.dto.ReservationCreate;
import com.airdnb.reservation.dto.ReservationCreateRequest;
import com.airdnb.reservation.dto.ReservationQuery;
import com.airdnb.reservation.dto.ReservationQueryResponse;
import com.airdnb.security.SecurityMemberProvider;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ApiResponse> createReservation(
            @Valid @RequestBody ReservationCreateRequest reservationCreateRequest) {

        String currentMemberId = SecurityMemberProvider.getCurrentMemberId();
        
        ReservationCreate reservationCreate = reservationCreateRequest.toReservationCreate(currentMemberId);

        Long reservationId = reservationService.createReservation(reservationCreate);

        URI location = UriMaker.makeUri(reservationId);
        return ResponseEntity.created(location).body(ApiResponse.success(null));
    }

    @GetMapping("/{id}")
    public ApiResponse queryReservationDetail(@PathVariable Long id) {
        ReservationQuery reservationQuery = reservationService.queryReservationDetail(id);
        return ApiResponse.success(ReservationQueryResponse.from(reservationQuery));
    }

    @PatchMapping("/{id}")
    public ApiResponse updateReservationStatus(@PathVariable Long id, @RequestParam String status) {
        reservationService.updateReservationStatus(id, status);
        return ApiResponse.success(null);
    }
}
