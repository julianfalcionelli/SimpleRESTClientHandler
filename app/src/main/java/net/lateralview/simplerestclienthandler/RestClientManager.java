package net.lateralview.simplerestclienthandler;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RestClientManager
{
	public static final String TAG = RestClientManager.class.getSimpleName();

	private RestClientManager(Context context)
	{
		mContext = context;
		mRequestQueue = Volley.newRequestQueue(context);
	}

	private final RequestQueue mRequestQueue;
	private final Context mContext;

	private static RestClientManager sInstance;

	public static RestClientManager getInstance()
	{
		if (sInstance == null)
		{
			throw new UnsupportedOperationException("You must call RestClientManager.Initialize before using RestClient classes");
		}
		return sInstance;
	}

	public static void initialize(Context context)
	{
		sInstance = new RestClientManager(context);
	}

	public <T> void addToRequestQueue(Request<T> req, String tag)
	{
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		mRequestQueue.add(req);
	}

	public <T> void addToRequestQueue(Request<T> req)
	{
		req.setTag(TAG);
		mRequestQueue.add(req);
	}

	public void cancelPendingRequests(Object tag)
	{
		mRequestQueue.cancelAll(tag);
	}

	public Context getContext()
	{
		return mContext;
	}
}
