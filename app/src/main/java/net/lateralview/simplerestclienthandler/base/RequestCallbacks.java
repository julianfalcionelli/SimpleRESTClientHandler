package net.lateralview.simplerestclienthandler.base;

public abstract class RequestCallbacks<R, E>
{
	protected void onRequestStart()
	{
	}

	protected abstract void onRequestSuccess(R response);

	protected abstract void onRequestError(E error);

	protected void onRequestFinish()
	{
	}
}
