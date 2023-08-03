package com.ssafy.backend.domain.review.controller;

import com.ssafy.backend.domain.common.BasicResponse;
import com.ssafy.backend.domain.review.dto.ReviewModifyDto;
import com.ssafy.backend.domain.review.dto.ReviewResultDto;
import com.ssafy.backend.domain.review.dto.ReviewRegistDto;
import com.ssafy.backend.domain.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Tag(name = "후기 API", description = "상품에 대한 리뷰를 가져오는 API")
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "후기 하나 등록하기", description = "새로운 후기 하나를 쓸 수 있습니다.")
    @Parameter(name = "ReviewRegistDto", description = "dto를 넘겨주세요")
    @PostMapping("/review")
    public ResponseEntity<BasicResponse> registReview(@RequestBody ReviewRegistDto reviewRegistDto) {
        reviewService.registReview(reviewRegistDto);

        BasicResponse basicResponse = BasicResponse.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("리뷰 등록 성공").build();

        return new ResponseEntity<>(basicResponse, basicResponse.getHttpStatus());
    }

    @Operation(summary = "상품 후기 한 페이지 조회하기", description = "상품 아이디와 페이징 정보에 해당하는 모든 후기를 불러옵니다.")
    @Parameter(name = "/review?page=1&size=3&productId", description = "page : 페이지 번호, size : 페이지당 후기 개수 . 해당 상품 아이디")
    @GetMapping("/review/{productId}")
    public ResponseEntity<?> getAllProductReview(Pageable pageable, @PathVariable Long productId) { //pageNumber, pageSize, offset
        List<ReviewResultDto> reviewList = reviewService.getAllProductReview(pageable, productId);
        BasicResponse basicResponse;
        if (reviewList == null) {
            basicResponse = BasicResponse.builder()
                    .code(HttpStatus.NO_CONTENT.value())
                    .httpStatus(HttpStatus.NO_CONTENT)
                    .message("상품에 대한 한 페이지 리뷰 조회 실패")
                    .count(0).build();
        } else {
            basicResponse = BasicResponse.builder()
                    .code(HttpStatus.OK.value())
                    .httpStatus(HttpStatus.OK)
                    .message("상품에 대한 한 페이지 리뷰 조회 성공")
                    .count(reviewList.size())
                    .result(Collections.singletonList(reviewList)).build();
        }
        return new ResponseEntity<BasicResponse>(basicResponse, basicResponse.getHttpStatus());
    }

    @Operation(summary = "회원이 쓴 상품 후기 한 페이지 조회하기", description = "로그인한 유저와 페이징 정보에 해당하는 모든 후기를 불러옵니다.")
    @Parameter(name = "/review?page=1&size=3", description = "page : 페이지 번호, size : 페이지당 후기 개수 ")
    @GetMapping("/review")
    public ResponseEntity<?> getAllUserReview(Pageable pageable) { //pageNumber, pageSize, offset
        List<ReviewResultDto> reviewList = reviewService.getAllUserReview(pageable);
        BasicResponse basicResponse;
        if (reviewList == null) {
            basicResponse = BasicResponse.builder()
                    .code(HttpStatus.NO_CONTENT.value())
                    .httpStatus(HttpStatus.NO_CONTENT)
                    .message("회원에 대한 한 페이지 리뷰 조회 실패")
                    .count(0).build();
        } else {
            basicResponse = BasicResponse.builder()
                    .code(HttpStatus.OK.value())
                    .httpStatus(HttpStatus.OK)
                    .message("회원에 대한 한 페이지 리뷰 조회 성공")
                    .count(reviewList.size())
                    .result(Collections.singletonList(reviewList)).build();
        }
        return new ResponseEntity<BasicResponse>(basicResponse, basicResponse.getHttpStatus());
    }

    @Operation(summary = "후기 하나 수정하기", description = "후기를 수정합니다.")
    @Parameter(name = "reviewId, ReviewModifyDto", description = "변경 가능한 것 : 별점, 이미지, 내용")
    @PutMapping("/review/{reviewId}")
    public ResponseEntity<?> modifyReview(@PathVariable Long reviewId, @RequestBody ReviewModifyDto reviewModifyDto) throws Throwable {
        reviewService.modifyReview(reviewId, reviewModifyDto);

        BasicResponse basicResponse = BasicResponse.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("후기 수정 성공").build();

        return new ResponseEntity<>(basicResponse, basicResponse.getHttpStatus());
    }

    @Operation(summary = "후기 하나 삭제하기", description = "후기 하나를 삭제합니다.")
    @Parameter(name = "reviewId", description = "삭제할 후기 아이디를 보내주세요")
    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);

        BasicResponse basicResponse = BasicResponse.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("후기 삭제 성공").build();

        return new ResponseEntity<>(basicResponse, basicResponse.getHttpStatus());
    }
}
