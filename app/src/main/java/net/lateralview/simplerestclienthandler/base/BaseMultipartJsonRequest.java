package net.lateralview.simplerestclienthandler.base;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class BaseMultipartJsonRequest extends MultipartRequest<JSONObject>
{
	protected BaseMultipartJsonRequest(int method, String url, JSONObject parameters, Map<String, String> fileParameters, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, Map<String, String> headers)
	{
		super(method, url, parameters, fileParameters, headers, listener, errorListener);
	}

	protected BaseMultipartJsonRequest(int method, String url, RequestHandler requestHandler, Map<String, String> headers)
	{
		this(method, url, requestHandler.getParameters(), requestHandler.getFileParameters(), requestHandler.getResponseSuccessListener(), requestHandler.getResponseErrorListener(), headers);
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response)
	{
		try
		{
			String jsonString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
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
}
