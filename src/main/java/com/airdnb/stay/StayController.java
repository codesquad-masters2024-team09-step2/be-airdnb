package com.airdnb.stay;

import com.airdnb.global.ApiResponse;
import com.airdnb.global.UriMaker;
import com.airdnb.stay.dto.StayCreate;
import com.airdnb.stay.dto.StayCreateRequest;
import com.airdnb.stay.dto.StayDetailQuery;
import com.airdnb.stay.dto.StayDetailQueryResponse;
import com.airdnb.stay.dto.StayListQuery;
import com.airdnb.stay.dto.StayListQueryResponse;
import com.airdnb.stay.dto.StayPriceListQuery;
import com.airdnb.stay.dto.StayPriceListQueryResponse;
import com.airdnb.stay.dto.StayQueryCondition;
import com.airdnb.stay.dto.StayQueryConditionRequest;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/stays")
public class StayController {
    private final StayService stayService;

    @PostMapping
    public ResponseEntity<ApiResponse> createStay(@Valid @RequestBody StayCreateRequest stayCreateRequest) {
        StayCreate stayCreate = stayCreateRequest.toStayCreate();

        Long stayId = stayService.createStay(stayCreate);

        URI location = UriMaker.makeUri(stayId);
        return ResponseEntity.created(location).body(ApiResponse.success(null));
    }

    @GetMapping("/{id}")
    public ApiResponse queryStayDetail(@PathVariable Long id) {
        StayDetailQuery stayDetailQuery = stayService.queryStayDetailById(id);
        return ApiResponse.success(StayDetailQueryResponse.from(stayDetailQuery));
    }

    @GetMapping
    public ApiResponse queryStayList(@ModelAttribute StayQueryConditionRequest request) {
        StayQueryCondition condition = request.toStayQueryCondition();
        List<StayListQuery> queries = stayService.queryStayList(condition);
        List<StayListQueryResponse> responses = queries.stream()
                .map(StayListQueryResponse::from)
                .toList();
        return ApiResponse.success(responses);
    }

    @GetMapping("/prices")
    public ApiResponse queryStayPriceList() {
        StayPriceListQuery query = stayService.queryStayPriceList();
        return ApiResponse.success(StayPriceListQueryResponse.from(query));
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteStay(@PathVariable Long id) {
        stayService.softDeleteStay(id);
        return ApiResponse.success(null);
    }
}
