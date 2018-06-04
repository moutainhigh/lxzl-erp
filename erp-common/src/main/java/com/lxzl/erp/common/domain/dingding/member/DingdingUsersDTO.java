package com.lxzl.erp.common.domain.dingding.member;

import com.lxzl.erp.common.domain.dingding.DingdingBaseDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author daiqi
 * @create 2018-06-04 11:41
 */
public class DingdingUsersDTO extends DingdingBaseDTO {

    private List<DingdingUserDTO> members;

    public DingdingUsersDTO(List<DingdingUserDTO> members) {
        this.members = members;
    }

    public DingdingUsersDTO(DingdingUserDTO dingdingUserDTO) {
        members = new ArrayList<>();
        members.add(dingdingUserDTO);
    }

    public List<DingdingUserDTO> getMembers() {
        return members;
    }

    public void setMembers(List<DingdingUserDTO> members) {
        this.members = members;
    }
}
