package Gennet.backend.starrate.controller;

import Gennet.backend.member.entity.Member;
import Gennet.backend.starrate.dto.StarRateDto;
import Gennet.backend.starrate.entity.StarRate;
import Gennet.backend.starrate.service.StarRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/star-rate")
public class StarRateController {
    private final StarRateService starRateService;

    @Autowired
    public StarRateController(StarRateService starRateService) {
        this.starRateService = starRateService;
    }

    // 별점 설정 API
    @PostMapping("/set-rating")
    public ResponseEntity<Void> setStarRate(@RequestBody StarRateDto starRateDto) {
        StarRate starRate = new StarRate();

        Member member = new Member();
        member.setMemberId(starRateDto.getMemberId());
        starRate.setMember(member);

        Member ratedMember = new Member();
        ratedMember.setMemberId(starRateDto.getRatedMemberId());
        starRate.setRatedMember(ratedMember);

        starRate.setRating(starRateDto.getRating());

        starRateService.createStarRate(starRate);

        return ResponseEntity.ok().build();
    }

    // 별점 조회 API
    @GetMapping("/get-rating/{starRateId}")
    public ResponseEntity<Long> getStarRate(@PathVariable Long starRateId) {
        long rating = starRateService.getRatingById(starRateId);
        return ResponseEntity.ok(rating);
    }
}