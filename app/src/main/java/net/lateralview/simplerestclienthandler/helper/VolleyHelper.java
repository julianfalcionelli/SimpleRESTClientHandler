package net.lateralview.simplerestclienthandler.helper;

import android.content.Context;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.ResponseDelivery;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

import java.io.File;

/**
 * Created by Joaquin on 8/3/16.
 */
public class VolleyHelper
{
	private static final String DEFAULT_CACHE_DIR = "volley";

	private static final int DEFAULT_NETWORK_THREAD_POOL_SIZE = 4;

	public static RequestQueue newRequestQueue(Context context, ResponseDelivery responseDelivery)
	{
		File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);

		Network network = new BasicNetwork(new HurlStack());

		RequestQueue requestQueue = new RequestQueue(new DiskBasedCache(cacheDir), network, DEFAULT_NETWORK_THREAD_POOL_SIZE, responseDelivery);

		requestQueue.start();

		return requestQueue;
	}
}