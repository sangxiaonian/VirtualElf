package sang.com.virtuallocation.entity;

/**
 * 作者： ${PING} on 2018/5/22.
 */

public class SelectBean<T> {
    private boolean isCheck;
    private T t;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
