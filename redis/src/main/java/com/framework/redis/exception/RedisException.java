package com.framework.redis.exception;

/**
 * 说明：Redis数据操作异常处理类。
 *
 * @author chenhaipeng
 * @version 1.0
 * @date 2016/07/17 20:45
 */
public class RedisException extends Exception {

    private static final long serialVersionUID = -1747873934394075651L;

    private String errCode;

    private String errMsg;

    private Exception e;

    public static final String ERR_WRITE_TIME_OUT = "0001";// 写入超时异常编码

    public RedisException(Exception e) {
        super();
        this.errMsg = e.getMessage();
    }

    public RedisException(String errMsg, Exception e) {
        this.e = e;
        this.errMsg = errMsg;
    }

    public RedisException(String errMsg) {
        this.errMsg = errMsg;
    }

    public RedisException(String errCode, String errMsg, Exception e) {
        super();
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.e = e;
    }

    @Override
    public void printStackTrace() {

        if (this.errCode == null && "".equals(this.errCode)) {
            System.err.println(this.getClass().getName() + ":" + this.errMsg);
        } else {
            System.err.println(this.getClass().getName() + ":" + "[异常编码："
                    + this.errCode + "]" + this.errMsg);
        }

        super.printStackTrace();
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Exception getE() {
        return e;
    }

    public void setE(Exception e) {
        this.e = e;
    }

}
