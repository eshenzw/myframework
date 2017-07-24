package com.myframework.core.zookeeper.exception;


import com.myframework.core.error.exception.code.ErrorCode;
import com.myframework.core.error.exception.internel.ErrorCodeLoader;

/**
 * 
 * zookeeper 异常
 * @author zhangjun
 *
 */
public enum ZookeeperErrorCode implements ErrorCode {
	
  //zookeeper 非预期异常
  EX_ZK_UNEXPECTED_EX (100012000),
  //zookeeper 未启动或者连接丢失
  EX_ZK_NOT_START (100012001),    
  
  //zookeeper 创建节点失败
  EX_ZK_CREATE_NODE_FAIL (100012002),
  
  //zookeeper 节点已存在
  EX_ZK_NODE_FAIL_NODE_EXIST (100012003),  
  
  //zookeeper 节点不存在
  EX_ZK_NODE_FAIL_NOT_EXIST (100012004),
  
  //zookeeper 获取节点数据失败
  EX_ZK_GET_NODE_DATA_FAIL (100012005),
  
  //zookeeper 修改节点数据失败
  EX_ZK_UPDATE_NODE_DATA_FAIL (100012006),
  
  //zookeeper 删除节点失败
  EX_ZK_DELETE_NODE_DATA_FAIL (100012007),
	
  //删除节点失败，含有子节点
  EX_ZK_DELETE_NODE_FAIL_FOR_NOT_EMPTY (100012008), 
	;

	

    /**
     * 异常码
     */
    private final int code;
    
    
    private ZookeeperErrorCode(int code){
        this.code = code;
    }
	

	@Override
	public String getMessage() {
		
		return ErrorCodeLoader.getErrorMessageByCode(this.code);
	}

	@Override
	public int getCode() {
		return code;
	}
	
	
	
}	