package net.lateralview.simplerestclienthandler.helper;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ParametersJSONObject extends JSONObject
{
	public static JSONObject fromMap(Map<String, String> parameters)
	{
		JSONObject b = new JSONObject();

		try
		{
			if (null != parameters)
			{
				for (String parameter : parameters.keySet())
				{
					b.put(parameter, parameters.get(parameter));
				}
			}
		} catch (JSONException e)
		{
		}

		return b;
	}

	public static Map<String, String> toMap(JSONObject parameters)
	{
		Map<String, String> params = new HashMap<>();

		if (parameters != null)
		{
			JSONArray names = parameters.names();

			try
			{
				if (null != names)
				{
					for (int i = 0; i < names.length(); i++)
					{
						params.put(names.getString(i), parameters.get(names.getString(i)).toString());
					}
				}
			} catch (JSONException e)
			{
			}
		}

		return params;
	}

	public static String getUrlQuery(JSONObject parameters)
	{
		StringBuilder urlParameters = new StringBuilder();
		Map<String, String> parametersMap = toMap(parameters);

		if (parametersMap.size() > 0)
		{
			urlParameters.append("?");
		}

		for (String parameterName : parametersMap.keySet())
		{
			urlParameters.append(parameterName).append("=").append(parametersMap.get(parameterName).replace(" ", "%20")).append("&");
		}

		urlParameters.deleteCharAt(urlParameters.length() - 1);

		return urlParameters.toString();
	}

	public JSONObject putBoolean(String name, boolean value)
	{
		try
		{
			super.put(name, value);
		} catch (JSONException e)
		{
		}
		return this;
	}

	public JSONObject putDouble(String name, double value)
	{
		try
		{
			super.put(name, value);
		} catch (JSONException e)
		{
		}
		return this;
	}

	public JSONObject putInt(String name, int value)
	{
		try
		{
			super.put(name, value);
		} catch (JSONException e)
		{
		}
		return this;
	}

	public JSONObject putLong(String name, long value)
	{
		try
		{
			super.put(name, value);
		} catch (JSONException e)
		{
		}
		return this;
	}

	public JSONObject putString(String name, String value)
	{
		try
		{
			super.put(name, value);
		} catch (JSONException e)
		{
		}
		return this;
	}

	public JSONObject putObject(String name, Object value)
	{
		try
		{
			super.put(name, value);
		} catch (JSONException e)
		{
		}
		return this;
	}

	public JSONObject putOptObject(String name, Object value)
	{
		if (name == null || value == null)
		{
			return this;
		}
		return putObject(name, value);
	}

	public JSONObject putJSONArray(String name, JSONArray array)
	{
		putOptObject(name, array);
		return this;
	}

	public JSONObject putObjectArray(String name, Object[] objects)
	{
		JSONArray jsonArray = new JSONArray();

		for (Object object : objects)
		{
			try
			{
				jsonArray.put(new JSONObject(new Gson().toJson(object)));
			} catch (JSONException e)
			{
			}
		}

		putJSONArray(name, jsonArray);

		return this;
	}
}
