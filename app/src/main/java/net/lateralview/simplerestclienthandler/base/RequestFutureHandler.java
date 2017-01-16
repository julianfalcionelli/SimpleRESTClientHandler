package net.lateralview.simplerestclienthandler.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.lateralview.simplerestclienthandler.gson.AnnotationExclusionStrategy;
import net.lateralview.simplerestclienthandler.helper.BundleJSONConverter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;


public class RequestFutureHandler
{
	protected static final String TAG = RequestFutureHandler.class.getSimpleName();

	public static Type sServerErrorClass;

	private Type mToClassType, mErrorClassType;
	private Object mObjectParameter;
	private Bundle mBundleParameters = new Bundle();
	private Map<String, String> mFileParameters;
	private RequestFuture mCallbacks;

	public static void setServerErrorClass(Class serverErrorClass)
	{
		sServerErrorClass = serverErrorClass;
	}

	public RequestFutureHandler(Type responseClass, Type errorClass)
	{
		mToClassType = responseClass;
		mErrorClassType = errorClass;

		initializeRequestHandler();
	}

	public RequestFutureHandler(Type responseClass)
	{
		this(responseClass, sServerErrorClass);
	}

	public RequestFutureHandler(Type responseClass, Object parameters)
	{
		this(responseClass, sServerErrorClass, parameters);
	}

	public RequestFutureHandler(Type responseClass, Bundle parameters)
	{
		this(responseClass, sServerErrorClass, parameters);
	}

	public RequestFutureHandler(Type responseClass, Type errorClass, Object parameters)
	{
		this(responseClass, errorClass);
		mObjectParameter = parameters;
	}

	public RequestFutureHandler(Type responseClass, Type errorClass, @NonNull Bundle parameters)
	{
		this(responseClass, errorClass);
		mBundleParameters = parameters;
	}

	private void initializeRequestHandler()
	{
		mCallbacks = RequestFuture.newFuture();
	}

	public RequestFuture getRequestFuture()
	{
		return mCallbacks;
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
				jsonObject = BundleJSONConverter.convertToJSON(mBundleParameters);
			}
		} catch (Exception e)
		{
			if (e.getMessage() != null)
			{
				Log.e(TAG, e.getMessage());
			}
		}

		return jsonObject;
	}

	public Object call()
	{
		Object response;

		try
		{
			response = mCallbacks.get();
		} catch (Exception e)
		{
			ServerError serverError = null;

			if (e.getCause() instanceof ServerError)
			{
				serverError = ((ServerError) e.getCause());
			}

			throw new HttpErrorException(parseError(serverError));
		}

		return parseResponse(response);
	}

	private Object parseResponse(Object response)
	{
		if (response instanceof JSONArray)
		{
			return parseJSONArrayResponse((JSONArray) response);
		}

		return parseJSONObject((JSONObject) response);
	}

	private Object parseJSONObject(JSONObject response)
	{
		Object objectResponse = response;

		if (response != null && !mToClassType.toString().equals(JSONObject.class.toString()))
		{
			try
			{
				objectResponse = new Gson().fromJson(response.toString(), mToClassType);

			} catch (Exception e)
			{
				if (e.getMessage() != null)
				{
					Log.e(TAG, e.getMessage());
				}
			}
		}

		return objectResponse;
	}


	private Object parseJSONArrayResponse(JSONArray response)
	{
		Object objectResponse = response;

		if (response != null && !mToClassType.toString().equals(JSONArray.class.toString()))
		{
			try
			{
				objectResponse = new Gson().fromJson(response.toString(), mToClassType);

			} catch (Exception e)
			{
				if (e.getMessage() != null)
				{
					Log.e(TAG, e.getMessage());
				}
			}
		}

		return objectResponse;
	}

	private Object parseError(VolleyError error)
	{
		Object errorObject = error;

		if (!mToClassType.toString().equals(VolleyError.class.toString()) &&
				error != null && error.networkResponse != null && error.networkResponse.data != null)
		{
			try
			{
				errorObject = mErrorClassType != null ? new Gson().fromJson(new String(error.networkResponse.data), mErrorClassType) : error;
			} catch (Exception e)
			{
				if (e.getMessage() != null)
				{
					Log.e(TAG, e.getMessage());
				}
			}
		}

		return errorObject;
	}

	public Map<String, String> getFileParameters()
	{
		return mFileParameters;
	}

	public RequestFutureHandler setFileParameters(Map<String, String> fileParameters)
	{
		mFileParameters = fileParameters;
		return this;
	}
}
