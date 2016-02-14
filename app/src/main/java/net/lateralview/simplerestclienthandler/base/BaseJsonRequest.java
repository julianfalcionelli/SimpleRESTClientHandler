package net.lateralview.simplerestclienthandler.base;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import net.lateralview.simplerestclienthandler.log.LogHelper;
import net.lateralview.simplerestclienthandler.log.RequestLoggingHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public abstract class BaseJsonRequest extends JsonObjectRequest
{
	protected static String TAG = BaseJsonRequest.class.getSimpleName();

	protected Map<String, String> mHeaders;

	/**
	 * @param method        HTTP method type (POST, PUT, DELETE or GET)
	 * @param url           Full URL for the request
	 * @param parameters    JSONObject with required and optional parameters
	 * @param listener      Listener to handle request successful response
	 * @param errorListener Listener to handle request error response
	 */
	protected BaseJsonRequest(int method, String url, JSONObject parameters, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener)
	{
		super(method, url, parameters, listener, errorListener);
		if (LogHelper.LOG_DEBUG_INFO)
		{
			Log.i(TAG, RequestLoggingHelper.getRequestText(this));
		}
	}

	protected BaseJsonRequest(int method, String url, JSONObject parameters, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, Map<String, String> headers)
	{
		this(method, url, parameters, listener, errorListener);
		mHeaders = headers;
	}

	protected BaseJsonRequest(int method, String url, RequestHandler requestHandler)
	{
		this(method, url, requestHandler.getParameters(), requestHandler.getResponseSuccessListener(), requestHandler.getResponseErrorListener());
	}

	protected BaseJsonRequest(int method, String url, RequestHandler requestHandler, Map<String, String> headers)
	{
		this(method, url, requestHandler.getParameters(), requestHandler.getResponseSuccessListener(), requestHandler.getResponseErrorListener());
		mHeaders = headers;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError
	{
		Map<String, String> headers = new HashMap<String, String>();

		headers.put("Content-Type", "application/json");
		if (mHeaders != null)
		{
			for (Map.Entry<String, String> entry : mHeaders.entrySet())
			{
				headers.put(entry.getKey(), entry.getValue());
			}
		}

		return headers;
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response)
	{
		try
		{
			String jsonString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			//Allow null
			if (jsonString == null || jsonString.length() == 0)
			{
				return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
			}
			return Response.success(new JSONObject(jsonString),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e)
		{
			return Response.error(new ParseError(e));
		} catch (JSONException je)
		{
			return Response.error(new ParseError(je));
		}
	}

	@Override
	public String getBodyContentType()
	{
		return "application/json";
	}

	@Override
	public void deliverError(VolleyError error)
	{
		if (LogHelper.LOG_DEBUG_INFO)
		{
			Log.i(TAG, RequestLoggingHelper.getRequestErrorText(this, error));
		}
		super.deliverError(error);
	}

	@Override
	protected void deliverResponse(JSONObject response)
	{
		if (LogHelper.LOG_DEBUG_INFO)
		{
			Log.i(TAG, RequestLoggingHelper.getRequestResponseText(this, response));
		}
		super.deliverResponse(response);
	}
}