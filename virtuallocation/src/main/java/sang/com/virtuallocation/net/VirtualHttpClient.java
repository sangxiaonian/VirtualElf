package sang.com.virtuallocation.net;

import retrofit2.Retrofit;
import sang.com.commonlibrary.net.HttpClient;
import sang.com.virtuallocation.config.Configs;

/**
 * 作者： ${PING} on 2018/6/1.
 */

public class VirtualHttpClient {



    private static VirtualHttpClient client;


    private   VirtualService virtualService;


    public static VirtualHttpClient getClient(){
        if (client==null) {
            synchronized (VirtualHttpClient.class){
                if (client==null) {
                    client=new VirtualHttpClient();
                }
            }
        }
        return client;
    }
    private VirtualHttpClient() {
        virtualService = new HttpClient().getClient(Configs.baseUrl).create(VirtualService.class);
    }

    public VirtualService getService(){
        return virtualService;
    }




}
