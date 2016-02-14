package net.lateralview.simplerestclienthandler;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import net.lateralview.simplerestclienthandler.base.BaseArrayJsonRequest;
import net.lateralview.simplerestclienthandler.base.BaseJsonRequest;
import net.lateralview.simplerestclienthandler.base.RequestHandler;

import java.util.Map;

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

	public static boolean sDebugLog = false;

	public static RestClientManager getInstance()
	{
		if (sInstance == null)
		{
			throw new UnsupportedOperationException("You must call RestClientManager.Initialize before using RestClient classes");
		}
		return sInstance;
	}

	public static RestClientManager initialize(Context context)
	{
		sInstance = new RestClientManager(context);

		return sInstance;
	}

	public void enableDebugLog(boolean enable)
	{
		sDebugLog = enable;
	}

	private <T> void addToRequestQueue(Request<T> req, String tag)
	{
		//to avoid time out
		req.setRetryPolicy(new DefaultRetryPolicy(
				50000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		req.setTag(tag != null ? tag : TAG);
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

	/*
		For request what returns JSONObject
	 */
	public void makeJsonRequest(int method, String url, RequestHandler requestHandler)
	{
		makeJsonRequest(method, url, requestHandler, null, null);
	}

	public void makeJsonRequest(int method, String url, RequestHandler requestHandler, String tag)
	{
		makeJsonRequest(method, url, requestHandler, null, tag);
	}

	public void makeJsonRequest(int method, String url, RequestHandler requestHandler, Map<String, String> headers)
	{
		makeJsonRequest(method, url, requestHandler, headers, null);
	}

	public void makeJsonRequest(int method, String url, RequestHandler requestHandler, Map<String, String> headers, String tag)
	{
		addToRequestQueue(new BaseJsonRequest(method, url, requestHandler, headers)
		{
		}, tag);
	}

	/*
		For request what returns JSONArray
	 */
	public void makeJsonArrayRequest(int method, String url, RequestHandler requestHandler)
	{
		makeJsonRequest(method, url, requestHandler, null, null);
	}

	public void makeJsonArrayRequest(int method, String url, RequestHandler requestHandler, String tag)
	{
		makeJsonRequest(method, url, requestHandler, null, tag);
	}

	public void makeJsonArrayRequest(int method, String url, RequestHandler requestHandler, Map<String, String> headers)
	{
		makeJsonRequest(method, url, requestHandler, headers, null);
	}

	public void makeJsonArrayRequest(int method, String url, RequestHandler requestHandler, Map<String, String> headers, String tag)
	{
		addToRequestQueue(new BaseArrayJsonRequest(method, url, requestHandler, headers)
		{
		}, tag);
	}
}
