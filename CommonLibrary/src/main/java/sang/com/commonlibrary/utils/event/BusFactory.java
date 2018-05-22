package sang.com.commonlibrary.utils.event;

/**
 * Created by dale on 2017/7/31.
 */

public class BusFactory {

    private static IBus bus;

    public static IBus getBus() {
        if (bus == null) {
            synchronized (BusFactory.class) {
                if (bus == null) {
                    bus = new EventBusImpl();
                }
            }
        }
        return bus;
    }
}
