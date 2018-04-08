package com.lxzl.erp.core.service.interfaceSwitch;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.interfaceSwitch.SwitchQueryParam;
import com.lxzl.erp.common.domain.interfaceSwitch.pojo.Switch; /**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/4/4
 * @Time : Created in 16:19
 */
public interface SwitchService {

    /**
    * 添加功能开关
    * @Author : XiaoLuYu
    * @Date : Created in 2018/4/4 16:21
    * @param : interfaceSwitch
    * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
    */
    ServiceResult<String,String> add(Switch interfaceSwitch);
    /**
     * 修改功能开关
     * @Author : XiaoLuYu
     * @Date : Created in 2018/4/4 16:21
     * @param : interfaceSwitch
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    ServiceResult<String,String> update(Switch interfaceSwitch);
    /**
     * 分页查询功能开关
     * @Author : XiaoLuYu
     * @Date : Created in 2018/4/4 16:21
     * @param : interfaceSwitch
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    ServiceResult<String,Page<Switch>> page(SwitchQueryParam switchQueryParam);
    /**
     * 删除功能开关
     * @Author : XiaoLuYu
     * @Date : Created in 2018/4/4 16:21
     * @param : interfaceSwitch
     * @Return : com.lxzl.erp.common.domain.ServiceResult<java.lang.String,java.lang.String>
     */
    ServiceResult<String,String> delete(Switch interfaceSwitch);
}
