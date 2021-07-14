package com.akarinti.preapproved.dto.authentication.uidm.userDetail;

import com.akarinti.preapproved.dto.authentication.ProfileUserDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDetailResponseDTO {
    @JsonProperty(value="user_detail")
    private ProfileUserDTO userDetail;
}
