package net.lateralview.simplerestclienthandler.log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Helper class to log requests information
 */
public class RequestLoggingHelper
{
	/**
	 * Gets basic request information.
	 *
	 * @param request Request instance to retrieve information for logging
	 * @return Message describing request, including Method type, URL and JSON body
	 */
	public static String getRequestText(Request request)
	{
		StringBuilder msg = new StringBuilder();

		msg.append("New ").append(getMethodText(request.getMethod())).append(" request").append("\n");
		msg.append("URL: ").append(request.getUrl()).append("\n");
		try
		{
			msg.append("JSON: ").append(new String(request.getBody())).append("\n");
		} catch (AuthFailureError authFailureError)
		{
			authFailureError.printStackTrace();
		}

		return msg.toString();
	}

	public static String getMultipartRequestText(Request request, JSONObject stringParameters)
	{
		StringBuilder msg = new StringBuilder();

		msg.append("New ").append(getMethodText(request.getMethod())).append(" request").append("\n");
		msg.append("URL: ").append(request.getUrl()).append("\n");

		if (stringParameters != null)
		{
			msg.append("JSON: ").append(stringParameters.toString()).append("\n");
		}

		return msg.toString();
	}

	/**
	 * Gets Request error information.
	 *
	 * @param request     Request instance to retrieve information for logging
	 * @param volleyError Error instance to retrieve information for logging
	 * @return Message describing error, including Method type, URL, HTTP status and JSON response
	 */
	public static String getRequestErrorText(Request request, VolleyError volleyError)
	{
		StringBuilder msg = new StringBuilder();

		msg.append(getMethodText(request.getMethod())).append(" request failed ").append("\n");
		msg.append("URL: ").append(request.getUrl()).append("\n");

		String statusCode = "unknown";
		String responseData = "unknown";
		if (volleyError.networkResponse != null)
		{
			statusCode = String.valueOf(volleyError.networkResponse.statusCode);
			responseData = new String(volleyError.networkResponse.data);
		}

		msg.append("HTTP status:").append(statusCode).append("\n");
		msg.append("Response:").append(new String(responseData)).append("\n");

		return msg.toString();
	}

	/**
	 * Gets Request response information.
	 *
	 * @param request  Request instance to retrieve information for logging
	 * @param response Response instance to retrieve information for logging
	 * @return Message describing response, including Method type, URL and JSON response
	 */
	public static String getRequestResponseText(Request request, JSONArray response)
	{
		return getRequestResponseText(request, response.toString());
	}

	public static String getRequestResponseText(Request request, JSONObject response)
	{
		return getRequestResponseText(request, response.toString());
	}

	public static String getRequestResponseText(Request request, String response)
	{
		StringBuilder msg = new StringBuilder();

		msg.append(getMethodText(request.getMethod())).append(" request successful ").append("\n");

		msg.append("URL: ").append(request.getUrl()).append("\n");

		if (response != null)
		{
			msg.append("Response: ").append(response).append("\n");
		}

		return msg.toString();
	}

	public static String getMethodText(int method)
	{
		switch (method)
		{
			case Request.Method.POST:
			{
				return "POST";
			}
			case Request.Method.GET:
			{
				return "GET";
			}
			case Request.Method.PUT:
			{
				return "PUT";
			}
			case Request.Method.DELETE:
			{
				return "DELETE";
			}
			default:
			{
				return "OTHER";
			}
		}
	}
}
