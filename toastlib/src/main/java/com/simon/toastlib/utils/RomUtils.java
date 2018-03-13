package com.simon.toastlib.utils;

import android.text.TextUtils;

import java.io.IOException;

public class RomUtils {

    //MIUI标识
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    //EMUI标识
    private static final String KEY_EMUI_VERSION_CODE = "ro.build.version.emui";
    private static final String KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level";
    private static final String KEY_EMUI_CONFIG_HW_SYS_VERSION = "ro.confg.hw_systemversion";

    //Flyme标识  
    private static final String KEY_FLYME_ID_FALG_KEY = "ro.build.display.id";
    private static final String KEY_FLYME_ID_FALG_VALUE_KEYWORD = "Flyme";
    private static final String KEY_FLYME_ICON_FALG = "persist.sys.use.flyme.icon";
    private static final String KEY_FLYME_SETUP_FALG = "ro.meizu.setupwizard.flyme";
    private static final String KEY_FLYME_PUBLISH_FALG = "ro.flyme.published";

    /**
     * @param
     * @return ROM_TYPE ROM类型的枚举
     * @description获取ROM类型: MIUI_ROM, FLYME_ROM, EMUI_ROM, OTHER_ROM{@link RomType}
     */

    public static int getRomType() {
        int romType = RomType.OTHER;
        try {
            BuildProperties buildProperties = BuildProperties.getInstance();

            if (buildProperties.containsKey(KEY_EMUI_VERSION_CODE) || buildProperties.containsKey(KEY_EMUI_API_LEVEL) || buildProperties.containsKey(KEY_EMUI_CONFIG_HW_SYS_VERSION)) {
                return RomType.EMUI;
            }
            if (buildProperties.containsKey(KEY_MIUI_VERSION_CODE) || buildProperties.containsKey(KEY_MIUI_VERSION_NAME) || buildProperties.containsKey(KEY_MIUI_INTERNAL_STORAGE)) {
                return RomType.MIUI;
            }
            if (buildProperties.containsKey(KEY_FLYME_ICON_FALG) || buildProperties.containsKey(KEY_FLYME_SETUP_FALG) || buildProperties.containsKey(KEY_FLYME_PUBLISH_FALG)) {
                return RomType.FLYME;
            }
            if (buildProperties.containsKey(KEY_FLYME_ID_FALG_KEY)) {
                String romName = buildProperties.getProperty(KEY_FLYME_ID_FALG_KEY);
                if (!TextUtils.isEmpty(romName) && romName.contains(KEY_FLYME_ID_FALG_VALUE_KEYWORD)) {
                    return RomType.FLYME;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return romType;
    }

    public static boolean isMiui() {
        return getRomType() == RomType.MIUI;
    }

    public static boolean isFlyme() {
        return getRomType() == RomType.FLYME;
    }

    public static boolean isEmui() {
        return getRomType() == RomType.EMUI;
    }



    /**
     * 系统类型
     */
    public class RomType {
        public static final int MIUI = 0;
        public static final int FLYME = 1;
        public static final int EMUI = 2;
        public static final int OTHER = 3;
    }
}  