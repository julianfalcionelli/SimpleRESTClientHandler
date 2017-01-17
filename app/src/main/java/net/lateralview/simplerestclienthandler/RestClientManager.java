package net.lateralview.simplerestclienthandler;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ResponseDelivery;
import com.android.volley.toolbox.Volley;

import net.lateralview.simplerestclienthandler.base.BaseArrayJsonRequest;
import net.lateralview.simplerestclienthandler.base.BaseJsonRequest;
import net.lateralview.simplerestclienthandler.base.BaseMultipartJsonArrayRequest;
import net.lateralview.simplerestclienthandler.base.BaseMultipartJsonRequest;
import net.lateralview.simplerestclienthandler.base.RequestFutureHandler;
import net.lateralview.simplerestclienthandler.base.RequestHandler;
import net.lateralview.simplerestclienthandler.helper.VolleyHelper;

import java.util.Map;

public class RestClientManager
{
	public static final String TAG = RestClientManager.class.getSimpleName();
	public static boolean sDebugLog = false;
	private static RestClientManager sInstance;
	private final RequestQueue mRequestQueue;
	private final Context mContext;

	private RestClientManager(Context context)
	{
		mContext = context;
		mRequestQueue = Volley.newRequestQueue(context);
	}

	/**
	 * Method used to create a queue that uses a custom response delivery.
	 *
	 * @param context          the context the manager will use to create de queue (Use application context in the application initialization)
	 * @param responseDelivery this can be used to execute calls in a worker or a main thread. Be careful with it's usage since it can generate a blocking thread and this can lead to a poor performance of your app.
	 *                         Consider using a SingleThreadExecutor to perform Async tests in your unit testing clases.
	 *                         <p/>
	 *                         Example: new ExecutorDelivery(Executors.newSingleThreadExecutor())
	 */
	private RestClientManager(Context context, ResponseDelivery responseDelivery)
	{
		mContext = context;
		mRequestQueue = VolleyHelper.newRequestQueue(mContext, responseDelivery);
	}

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

	public static RestClientManager initialize(Context context, ResponseDelivery responseDelivery)
	{
		sInstance = new RestClientManager(context, responseDelivery);

		return sInstance;
	}

	public RestClientManager enableDebugLog(boolean enable)
	{
		sDebugLog = enable;

		return getInstance();
	}

	private <T> void addToRequestQueue(Request<T> req, String tag)
	{
		mRequestQueue.add(normalizeRequest(req, tag));
	}

