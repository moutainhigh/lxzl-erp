package com.lxzl.erp.common.domain.dingding.member;

import com.lxzl.erp.common.domain.dingding.DingdingBaseDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author daiqi
 * @create 2018-06-04 11:41
 */
public class DingdingUsersDTO extends DingdingBaseDTO {

    private List<DingdingUserDTO> dingdingUserDTOs;

    public DingdingUsersDTO(List<DingdingUserDTO> dingdingUserDTOs) {
        this.dingdingUserDTOs = dingdingUserDTOs;
    }

    public DingdingUsersDTO(DingdingUserDTO dingdingUserDTO) {
        dingdingUserDTOs = new ArrayList<>();
        dingdingUserDTOs.add(dingdingUserDTO);
    }

    public List<DingdingUserDTO> getDingdingUserDTOs() {
        return dingdingUserDTOs;
    }

    public void setDingdingUserDTOs(List<DingdingUserDTO> dingdingUserDTOs) {
        this.dingdingUserDTOs = dingdingUserDTOs;
    }
}
