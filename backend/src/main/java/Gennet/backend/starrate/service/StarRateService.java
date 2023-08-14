//package Gennet.backend.starrate.service;
//
//import Gennet.backend.starrate.dto.StarRateDto;
//import Gennet.backend.starrate.mapper.StarRateMapper;
//import Gennet.backend.starrate.repository.StarRateRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.OptionalDouble;
//
//@Service
//public class StarRateService {
//    private final StarRateRepository starRateRepository;
//    private final StarRateMapper starRateMapper;
//
//    @Autowired
//    public StarRateService(StarRateRepository starRateRepository, StarRateMapper starRateMapper) {
//        this.starRateRepository = starRateRepository;
//        this.starRateMapper = starRateMapper;
//    }
//
//    // 사용자의 평균 별점을 계산하는 메서드
//    public Double calculateAverageStarRateForMember(Long memberId) {
//        List<StarRateDto> starRates = starRateMapper.toDtoList(starRateRepository.findByMemberId(memberId));
//        OptionalDouble average = starRates.stream()
//                .mapToLong(StarRateDto::getStarRate)
//                .average();
//        return average.orElse(0.0); // 기본값은 0.0
//    }
//}
//
