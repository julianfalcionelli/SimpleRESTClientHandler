package net.lateralview.simplerestclienthandler.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.ResponseDelivery;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import java.io.File;

/**
 * Created by Joaquin on 8/3/16.
 */
public class VolleyHelper extends Volley{

    /** Default on-disk cache directory. */
    private static final String DEFAULT_CACHE_DIR = "volley";

    private static final int DEFAULT_NETWORK_THREAD_POOL_SIZE = 4;

    /**
     * Creates a default instance of the worker pool and calls {@link RequestQueue#start()} on it.
     * You may set a maximum size of the disk cache in bytes.
     *
     * @param context A {@link Context} to use for creating the cache dir.
     * @param stack An {@link HttpStack} to use for the network, or null for default.
     * @param maxDiskCacheBytes the maximum size of the disk cache, in bytes. Use -1 for default size.
     * @return A started {@link RequestQueue} instance.
     */
    public static RequestQueue newRequestQueue(Context context, HttpStack stack, int maxDiskCacheBytes, ResponseDelivery responseDelivery) {
        File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);

        String userAgent = "volley/0";
        try {
            String packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            userAgent = packageName + "/" + info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }

        if (stack == null) {
            if (Build.VERSION.SDK_INT >= 9) {
                stack = new HurlStack();
            } else {
                // Prior to Gingerbread, HttpUrlConnection was unreliable.
                // See: http://android-developers.blogspot.com/2011/09/androids-http-clients.html
                //stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
            }
        }

        Network network = new BasicNetwork(stack);

        RequestQueue queue;
        if(responseDelivery!=null){
            if (maxDiskCacheBytes <= -1)
            {
                // No maximum size specified
                queue = new RequestQueue(new DiskBasedCache(cacheDir), network,DEFAULT_NETWORK_THREAD_POOL_SIZE,responseDelivery);
            }
            else
            {
                // Disk cache size specified
                queue = new RequestQueue(new DiskBasedCache(cacheDir, maxDiskCacheBytes), network,DEFAULT_NETWORK_THREAD_POOL_SIZE,responseDelivery);
            }
        }
        else
        {
            if (maxDiskCacheBytes <= -1)
            {
                // No maximum size specified
                queue = new RequestQueue(new DiskBasedCache(cacheDir), network);
            }
            else
            {
                // Disk cache size specified
                queue = new RequestQueue(new DiskBasedCache(cacheDir, maxDiskCacheBytes), network);
            }
        }


        queue.start();

        return queue;
    }



}