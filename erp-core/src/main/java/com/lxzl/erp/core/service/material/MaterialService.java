package com.lxzl.erp.core.service.material;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.BulkMaterialQueryParam;
import com.lxzl.erp.common.domain.material.MaterialQueryParam;
import com.lxzl.erp.common.domain.material.pojo.BulkMaterial;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.material.pojo.MaterialImg;
import com.lxzl.se.core.service.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-13 13:43
 */
public interface MaterialService extends BaseService {

    /**
     * 上传图片
     *
     * @param files 图片流
     * @return 图片ID
     */
    ServiceResult<String, List<MaterialImg>> uploadImage(MultipartFile[] files);

    /**
     * 删除商品图片
     *
     * @param imgId 图片ID
     * @return 图片ID
     */
    ServiceResult<String, Integer> deleteImage(Integer imgId);
    /**
     * 添加物料
     *
     * @param material 物料信息
     * @return 物料编码
     */
    ServiceResult<String, String> addMaterial(Material material);

    /**
     * 修改物料
     *
     * @param material 物料信息
     * @return 物料编码
     */
    ServiceResult<String, String> updateMaterial(Material material);

    /**
     * 删除物料
     *
     * @param materialNo 物料信息编号
     * @return 物料编码
     */
    ServiceResult<String, String> deleteMaterial(String materialNo);

    /**
     * 查询所有物料
     *
     * @param materialQueryParam 查询物料参数
     * @return 物料集合
     */
    ServiceResult<String, Page<Material>> queryAllMaterial(MaterialQueryParam materialQueryParam);

    /**
     * 查询单个物料
     *
     * @param materialNo 查询物料编号
     * @return 物料信息
     */
    ServiceResult<String, Material> queryMaterialByNo(String materialNo);

    /**
     * 查询所有散料
     *
     * @param bulkMaterialQueryParam 查询散料参数
     * @return 散料集合
     */
    ServiceResult<String, Page<BulkMaterial>> queryAllBulkMaterial(BulkMaterialQueryParam bulkMaterialQueryParam);

    /**
     * 根据物料信息查询散料
     *
     * @param bulkMaterialQueryParam 查询散料参数
     * @return 散料集合
     */
    ServiceResult<String, Page<BulkMaterial>> queryBulkMaterialByMaterialId(BulkMaterialQueryParam bulkMaterialQueryParam);

    /**
     * 集合内是否都是四大件
     *
     * @param materialList 物料集合
     * @return 是否
     */
    boolean isAllMainMaterial(List<Material> materialList);

    /**
     * 集合内是否都是小配件
     *
     * @param materialList 物料集合
     * @return 是否
     */
    boolean isAllGadget(List<Material> materialList);
}
