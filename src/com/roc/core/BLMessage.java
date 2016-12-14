package com.roc.core;


/**
 * User: rocwu
 * Time: 2016-8-9
 * Time: 1:48
 * Desc: 业务逻辑层返回的消息
 */
public class BLMessage {
    /** 是否成功 */
    protected boolean success;

    /** 消息 */
    protected String message;

    /** 结果 */
    protected Object data;


    public BLMessage() {
        this.success = true;
    }

    
    public BLMessage(boolean success) {
        this.success = success;
    }

    public BLMessage(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public BLMessage(boolean success, String message, Object data) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    /**
     * @return 合并后的字符串
     */
    public String toString(){
        return "result  =" + data
                + "\nsuccess=" + success
                + "\nmessage=" + message;
    }


	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}


	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}


	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}


	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}


	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}


	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
