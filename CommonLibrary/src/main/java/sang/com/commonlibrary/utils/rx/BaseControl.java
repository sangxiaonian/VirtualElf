package sang.com.commonlibrary.utils.rx;

import io.reactivex.disposables.Disposable;

/**
 * 作者： ${PING} on 2017/10/18.
 */

public class BaseControl {

    public interface TaskListener<T> {
        void taskStart(Disposable d);

        void taskSuccessed();

        void taskFaile(String errorCode, String errorMessage);

        /**
         * 开始任务进度详情
         *
         * @param infor
         */
        void taskDetail(T infor);

    }



}
