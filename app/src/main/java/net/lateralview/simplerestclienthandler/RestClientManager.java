package net.lateralview.simplerestclienthandler;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
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
		//to avoid time out
		req.setRetryPolicy(new DefaultRetryPolicy(
				50000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


		req.setTag(tag);
		mRequestQueue.add(req);
	}

	public <T> void makeRequest(Request<T> req)
	{
		makeRequest(req, TAG);
	}

	public <T> void makeRequest(Request<T> req, String tag)
	{
		addToRequestQueue(req, tag);
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
