package com.example.myProject1.status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class StatusDto {
    private Long id;
    private String name;
    private String reason;
    public static StatusDto dto (Status status){
        StatusDto statusDto= StatusDto.builder()
                .id(status.getId())
                .name(status.getName())
                .reason(status.getReason())
                .build();
        return statusDto;
    }
}
