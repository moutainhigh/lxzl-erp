package com.lxzl.erp.common.constant;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-10 9:02
 */
public class MaterialType {
    // 内存
    public static final Integer MATERIAL_TYPE_MEMORY = 1;
    // 主板
    public static final Integer MATERIAL_TYPE_MAIN_BOARD = 2;
    // CPU
    public static final Integer MATERIAL_TYPE_CPU = 3;
    // 机械硬盘
    public static final Integer MATERIAL_TYPE_HDD = 4;
    // 显卡
    public static final Integer MATERIAL_TYPE_GRAPHICS_CARD = 5;
    // 电源
    public static final Integer MATERIAL_TYPE_POWER_SUPPLY = 6;
    // 散热器
    public static final Integer MATERIAL_TYPE_RADIATOR = 7;
    // 固态硬盘
    public static final Integer MATERIAL_TYPE_SSD = 8;
    // 机箱
    public static final Integer MATERIAL_TYPE_BOX = 9;

    public static boolean inThisScope(Integer materialType) {
        if (materialType == null
                || (!MATERIAL_TYPE_MEMORY.equals(materialType)
                && !MATERIAL_TYPE_MAIN_BOARD.equals(materialType)
                && !MATERIAL_TYPE_CPU.equals(materialType)
                && !MATERIAL_TYPE_HDD.equals(materialType)
                && !MATERIAL_TYPE_GRAPHICS_CARD.equals(materialType)
                && !MATERIAL_TYPE_POWER_SUPPLY.equals(materialType)
                && !MATERIAL_TYPE_RADIATOR.equals(materialType)
                && !MATERIAL_TYPE_SSD.equals(materialType)
                && !MATERIAL_TYPE_BOX.equals(materialType))) {
            return false;
        }
        return true;
    }


    public static boolean isMainMaterial(Integer materialType) {
        if (materialType == null
                || (!MATERIAL_TYPE_MEMORY.equals(materialType)
                && !MATERIAL_TYPE_MAIN_BOARD.equals(materialType)
                && !MATERIAL_TYPE_CPU.equals(materialType)
                && !MATERIAL_TYPE_HDD.equals(materialType)
                && !MATERIAL_TYPE_SSD.equals(materialType)
                && !MATERIAL_TYPE_GRAPHICS_CARD.equals(materialType))) {
            return false;
        }
        return true;
    }

    public static boolean isCapacityMaterial(Integer materialType) {
        if (materialType == null
                || (!MATERIAL_TYPE_MEMORY.equals(materialType)
                && !MATERIAL_TYPE_HDD.equals(materialType)
                && !MATERIAL_TYPE_SSD.equals(materialType))) {
            return false;
        }
        return true;
    }

    public static boolean isModelMaterial(Integer materialType) {
        if (materialType == null
                || (!MATERIAL_TYPE_MAIN_BOARD.equals(materialType)
                && !MATERIAL_TYPE_CPU.equals(materialType)
                && !MATERIAL_TYPE_GRAPHICS_CARD.equals(materialType)
                && !MATERIAL_TYPE_POWER_SUPPLY.equals(materialType)
                && !MATERIAL_TYPE_RADIATOR.equals(materialType)
                && !MATERIAL_TYPE_BOX.equals(materialType))) {
            return false;
        }
        return true;
    }
}
