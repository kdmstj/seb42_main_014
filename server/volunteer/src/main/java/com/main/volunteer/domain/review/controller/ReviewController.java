package com.main.volunteer.domain.review.controller;

import com.main.volunteer.domain.member.entity.Member;
import com.main.volunteer.domain.review.dto.ReviewDto;
import com.main.volunteer.domain.review.entity.Review;
import com.main.volunteer.domain.review.mapper.ReviewMapper;
import com.main.volunteer.domain.review.service.ReviewService;
import com.main.volunteer.response.ApiResponse;
import com.main.volunteer.util.UriUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    public static final String DEFAULT_URI = "/reviews";

    private final ReviewMapper reviewMapper;
    private final ReviewService reviewService;


    public ReviewController(ReviewMapper reviewMapper, ReviewService reviewService) {
        this.reviewMapper = reviewMapper;
        this.reviewService = reviewService;
    }


    @PostMapping("/{volunteer-id}")
    public ResponseEntity<?> postReview(@RequestBody @Valid ReviewDto.Post postDto, @PathVariable("volunteer-id") Long volunteerId, @PathParam("member-id") Long memberId) {

        Member member = new Member();
        member.setMemberId(memberId);

        Review review = reviewMapper.postDtoToReview(postDto);
        review.setMember(member);

        Review createdReview = reviewService.createReview(review, volunteerId);

        URI uri = UriUtil.createUri(DEFAULT_URI, createdReview.getReviewId());
        return ResponseEntity.created(uri).body(ApiResponse.created("data", reviewMapper.reviewToResponse(createdReview)));

    }

    /*
    내가 쓴 후기 목록 보기
     */
    @GetMapping("/my")
    public ResponseEntity<?> getMyReviews(@PathParam("member-id") Long memberId) {

        Member member = new Member();
        member.setMemberId(memberId);

        List<Review> MyReviewList = reviewService.getMyReviewList(member);

        return ResponseEntity.ok().body(ApiResponse.ok("data", reviewMapper.reviewListToResponseList(MyReviewList)));
    }

    /*
    특정 봉사활동에서 내가 쓴 후기 보기
     */
    @GetMapping("/{volunteer-id}/my")
    public ResponseEntity<?> getMyReview(@PathVariable("volunteer-id") Long volunteerId, @PathParam("member-id") Long memberId){

        Member member = new Member();
        member.setMemberId(memberId);

        Review myReview = reviewService.getMyReview(volunteerId, member);

        return ResponseEntity.ok().body(ApiResponse.ok("data", reviewMapper.reviewToResponse(myReview)));
    }

    /*
    특정 봉사활동에서 리뷰 목록 가져오기
     */
    @GetMapping("/{volunteer-id}")
    public ResponseEntity<?> getReviews(@PathVariable("volunteer-id") Long volunteerId){

        List<Review> reviewList = reviewService.getReviewList(volunteerId);
        return ResponseEntity.ok().body(ApiResponse.ok("data", reviewMapper.reviewListToResponseList(reviewList)));
    }

    @PatchMapping("/{review-id}")
    public ResponseEntity<?> patchMyReview(@PathVariable("review-id") Long reviewId, @RequestBody ReviewDto.Patch patchDto, @PathParam("member-id") Long memberId) {
        Member member = new Member();
        member.setMemberId(memberId);

        Review review = reviewMapper.patchDtoToReview(patchDto);
        review.setReviewId(reviewId);
        review.setMember(member);

        Review patchedMyReview = reviewService.patchReview(review);

        return ResponseEntity.ok().body(ApiResponse.ok("data", reviewMapper.reviewToResponse(patchedMyReview)));
    }

    @DeleteMapping("/{review-id}")
    public ResponseEntity<?> deleteMyReview(@PathVariable("review-id") Long reviewId, @PathParam("member-id") Long memberId) {
        Member member = new Member();
        member.setMemberId(memberId);

        reviewService.deleteReview(reviewId, memberId);

        return ResponseEntity.noContent().build();
    }
}
