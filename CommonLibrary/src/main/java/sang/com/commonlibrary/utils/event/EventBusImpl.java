package sang.com.commonlibrary.utils.event;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by dale on 2017/7/31.
 */

public class EventBusImpl implements IBus {

    @Override
    public void register(Object object) {
        if (!EventBus.getDefault().isRegistered(object)) {
            EventBus.getDefault().register(object);

        }
    }


    @Override
    public void unregister(Object object) {
        if (EventBus.getDefault().isRegistered(object)) {
            EventBus.getDefault().unregister(object);
        }
    }

    @Override
    public void post(Object event) {
        EventBus.getDefault().post(event);
    }

    @Override
    public void postSticky(Object event) {
        EventBus.getDefault().postSticky(event);
    }

    @Override
    public void removeStickyEvent(Object event) {
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Override
    public void removeStickyEvent(Class event) {
        EventBus.getDefault().removeStickyEvent(event);
    }
}
