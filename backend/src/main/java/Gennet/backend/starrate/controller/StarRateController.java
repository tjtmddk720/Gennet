//package Gennet.backend.starrate.controller;
//
//import Gennet.backend.starrate.dto.StarRateDto;
//import Gennet.backend.starrate.service.StarRateService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/star")
//public class StarRateController {
//    private final StarRateService starRateService;
//
//    @Autowired
//    public StarRateController(StarRateService starRateService) {
//        this.starRateService = starRateService;
//    }
//
//    // 사용자의 평균 별점 반환 API
//    @GetMapping("/average/{memberId}")
//    public ResponseEntity<Double> getAverageStarRateForMember(@PathVariable Long memberId) {
//        Double averageStarRate = starRateService.calculateAverageStarRateForMember(memberId);
//        return ResponseEntity.ok(averageStarRate);
//    }
//}