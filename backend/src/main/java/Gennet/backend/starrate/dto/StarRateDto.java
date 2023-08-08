package Gennet.backend.starrate.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StarRateDto {
    private Long starRateId;
    private Long memberId;
    private Long chattingRoomId;
    private Long starRate;
}