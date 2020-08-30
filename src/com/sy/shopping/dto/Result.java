package com.sy.shopping.dto;

public class Result {
    /**
     * 是否成功
     */
    private Boolean success = true;

    /**
     * 如果出现错误，此处就是错误信息
     */
    private String errMsg;

    /**
     * 如果操作成功需要返回一些例如查询后的数据，就放在这里
     */
    private Object data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 创建正确情况下的返回
     *
     * @return 正确情况下的Result对象
     */
    public static Result buildSuccess() {
        Result result = new Result();
        result.setSuccess(true);
        return result;
    }

    /**
     * 创建正确情况下的返回
     *
     * @param data 正确情况下要附到给前端的数据（例如查询数据）
     * @return 正确情况下的Result对象
     */
    public static Result buildSuccess(Object data) {
        Result result = buildSuccess();
        result.setData(data);
        return result;
    }

    /**
     * 创建异常情况下的返回
     *
     * @param errMsg 错误信息
     * @return 异常情况下的Result对象
     */
    public static Result buildFailure(String errMsg) {
        Result result = new Result();
        result.setSuccess(false);
        result.setErrMsg(errMsg);
        return result;
    }
}
