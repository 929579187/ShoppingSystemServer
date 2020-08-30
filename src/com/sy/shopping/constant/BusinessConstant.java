package com.sy.shopping.constant;

/**
 * 业务操作相关的常量
 */
public class BusinessConstant {
    /**
     * 验证码的长度
     */
    public static final int VALIDATE_CODE_LENGTH = 4;

    /**
     * Session中存储验证码的属性的名字
     */
    public static final String SESSION_VALIDATE_CODE = "session_validate";

    /**
     * Cookie中用于保存token信息的属性的名字
     */
    public static final String COOKIE_TOKEN_NAME = "token";

    /**
     * 用户的过期时间(s)
     */
    public static final int LOGIN_EXPIRE_SECONDS = 30 * 60;

    /**
     * Redis中用于表示登录用户的Key的前缀
     */
    public static final String REDIS_USER_PREFIX = "user:";

    /**
     * 要展示的最新商品的数量
     */
    public static final int NEWEST_PRODUCTS_SHOW_COUNT = 10;

    /**
     * 要展示的热门商品的数量
     */
    public static final int HOTEST_PRODUCTS_SHOW_COUNT = 10;
    /**
     * Redis中用于保存点击量数据的key
     */
    public static final String REDIS_CLICK_COUNT_KEY = "click_count";
    /**
     * Redis中点击量每次递增数值
     */
    public static final long REDIS_CLICK_COUNT_INCR = 1;
    /**
     * Redis中的点击量同步操作对应的Cron表达式
     */
    public static final String SYNC_REDIS_CLICK_COUNT_CRON_PATTERN = "0 0 0 * * ? *";
    /**
     * Redis中用于表示点击量排行数据的sorted set的key
     */
    public static final String REDIS_CLICK_COUNT_SORTED_SET_KEY = "sorted_set_click_count";
    /**
     * Redis中点击量排行榜元素的起始索引
     */
    public static final int REDIS_CLICK_COUNT_START_OFFSET = 0;
    /**
     * 分类信息一级节点的parentId
     */
    public static final int FIRST_LEVEL_CATEGORY_PARENT_ID = 0;
    /**
     * Redis中用于存储分类信息的key
     */
    public static final String REDIS_CATEGORY_KEY = "categories";
    /**
     * 商品搜索结果页默认显示的商品数量
     */
    public static final int PRODUCT_LIST_DEFAULT_SHOW_COUNT = 10;
    /**
     * 商品搜索结果页默认的页码
     */
    public static final int PRODUCT_LIST_DEFAULT_PAGE_NO = 1;
    /**
     * Cookie中用来保存客户端临时访问记录的name
     */
    public static final String COOKIE_TEMP_HISTORY_NAME = "temp_history";
    /**
     * Cookie中临时浏览记录的过期时间
     */
    public static final int COOKIE_TEMP_HISTORY_EXPIRE = 3 * 24 * 60 * 60;
    /**
     * Redis中保存临时浏览记录的key的前缀
     */
    public static final String REDIS_TEMP_HISTORY_KEY_PREFIX = "temp_history:";
    /**
     * Redis中保存登录以后浏览记录的key的前缀
     */
    public static final String REDIS_USER_HISTORY_KEY_PREFIX = "user_history:";
    /**
     * Redis中表示删除List中所有满足条件的元素
     */
    public static final Long REDIS_LIST_REMOVE_ALL = 0L;
    /**
     * Redis中List的起始索引
     */
    public static final Long REDIS_LIST_START_INDEX = 0L;
    /**
     * Redis中用于保存购物车数据的Key的前缀
     */
    public static final String REDIS_CART_KEY_PREFIX = "cart:";
}
