package com.myframework.constant;

import java.math.BigDecimal;

/**
 * 框架通用常量
 * 
 */
public final class Constants
{
	/**
	 * 私有构造函数，不允许实例化
	 */
	private Constants()
	{

	}
	/** The Constant SYSDATE. */
	public static final String SYSDATE = "sysdate";
	/** 国际化资源文件位置 */
	public static final String BUNDLE_KEY = "ApplicationResources";
	/** 数据库配置文件位置 */
	public static final String JDBC_FILE_PATH = "config/jdbc.properties";
	/** 配置文件名 */
	public static final String SYSTEM_FILE_PATH = "config/system.properties";
	/** 文件分隔符 */
	public static final String FILE_SEP = System.getProperty("file.separator");
	/** 使用个项目 */
	public static final String USER_HOME = System.getProperty("user.home") + FILE_SEP;
	/** 是 */
	public static final String YES = "Y";
	/** 否 */
	public static final String NO = "N";
	/** true */
	public static final String TRUE = "true";
	/** false */
	public static final String FALSE = "false";
	/** 数据类型 表FD_PROPERTY中FIELD_TYPE所有可能的取值 */
	/** int 数据类型 */
	public static final String DB_INT = "int";
	/** int 数据类型 */
	public static final String DB_INT8 = "int8";
	/** long 数据类型 */
	public static final String DB_LONG = "long";
	/** short 数据类型 */
	public static final String DB_SHORT = "short";
	/** numeric 数据类型 */
	public static final String DB_NUMERIC = "numeric";
	/** float 数据类型 */
	public static final String DB_FLOAT = "float";
	/** string Java数据类型 */
	public static final String DB_STRING = "string";
	/** The Constant DB_VARCHAR. */
	public static final String DB_VARCHAR = "varchar";
	/** char 数据类型 */
	public static final String DB_CHAR = "char";
	/** date 数据类型 */
	public static final String DB_DATE = "date";
	/** timestamp 数据类型 */
	public static final String DB_TIMESTAMP = "timestamp";
	/** datetime 数据类型 */
	public static final String DB_DATETIME = "datetime";
	/** clob 数据类型 */
	public static final String DB_CLOB = "clob";
	/** blob 数据类型 */
	public static final String DB_BLOB = "blob";
	/** bytea 数据类型 */
	public static final String DB_BYTEA = "bytea";
	/** 年 格式化 */
	public static final String YEAR = "yyyy";
	/** 年 格式化 */
	public static final String YEAR_MONTH = "yyyy-MM";
	/** 日期 格式化 */
	public static final String DATE = "yyyy-MM-dd";
	/** 日期时间 格式化 */
	public static final String TIMESTAMP = "yyyy-MM-dd hh:mm:ss";
	/** SESSION 相关常量key */
	public static final String SESSION_CITYID_KEY = "cityId";
	public static final String SESSION_CITY_KEY = "city";
	/** 逻辑删除状态 */
	public static final String STATUS_LOGICAL_DELETE = "0";
	/** SESSION中USER对象的KEY */
	public static final String SESSION_MENU_ID = "menuId";
	public static final String SESSION_VIEW_ALL = "canViewAll";
	/** 属性集合 */
	public static final String FIELDS = "fields";
	/** 属性 */
	public static final String FIELD = "field";
	/** 属性值集合 */
	public static final String VALUES = "values";
	/** 属性值 */
	public static final String VALUE = "value";
	/** 表名称 */
	public static final String TABLENAME = "tableName";
	/** 记录集 */
	public static final String RECORDS = "records";
	/** 排序 */
	public static final String SORT = "sorts";
	/** 条件集 */
	public static final String CONDITIONS = "conditions";
	/** 操作符 */
	public static final String OPERATOR = "operator";
	/**  */
	public static final String ID = "id";
	/** 菜单类型 */
	public static final String MENU_TYPE_WEB = "1";
	/** XMAP */
	public static final String MENU_TYPE_XMAP = "2";
	/** WAP */
	public static final String MENU_TYPE_WAP = "3";
	/** 生成应用SQL基本路径(全路径为: WEB-INF\{appcode}\{ver}\install.sql) */
	public static final String BASE_INSTALLSQL_PATH = "WEB-INF";
	/** 生成应用SQL文件 */
	public static final String INSTALLSQL_FILE = "install.sql";
	/** 生成应用SQL文件的字符编码 */
	public static final String FILE_ENCODING = "UTF-8";
    /** MyTags 默认属性 */
    public static final String DEFAULT_ATTRIBUTE_NAME = "defaultAttributeName";

	/** 查看权限 */
	public static final String SEC_VIEW = "VIEW";
	/** 所有数据权限 */
	public static final String SEC_VIEW_ALL = "VIEW_ALL";
	/** 新增权限 */
	public static final String SEC_ADD = "ADD";
	/** 修改权限 */
	public static final String SEC_UPD = "UPD";
	/** 删除权限 */
	public static final String SEC_DEL = "DEL";
	/** 导入权限 */
	public static final String SEC_IMP = "IMP";
	/** 导出权限 */
	public static final String SEC_EXP = "EXP";
	/** 通用权限 */
	public static final String SEC_UDF_EXCUTE_OP = "UDF_EXCUTE_OP";
	/**
	 * 重置密码权限对象
	 */
	public static final String ORG_PWD_VIEW = "ORG_PWD_VIEW";
	/** 用户状态删除 */
	public static final String USER_STATUS_DELETE = "0";
	/** 用户状态普通 */
	public static final String USER_STATUS_NORMAL = "1";
	/** 用户状态冻结 */
	public static final String USER_STATUS_FREEZE = "2";
	/** 用户状态未登录 */
	public static final String USER_STATUS_FIRST_LOGIN = "3";
	/** 默认皮肤 (1, 第一种皮肤 3,第叁种皮肤)* */
	public static final String DEFAULT_SKIN = "1";

	/** 记录逻辑状-删除. */
	public static final String STATUS_DEL = "0";
	/** 记录逻辑状-正常 */
	public static final String STATUS_OK = "1";
	/** 排序-正序 */
	public static final String SORT_ASC = "ASC";
	/** 排序-逆序 */
	public static final String SORT_DESC = "DESC";
	/** url中菜单ID的参数名. */
	public static final String URL_PARAM_MENU_ID = "menuId";

	/***************************************************************************
	 * 配置表保留字段列名称
	 **************************************************************************/
	/** 主键列名 */
	public static final String DEFAULT_PK_COLUMN_NAME = "id";
	/** 删除状态 */
	public static final String STATUS = "status";
	/** 创建人 */
	public static final String RESERVE_FIELD_NAME_CREATE_ID = "create_id";
	/** 创建时间 */
	public static final String RESERVE_FIELD_NAME_CREATE_TIME = "create_time";
	/** 修改人 */
	public static final String RESERVE_FIELD_NAME_UPDATE_ID = "update_id";
	/** 修改时间 */
	public static final String RESERVE_FIELD_NAME_UPDATE_TIME = "update_time";
	/** 树形表保留字段－parent_id */
	public static final String DEFAULT_COLUMN_NAME_PARENT_ID = "parent_id";

	/** 默认的树根标识 */
	public static final Long ROOT_ID = -1L;
	/** 默认出错消息 */
	public static final String ERROR_MSG = "服务器忙，请稍后再试。";
	/** 默认企业参数值 */
	public static final String SYSTEM_NAME_DEFAULT = "未设置";

	/** 排序号 */
	public final static BigDecimal DEFAULT_SEQU = BigDecimal.valueOf(99999);

	/**上传根目录*/
	public final static String UPLOAD_ROOT_PATH = "";
}
