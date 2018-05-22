package sang.com.commonlibrary.utils.event;

/**
 * Created by dale on 2017/7/31.
 */

public interface IBus {
    void register(Object object);

    void unregister(Object object);

    void post(Object event);

    void postSticky(Object event);

    void removeStickyEvent(Object event);
    void removeStickyEvent(Class event);


    interface IEvent {
        int getTag();
    }
}
