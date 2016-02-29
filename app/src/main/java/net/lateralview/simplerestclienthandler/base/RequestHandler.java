package net.lateralview.simplerestclienthandler.base;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.lateralview.simplerestclienthandler.gson.AnnotationExclusionStrategy;
import net.lateralview.simplerestclienthandler.helper.BundleJSONConverter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class RequestHandler<T>
{
	protected static final String TAG = RequestHandler.class.getSimpleName();

	private RequestCallbacks mRequestCallbacks;
	private Type mToClassType, mErrorClassType;
	private T mObjectParameter;
	private Bundle mBundleParameters;
	private Map<String, String> mFileParameters;

	public RequestHandler(RequestCallbacks requestCallbacks, T parameter)
	{
		mObjectParameter = parameter;

		initializeRequestHandler(requestCallbacks);
	}

	public RequestHandler(RequestCallbacks requestCallbacks, Bundle parameters)
	{
		mBundleParameters = parameters;

		initializeRequestHandler(requestCallbacks);
	}

	public RequestHandler(RequestCallbacks requestCallbacks)
	{
		this(requestCallbacks, new Bundle());
	}

	private void initializeRequestHandler(RequestCallbacks requestCallbacks)
	{
		mRequestCallbacks = requestCallbacks;
		mToClassType = getToClassFromRequestCallback();
		mErrorClassType = getErrorClassFromRequestCallback();

		callRequestCallbackStarted(requestCallbacks);
	}

	public Response.Listener<JSONObject> getResponseSuccessListener()
	{
		return new Response.Listener<JSONObject>()
		{
			@Override
			public void onResponse(JSONObject response)
			{
				requestJSONObjectResponseHandler(response);
			}
		};
	}

	public Response.Listener<JSONArray> getArrayResponseSuccessListener()
	{
		return new Response.Listener<JSONArray>()
		{
			@Override
			public void onResponse(JSONArray response)
			{
				requestJSONArrayResponseHandler(response);
			}
		};
	}

	public Response.ErrorListener getResponseErrorListener()
	{
		return new Response.ErrorListener()
		{
			public void onErrorResponse(VolleyError error)
			{
				requestErrorHandler(error);
			}
		};
	}

	public JSONObject getParameters()
	{
		JSONObject jsonObject = null;

		try
		{
			if (mObjectParameter != null)
			{
				jsonObject = new JSONObject(new GsonBuilder().setExclusionStrategies(new AnnotationExclusionStrategy()).create().toJson(mObjectParameter));
			} else
			{
				if (mBundleParameters != null)
				{
					jsonObject = BundleJSONConverter.convertToJSON(mBundleParameters);
				}
			}
		} catch (Exception e)
		{
			if (e != null && e.getMessage() != null)
			{
				Log.e(TAG, e.getMessage());
			}
			callRequestCallbackOnError(null);
		}

		return jsonObject;
	}

	private <E> void callRequestCallbackOnSuccess(E responseParsedData)
	{
		if (mRequestCallbacks != null)
		{
			mRequestCallbacks.onRequestSuccess(responseParsedData);
		}
	}

	private <E> void callRequestCallbackOnError(E errorObject)
	{
		if (mRequestCallbacks != null)
		{
			mRequestCallbacks.onRequestError(errorObject);
		}
	}

	private void callRequestCallbackFinished(RequestCallbacks requestCallbacks)
	{
		if (requestCallbacks != null)
		{
			requestCallbacks.onRequestFinish();
		}
	}

	private void callRequestCallbackStarted(RequestCallbacks requestCallbacks)
	{
		if (requestCallbacks != null)
		{
			requestCallbacks.onRequestStart();
		}
	}

	private Type getToClassFromRequestCallback()
	{
		Type genericType = null;

		if (mRequestCallbacks != null)
		{
			genericType = ((ParameterizedType) mRequestCallbacks.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		}

		return genericType;
	}

	private Type getErrorClassFromRequestCallback()
	{
		Type genericType = null;

		if (mRequestCallbacks != null)
		{
			genericType = ((ParameterizedType) mRequestCallbacks.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
		}

		return genericType;
	}

	private void requestJSONObjectResponseHandler(JSONObject response)
	{
		callRequestCallbackFinished(mRequestCallbacks);

		if (response != null)
		{
			try
			{
				callRequestCallbackOnSuccess(mToClassType != null ? new Gson().fromJson(response.toString(), mToClassType) : null);
			} catch (Exception e)
			{
				if (e != null && e.getMessage() != null)
				{
					Log.e(TAG, e.getMessage());
				}
				callRequestCallbackOnError(null);
			}
		} else
		{
			callRequestCallbackOnError(null);
		}
	}

	private void requestJSONArrayResponseHandler(JSONArray response)
	{
		callRequestCallbackFinished(mRequestCallbacks);

		if (response != null)
		{
			try
			{
				callRequestCallbackOnSuccess(mToClassType != null ? new Gson().fromJson(response.toString(), mToClassType) : null);
			} catch (Exception e)
			{
				if (e != null && e.getMessage() != null)
				{
					Log.e(TAG, e.getMessage());
				}
				callRequestCallbackOnError(null);
			}
		} else
		{
			callRequestCallbackOnError(null);
		}
	}

	private void requestErrorHandler(VolleyError error)
	{
		callRequestCallbackFinished(mRequestCallbacks);

		if (error != null && error.networkResponse != null && error.networkResponse.data != null)
		{
			try
			{
				callRequestCallbackOnError(mErrorClassType != null ? new Gson().fromJson(new String(error.networkResponse.data), mErrorClassType) : null);
			} catch (Exception e)
			{
				if (e != null && e.getMessage() != null)
				{
					Log.e(TAG, e.getMessage());
				}
				callRequestCallbackOnError(null);
			}
		} else
		{
			callRequestCallbackOnError(null);
		}
	}

	public Map<String, String> getFileParameters()
	{
		return mFileParameters;
	}

	public RequestHandler setFileParameters(Map<String, String> fileParameters)
	{
		mFileParameters = fileParameters;
		return this;
	}
}
