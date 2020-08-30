package top.superookie.toolcenter.http;

public class CallResult {

    private String entity;
    private Integer status;
    private String message;

    public CallResult() {}

    public CallResult(String entity, Integer status, String message) {
        this.entity = entity;
        this.status = status;
        this.message = message;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CallResult{" +
                "entity='" + entity + '\'' +
                ", status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
