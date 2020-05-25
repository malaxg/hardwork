package com.malaxg.hardwork.web.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.malaxg.hardwork.util.DataVerificationUtil;

/**
 * hardwork
 *
 * @author wangrong
 * @version [版本号, 2019年1月7日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@SuppressWarnings("rawtypes")
public class RestResult<T>
{
    private int status = Constants.RET_SUCCESS;
    
    private int errorCode;
    
    private String errorMsg;
    
    private Map<String, Integer> pageinfo = new HashMap<String, Integer>();
    
    private List<T> data = new ArrayList<>();
    
    private int total;
    
    public RestResult()
    {
    }
    
    public RestResult(int status, int errorCode)
    {
        this.status = status;
        this.errorCode = errorCode;
    }
    
    public RestResult(List data, int total)
    {
        this.data = data;
        this.total = total;
    }
    
    public RestResult(int status, int errorCode, String errorMsg)
    {
        this.status = status;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    
    public RestResult(int errorCode, String errorMsg)
    {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    
    public RestResult(List data)
    {
        this.total = DataVerificationUtil.isEmpty(data) ? 0 : data.size();
        this.data = data;
    }
    
    public static RestResult ok()
    {
        return new RestResult();
    }
    
    public static RestResult ok(String message)
    {
        return new RestResult(Constants.RET_SUCCESS, message);
    }
    
    public static RestResult ok(List data)
    {
        return new RestResult(data);
    }
    
    public static RestResult ok(List data, int total)
    {
        return new RestResult(data, total);
    }
    
    public static RestResult fail(String message)
    {
        return new RestResult(Constants.FAIL, Constants.E600109999, message);
    }
    
    public static RestResult fail(int code, String message)
    {
        return new RestResult(Constants.FAIL, code, message);
    }
    
    public int getStatus()
    {
        return status;
    }
    
    public void setStatus(int status)
    {
        this.status = status;
    }
    
    public int getErrorCode()
    {
        return errorCode;
    }
    
    public RestResult setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
        return this;
    }
    
    public String getErrorMsg()
    {
        return errorMsg;
    }
    
    public RestResult setErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
        return this;
    }
    
    public int getTotal()
    {
        return total;
    }
    
    public void setTotal(int total)
    {
        this.total = total;
    }
    
    public Map<String, Integer> getPageinfo()
    {
        return pageinfo;
    }
    
    public List<T> getData()
    {
        return data;
    }
    
    public void setData(List<T> data)
    {
        this.data = data;
    }
    
}
