package com.kstd.api.domain.user.dto;

import com.kstd.api.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "사용자정보")
public class UserDTO {
    @Schema(description = "사용자사번")
    private Long userNo;
    @Schema(description = "사용자명")
    private String name;

    public static UserDTO fromEntity(User user) {
        return UserDTO.builder()
                .userNo(user.getUserNo())
                .name(user.getName()).build();
    }
}
