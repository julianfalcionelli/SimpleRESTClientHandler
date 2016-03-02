package net.lateralview.simplerestclienthandler.base;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.lateralview.simplerestclienthandler.RestClientManager;
import net.lateralview.simplerestclienthandler.helper.MultipartEntity;
import net.lateralview.simplerestclienthandler.helper.ParametersJSONObject;
import net.lateralview.simplerestclienthandler.log.RequestLoggingHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public abstract class MultipartRequest<T> extends Request<T>
{
	protected static final String TAG = MultipartRequest.class.getSimpleName();
	protected static final String PROTOCOL_CHARSET = "utf-8";

	private final Response.Listener<T> mListener;

	protected Map<String, String> mHeaders = new HashMap<>();
	protected String mMimeType;
	protected MultipartEntity mMultipartEntity;

	public MultipartRequest(int method, String url, JSONObject parameters, Map<String, String> fileParameters, Map<String, String> headers, Response.Listener<T> listener, Response.ErrorListener errorListener)
	{
		super(method, url, errorListener);

		mListener = listener;

		if (headers != null)
		{
			mHeaders.putAll(headers);
		}

		mMultipartEntity = new MultipartEntity(fileParameters, ParametersJSONObject.toMap(parameters));

		mMimeType = "multipart/form-data;boundary=" + mMultipartEntity.getBoundary();

		if (RestClientManager.sDebugLog)
		{
			Log.i(TAG, RequestLoggingHelper.getMultipartRequestText(this, parameters));
		}
	}

	@Override
	public String getBodyContentType()
	{
		return mMimeType;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError
	{
		return mHeaders;
	}

	@Override
	public byte[] getBody() throws AuthFailureError
	{
		return mMultipartEntity.build();
	}

	@Override
	public void deliverError(VolleyError error)
	{
		if (RestClientManager.sDebugLog)
		{
			Log.i(TAG, RequestLoggingHelper.getRequestErrorText(this, error));
		}

		super.deliverError(error);
	}

	@Override
	protected void deliverResponse(T response)
	{
		if (RestClientManager.sDebugLog)
		{
			Log.i(TAG, RequestLoggingHelper.getRequestResponseText(this, response.toString()));
		}

		if (mListener != null)
		{
			mListener.onResponse(response);
		}
	}
}
