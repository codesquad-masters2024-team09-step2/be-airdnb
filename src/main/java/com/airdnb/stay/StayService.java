package com.airdnb.stay;

import com.airdnb.global.exception.ForbiddenException;
import com.airdnb.global.exception.NotFoundException;
import com.airdnb.image.ImageService;
import com.airdnb.image.entity.Image;
import com.airdnb.member.MemberService;
import com.airdnb.member.entity.Member;
import com.airdnb.stay.dto.StayCommentDetail;
import com.airdnb.stay.dto.StayCreate;
import com.airdnb.stay.dto.StayDetailQuery;
import com.airdnb.stay.dto.StayListQuery;
import com.airdnb.stay.dto.StayPriceListQuery;
import com.airdnb.stay.dto.StayQueryCondition;
import com.airdnb.stay.entity.CommentStatus;
import com.airdnb.stay.entity.Location;
import com.airdnb.stay.entity.Stay;
import com.airdnb.stay.entity.StayComment;
import com.airdnb.stay.entity.StayStatus;
import com.airdnb.stay.entity.StayType;
import com.airdnb.stay.repository.StayCommentRepository;
import com.airdnb.stay.repository.StayRepository;
import com.airdnb.staytag.StayTag;
import com.airdnb.staytag.StayTagService;
import com.airdnb.tag.entity.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class StayService {
    private final StayRepository stayRepository;
    private final StayCommentRepository stayCommentRepository;
    private final StayTagService stayTagService;
    private final ImageService imageService;
    private final MemberService memberService;

    public Long createStay(StayCreate stayCreate) {
        Stay stay = buildStay(stayCreate);
        stayRepository.save(stay);

        stayTagService.createStayTags(stay, stayCreate.getTagIds());
        return stay.getId();
    }

    @Transactional(readOnly = true)
    public StayDetailQuery queryStayDetailById(Long id) {
        Stay stay = findActiveStayById(id);

        return StayDetailQuery.builder()
                .id(stay.getId())
                .name(stay.getName())
                .hostName(stay.getHost().getName())
                .imageUrl(stay.getImage().getUrl())
                .price(stay.getPrice())
                .maxGuests(stay.getMaxGuests())
                .address(stay.getLocation().getAddress())
                .latitude(stay.getLocation().getLatitude())
                .longitude(stay.getLocation().getLongitude())
                .startDate(stay.getStartDate())
                .endDate(stay.getEndDate())
                .type(stay.getType().name())
                .tagNames(getTagNames(stay))
                .comments(getActiveCommentDetails(stay))
                .rating(getActiveRating(stay))
                .closedDates(stay.getClosedStayDates())
                .build();
    }

    @Transactional(readOnly = true)
    public Stay findActiveStayById(Long id) {
        return stayRepository.findByIdAndStatus(id, StayStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("id와 일치하는 숙소를 찾을 수 없습니다."));
    }

    public void softDeleteStay(Long id) {
        Stay stay = findActiveStayById(id);
        String currentMemberId = memberService.getCurrentMemberId();
        if (!stay.hasSameHostId(currentMemberId)) {
            throw new ForbiddenException("숙소 삭제 권한이 없습니다.");
        }
        stay.softDelete();
    }

    @Transactional(readOnly = true)
    public List<StayListQuery> queryStayList(StayQueryCondition condition) {
        List<Stay> stays = stayRepository.findAll(condition);
        List<StayListQuery> responses = new ArrayList<>();
        for (Stay stay : stays) {
            Double activeRating = getActiveRating(stay);
            Integer activeCommentCount = getActiveCommentCount(stay);
            List<String> tagNames = getTagNames(stay);
            StayListQuery response = toStayListQuery(stay, tagNames, activeRating,
                    activeCommentCount);
            responses.add(response);
        }
        return responses;
    }

    @Transactional(readOnly = true)
    public StayPriceListQuery queryStayPriceList() {
        List<Integer> prices = stayRepository.findPriceAll();
        Map<Integer, Long> countPerPrice = getCountPerPrice(prices);
        int minPrice = calculateMinPrice(prices);
        int maxPrice = calculateMaxPrice(prices);
        int avgPrice = calculateAvgPrice(prices);

        return new StayPriceListQuery(countPerPrice, minPrice, maxPrice, avgPrice);
    }


    private Stay buildStay(StayCreate stayCreateRequest) {
        Image image = getImage(stayCreateRequest.getImageId());
        Member host = getHost();
        StayType stayType = StayType.of(stayCreateRequest.getType());
        Location location = new Location(stayCreateRequest.getAddress(), stayCreateRequest.getLatitude(),
                stayCreateRequest.getLongitude());

        return Stay.builder()
                .name(stayCreateRequest.getName())
                .image(image)
                .price(stayCreateRequest.getPrice())
                .host(host)
                .startDate(stayCreateRequest.getStartDate())
                .endDate(stayCreateRequest.getEndDate())
                .status(StayStatus.ACTIVE)
                .maxGuests(stayCreateRequest.getMaxGuests())
                .location(location)
                .type(stayType)
                .build();
    }

    private Member getHost() {
        String currentMemberId = memberService.getCurrentMemberId();
        return memberService.findMemberById(currentMemberId);
    }

    private Image getImage(Long imageId) {
        if (imageId == null) {
            return null;
        }
        return imageService.findImageById(imageId);
    }

    private List<String> getTagNames(Stay stay) {
        return stay.getStayTags().stream()
                .map(StayTag::getTag)
                .map(Tag::getName)
                .toList();
    }

    private List<StayCommentDetail> getActiveCommentDetails(Stay stay) {
        List<StayComment> comments = stayCommentRepository.findCommentsByStayIdAndStatus(stay.getId(),
                CommentStatus.ACTIVE);

        return comments.stream()
                .map(comment -> StayCommentDetail.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .writer(comment.getWriter())
                        .createdAt(comment.getCreatedAt())
                        .rating(comment.getRating())
                        .build())
                .toList();
    }

    private Double getActiveRating(Stay stay) {
        return stayCommentRepository.findCommentRatingAvg(stay.getId(), CommentStatus.ACTIVE);
    }

    private Integer getActiveCommentCount(Stay stay) {
        return stayCommentRepository.countByStayIdAndStatus(stay.getId(), CommentStatus.ACTIVE);
    }

    private StayListQuery toStayListQuery(Stay stay, List<String> tagNames, Double activeRating,
                                          Integer activeCommentCount) {
        return StayListQuery.builder()
                .id(stay.getId())
                .name(stay.getName())
                .price(stay.getPrice())
                .type(stay.getType())
                .address(stay.getLocation().getAddress())
                .latitude(stay.getLocation().getLatitude())
                .longitude(stay.getLocation().getLongitude())
                .maxGuests(stay.getMaxGuests())
                .tagNames(tagNames)
                .imageUrl(stay.getImage().getUrl())
                .rating(activeRating)
                .commentCount(activeCommentCount)
                .build();
    }

    private int calculateAvgPrice(List<Integer> prices) {
        return (int) prices.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
    }

    private int calculateMaxPrice(List<Integer> prices) {
        return prices.stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);
    }

    private int calculateMinPrice(List<Integer> prices) {
        return prices.stream()
                .mapToInt(Integer::intValue)
                .min()
                .orElse(0);
    }

    private Map<Integer, Long> getCountPerPrice(List<Integer> prices) {
        return prices.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
}