	private Request normalizeRequest(Request req, String tag)
	{
		//to avoid time out
		req.setRetryPolicy(new DefaultRetryPolicy(
				50000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		req.setTag(tag != null ? tag : TAG);

		return req;
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
		For request that return JSONObject
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
		addToRequestQueue(new BaseJsonRequest(method, url, requestHandler.getParameters(), requestHandler.getResponseSuccessListener(), requestHandler.getResponseErrorListener(), headers), tag);
	}

	/*
		For request that return JSONArray
	 */
	public void makeJsonArrayRequest(int method, String url, RequestHandler requestHandler)
	{
		makeJsonArrayRequest(method, url, requestHandler, null, null);
	}

	public void makeJsonArrayRequest(int method, String url, RequestHandler requestHandler, String tag)
	{
		makeJsonArrayRequest(method, url, requestHandler, null, tag);
	}

	public void makeJsonArrayRequest(int method, String url, RequestHandler requestHandler, Map<String, String> headers)
	{
		makeJsonArrayRequest(method, url, requestHandler, headers, null);
	}

	public void makeJsonArrayRequest(int method, String url, RequestHandler requestHandler, Map<String, String> headers, String tag)
	{
		addToRequestQueue(new BaseArrayJsonRequest(method, url, requestHandler.getParameters(), requestHandler.getArrayResponseSuccessListener(), requestHandler.getResponseErrorListener(), headers), tag);
	}

	/*
		For multipart request that return JSONObject
	 */
	public void makeMultipartJsonRequest(int method, String url, RequestHandler requestHandler)
	{
		makeMultipartJsonRequest(method, url, requestHandler, null, null);
	}

	public void makeMultipartJsonRequest(int method, String url, RequestHandler requestHandler, String tag)
	{
		makeMultipartJsonRequest(method, url, requestHandler, null, tag);
	}

	public void makeMultipartJsonRequest(int method, String url, RequestHandler requestHandler, Map<String, String> headers)
	{
		makeMultipartJsonRequest(method, url, requestHandler, headers, null);
	}

	public void makeMultipartJsonRequest(int method, String url, RequestHandler requestHandler, Map<String, String> headers, String tag)
	{
		addToRequestQueue(new BaseMultipartJsonRequest(method, url, requestHandler.getParameters(), requestHandler.getFileParameters(), requestHandler.getResponseSuccessListener(), requestHandler.getResponseErrorListener(), headers), tag);
	}

	/*
		For multipart request that return JSONArray
	 */
	public void makeMultipartJsonArrayRequest(int method, String url, RequestHandler requestHandler)
	{
		makeMultipartJsonArrayRequest(method, url, requestHandler, null, null);
	}

	public void makeMultipartJsonArrayRequest(int method, String url, RequestHandler requestHandler, String tag)
	{
		makeMultipartJsonArrayRequest(method, url, requestHandler, null, tag);
	}

	public void makeMultipartJsonArrayRequest(int method, String url, RequestHandler requestHandler, Map<String, String> headers)
	{
		makeMultipartJsonArrayRequest(method, url, requestHandler, headers, null);
	}

	public void makeMultipartJsonArrayRequest(int method, String url, RequestHandler requestHandler, Map<String, String> headers, String tag)
	{
		addToRequestQueue(new BaseMultipartJsonArrayRequest(method, url, requestHandler.getParameters(), requestHandler.getFileParameters(), requestHandler.getArrayResponseSuccessListener(), requestHandler.getResponseErrorListener(), headers), tag);
	}


	//------- Future Request ------//

	/*
		For request that return JSONObject
	 */
	public Object makeJsonRequest(int method, String url, RequestFutureHandler requestHandler)
	{
		return makeJsonRequest(method, url, requestHandler, null, null);
	}

	public Object makeJsonRequest(int method, String url, RequestFutureHandler requestHandler, String tag)
	{
		return makeJsonRequest(method, url, requestHandler, null, tag);
	}

	public Object makeJsonRequest(int method, String url, RequestFutureHandler requestHandler, Map<String, String> headers)
	{
		return makeJsonRequest(method, url, requestHandler, headers, null);
	}

	public Object makeJsonRequest(int method, String url, RequestFutureHandler requestHandler, Map<String, String> headers, String tag)
	{
		addToRequestQueue(new BaseJsonRequest(method, url, requestHandler.getParameters(), requestHandler.getRequestFuture(), requestHandler.getRequestFuture(), headers), tag);
		return requestHandler.call();
	}

	/*
		For request that return JSONArray
	 */
	public Object makeJsonArrayRequest(int method, String url, RequestFutureHandler requestHandler)
	{
		return makeJsonArrayRequest(method, url, requestHandler, null, null);
	}

	public Object makeJsonArrayRequest(int method, String url, RequestFutureHandler requestHandler, String tag)
	{
		return makeJsonArrayRequest(method, url, requestHandler, null, tag);
	}

	public Object makeJsonArrayRequest(int method, String url, RequestFutureHandler requestHandler, Map<String, String> headers)
	{
		return makeJsonArrayRequest(method, url, requestHandler, headers, null);
	}

	public Object makeJsonArrayRequest(int method, String url, RequestFutureHandler requestHandler, Map<String, String> headers, String tag)
	{
		addToRequestQueue(new BaseArrayJsonRequest(method, url, requestHandler.getParameters(), requestHandler.getRequestFuture(), requestHandler.getRequestFuture(), headers), tag);
		return requestHandler.call();
	}

	/*
		For multipart request that return JSONObject
	 */
	public Object makeMultipartJsonRequest(int method, String url, RequestFutureHandler requestHandler)
	{
		return makeMultipartJsonRequest(method, url, requestHandler, null, null);
	}

	public Object makeMultipartJsonRequest(int method, String url, RequestFutureHandler requestHandler, String tag)
	{
		return makeMultipartJsonRequest(method, url, requestHandler, null, tag);
	}

	public Object makeMultipartJsonRequest(int method, String url, RequestFutureHandler requestHandler, Map<String, String> headers)
	{
		return makeMultipartJsonRequest(method, url, requestHandler, headers, null);
	}

	public Object makeMultipartJsonRequest(int method, String url, RequestFutureHandler requestHandler, Map<String, String> headers, String tag)
	{
		addToRequestQueue(new BaseMultipartJsonRequest(method, url, requestHandler.getParameters(), requestHandler.getFileParameters(), requestHandler.getRequestFuture(), requestHandler.getRequestFuture(), headers), tag);
		return requestHandler.call();
	}

	/*
		For multipart request that return JSONArray
	 */
	public Object makeMultipartJsonArrayRequest(int method, String url, RequestFutureHandler requestHandler)
	{
		return makeMultipartJsonArrayRequest(method, url, requestHandler, null, null);
	}

	public Object makeMultipartJsonArrayRequest(int method, String url, RequestFutureHandler requestHandler, String tag)
	{
		return makeMultipartJsonArrayRequest(method, url, requestHandler, null, tag);
	}

	public Object makeMultipartJsonArrayRequest(int method, String url, RequestFutureHandler requestHandler, Map<String, String> headers)
	{
		return makeMultipartJsonArrayRequest(method, url, requestHandler, headers, null);
	}

	public Object makeMultipartJsonArrayRequest(int method, String url, RequestFutureHandler requestHandler, Map<String, String> headers, String tag)
	{
		addToRequestQueue(new BaseMultipartJsonArrayRequest(method, url, requestHandler.getParameters(), requestHandler.getFileParameters(), requestHandler.getRequestFuture(), requestHandler.getRequestFuture(), headers), tag);
		return requestHandler.call();
	}
}
