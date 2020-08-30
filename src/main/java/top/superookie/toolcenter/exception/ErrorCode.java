package top.superookie.toolcenter.exception;

public enum ErrorCode {

    MISS_CONFIG_PROPERTY(-100, "缺少配置项"),
    TOKEN_NOT_FOUND(-101, "token未找到, 请检查返回体的结构是否有变化"),
    MISS_CONFIG_FILE(-102, "配置文件不存在");


    private final int code;
    private final String desc;

    ErrorCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


}
