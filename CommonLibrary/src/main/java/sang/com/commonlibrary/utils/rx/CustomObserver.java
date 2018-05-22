package sang.com.commonlibrary.utils.rx;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * 作者： ${PING} on 2017/10/18.
 */

public class CustomObserver<T> implements Observer<T> {


    public Disposable disposable;
    private BaseControl.TaskListener listener;
    private int RESPONSE_CODE_FAILED = -1;

    public CustomObserver(BaseControl.TaskListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        disposable = d;
        if (listener != null) {
            listener.taskStart(d);
        }
    }


    @Override
    public void onNext(@NonNull T t) {

    }


    @Override
    public void onError(@NonNull Throwable t) {
        t.printStackTrace();
        int code = 0;
        String errorMessage = null;
        if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            code = httpException.code();
            errorMessage = httpException.getMessage();
        } else if (t instanceof SocketTimeoutException) {  //VPN open
            code = RESPONSE_CODE_FAILED;
            errorMessage = "服务器响应超时";
        } else if (t instanceof ConnectException) {
            code = RESPONSE_CODE_FAILED;
            errorMessage = "网络连接异常，请检查网络";
        } else if (t instanceof RuntimeException) {
            code = RESPONSE_CODE_FAILED;
            errorMessage = "运行时错误";
        } else if (t instanceof UnknownHostException) {
            code = RESPONSE_CODE_FAILED;
            errorMessage = "无法解析主机，请检查网络连接";
        } else if (t instanceof UnknownServiceException) {
            code = RESPONSE_CODE_FAILED;
            errorMessage = "未知的服务器错误";
        } else if (t instanceof IOException) {  //飞行模式等
            code = RESPONSE_CODE_FAILED;
            errorMessage = "没有网络，请检查网络连接";
        }
        if (listener != null) {
            listener.taskFaile(String.valueOf(code), errorMessage == null ? t.getMessage() : errorMessage);
        }
    }


    @Override
    public void onComplete() {
        if (listener != null) {
            listener.taskSuccessed();
        }
    }
}
