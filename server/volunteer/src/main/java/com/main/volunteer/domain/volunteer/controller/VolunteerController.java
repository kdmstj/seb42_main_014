package com.main.volunteer.domain.volunteer.controller;


import com.main.volunteer.auth.CustomUserDetails;
import com.main.volunteer.domain.apply.entity.Apply;
import com.main.volunteer.domain.member.service.MemberService;
import com.main.volunteer.domain.review.entity.Review;
import com.main.volunteer.domain.review.mapper.ReviewMapper;
import com.main.volunteer.domain.review.service.ReviewService;
import com.main.volunteer.domain.volunteer.service.VolunteerService;
import com.main.volunteer.response.ApiResponse;
import com.main.volunteer.domain.tag.entity.Tag;
import com.main.volunteer.domain.tag.service.TagService;
import com.main.volunteer.util.UriUtil;
import com.main.volunteer.domain.volunteer.dto.VolunteerDto;
import com.main.volunteer.domain.volunteer.entity.Volunteer;
import com.main.volunteer.domain.volunteer.mapper.VolunteerMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.websocket.server.PathParam;
import java.net.URI;
import java.util.List;
import java.util.Objects;


@Slf4j
@RestController
@RequestMapping("/volunteers")
public class VolunteerController {
    public static final String DEFAULT_URI = "/volunteers";

    private final VolunteerMapper volunteerMapper;
    private final ReviewMapper reviewMapper;
    private final VolunteerService volunteerService;
    private final ReviewService reviewService;
    private final TagService tagService;
    private final MemberService memberService;

    public VolunteerController(VolunteerMapper volunteerMapper, ReviewMapper reviewMapper, VolunteerService volunteerService, ReviewService reviewService, TagService tagService, MemberService memberService) {
        this.volunteerMapper = volunteerMapper;
        this.reviewMapper = reviewMapper;
        this.volunteerService = volunteerService;
        this.reviewService = reviewService;
        this.tagService = tagService;
        this.memberService = memberService;
    }


    /**
    봉사 등록 - 봉사 기관만 가능
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<?> postVolunteer(@RequestBody @Valid VolunteerDto.Post postDto, @AuthenticationPrincipal CustomUserDetails userDetails){


        Volunteer volunteer = volunteerMapper.postDtoToVolunteer(postDto);

        Tag tag = tagService.getTagId(postDto.getTagId());
        volunteer.setTag(tag);
        volunteer.setMember(memberService.findMember(userDetails.getMemberId()));

        Volunteer createdVolunteer = volunteerService.createVolunteer(volunteer);

        URI uri = UriUtil.createUri(DEFAULT_URI, createdVolunteer.getVolunteerId());
        return ResponseEntity.created(uri).body(ApiResponse.created("data", volunteerMapper.volunteerToResponseDto(createdVolunteer)));
    }

    /**
    봉사 삭제
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{volunteer-id}")
    public ResponseEntity<?> deleteVolunteer(@Positive @PathVariable("volunteer-id") Long volunteerId, @AuthenticationPrincipal CustomUserDetails userDetails){

        volunteerService.deleteVolunteer(volunteerId, memberService.findMember(userDetails.getMemberId()));

        return ResponseEntity.noContent().build();
    }
    /**
    기관이 등록한 봉사 목록 조회
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/organization")
    public ResponseEntity<?> getVolunteerListByOrg(@AuthenticationPrincipal CustomUserDetails userDetails){

        List<Volunteer> volunteerList = volunteerService.getVolunteerListByOrg(userDetails);

        return ResponseEntity.ok().body(ApiResponse.ok("data", volunteerMapper.volunteerListToResponseList(volunteerList)));
    }

    /**
    봉사 상세 조회(리뷰 목록 포함)
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{volunteer-id}")
    public ResponseEntity<?> getVolunteer(@PathVariable("volunteer-id") @Positive Long volunteerId, @AuthenticationPrincipal CustomUserDetails userDetails){
        Volunteer volunteer = volunteerService.getVolunteer(volunteerId);

        boolean applied = false;
        List<Apply> applyList = volunteer.getApplyList();
        for (Apply apply : applyList) {
            if (Objects.equals(userDetails.getMemberId(), apply.getMember().getMemberId())) {
                applied = true;
                break;
            }
        }

        List<Review> reviewList = reviewService.getReviewList(volunteerId);

        return ResponseEntity.ok().body(ApiResponse.ok("volunteer", volunteerMapper.volunteerToResponseDto(volunteer),
                "reviewList", reviewMapper.reviewListToResponseList(reviewList), "applied", applied));
    }



    /**
    봉사 목록 조회 - ALL
     */
    @GetMapping
    public ResponseEntity<?> getVolunteerList(){

        List<Volunteer> volunteerList = volunteerService.getVolunteerList();

        return ResponseEntity.ok().body(ApiResponse.ok("data", volunteerMapper.volunteerListToResponseList(volunteerList)));
    }

//    /**
//    봉사명으로 조회
//     */
//    @GetMapping("/title")
//    public ResponseEntity<?> searchVolunteerByTitle(@PathParam("title") String keyword){
//
//        List<Volunteer> volunteerList = volunteerService.searchByVolunteerTitle(keyword);
//
//        return ResponseEntity.ok().body(ApiResponse.ok("data", volunteerMapper.volunteerListToResponseList(volunteerList)));
//    }
//
//    /**
//    기관명으로 조회
//     */
//    @GetMapping("/organizationName")
//    public ResponseEntity<?> searchVolunteerByOrganization(@PathParam("organization") String keyword){
//
//        List<Volunteer> volunteerList = volunteerService.searchByOrganizationName(keyword);
//
//        return ResponseEntity.ok().body(ApiResponse.ok("data", volunteerMapper.volunteerListToResponseList(volunteerList)));
//
//    }

}
