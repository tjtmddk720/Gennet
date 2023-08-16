package Gennet.backend.starrate.service;

import Gennet.backend.starrate.entity.StarRate;
import Gennet.backend.starrate.repository.StarRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StarRateService {
    private final StarRateRepository starRateRepository;

    @Autowired
    public StarRateService(StarRateRepository starRateRepository) {
        this.starRateRepository = starRateRepository;
    }

    public void createStarRate(StarRate starRate) {
        starRateRepository.save(starRate);
    }

    public long getRatingById(Long starRateId) {
        StarRate starRate = starRateRepository.findById(starRateId).orElse(null);
        return (starRate != null) ? starRate.getRating() : 0L;
    }
}
