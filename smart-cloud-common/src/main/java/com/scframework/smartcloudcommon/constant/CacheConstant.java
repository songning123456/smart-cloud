package com.scframework.smartcloudcommon.constant;

/**
 * @author sonin
 * @date 2021/8/21 9:07
 * 缓存常量
 */
public class CacheConstant {

    /**
     * 字典信息缓存（含禁用的字典项）
     */
    public static final String SYS_DICT_CACHE = "sys:cache:dict";

    /**
     * 字典信息缓存 status为有效的
     */
    public static final String SYS_ENABLE_DICT_CACHE = "sys:cache:dictEnable";
    /**
     * 表字典信息缓存
     */
    public static final String SYS_DICT_TABLE_CACHE = "sys:cache:dictTable";
    public static final String SYS_DICT_TABLE_BY_KEYS_CACHE = SYS_DICT_TABLE_CACHE + "ByKeys";

    /**
     * 数据权限配置缓存
     */
    public static final String SYS_DATA_PERMISSIONS_CACHE = "sys:cache:permission:datarules";

    /**
     * 缓存用户信息
     */
    public static final String SYS_USERS_CACHE = "sys:cache:user";

    /**
     * 全部部门信息缓存
     */
    public static final String SYS_DEPARTS_CACHE = "sys:cache:depart:alldata";


    /**
     * 全部部门ids缓存
     */
    public static final String SYS_DEPART_IDS_CACHE = "sys:cache:depart:allids";

    /**
     * 字典信息缓存
     */
    public static final String SYS_DYNAMICDB_CACHE = "sys:cache:dbconnect:dynamic:";

    /**
     * gateway路由缓存
     */
    public static final String GATEWAY_ROUTES = "sys:cache:cloud:gateway_routes";


    /**
     * gateway路由 reload key
     */
    public static final String ROUTE_JVM_RELOAD_TOPIC = "gateway_jvm_route_reload_topic";

}
