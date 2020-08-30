package top.superookie.toolcenter.exception;

public class ToolException extends RuntimeException {

    private Integer code;
    private String message;

    public ToolException(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getDesc();
    }

    public ToolException(ErrorCode errorCode, String extra) {
        this.code = errorCode.getCode();
        this.message = String.format("%s: %s", errorCode.getDesc(), extra);
    }

    public ToolException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
