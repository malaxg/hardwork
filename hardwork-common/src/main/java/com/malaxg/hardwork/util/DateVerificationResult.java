package com.malaxg.hardwork.util;

/**
 * 验证结果
 *
 * @author wangrong
 * @version [版本号, March 25, 2020]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class DateVerificationResult
{
    private boolean success = true;
    private String message = "";
    
    public static DateVerificationResult ok()
    {
        return new DateVerificationResult();
    }
    
    public boolean isFail()
    {
        return !success;
    }
    
    public static DateVerificationResult fail(String message)
    {
        return new DateVerificationResult(false, message);
    }
    
    public DateVerificationResult()
    {
    }
    
    public DateVerificationResult(boolean success, String message)
    {
        this.success = success;
        this.message = message;
    }
    
    public boolean isSuccess()
    {
        return success;
    }
    
    public void setSuccess(boolean success)
    {
        this.success = success;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
}
