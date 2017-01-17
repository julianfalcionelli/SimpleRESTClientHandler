package net.lateralview.simplerestclienthandler.base;

/**
 * Created by julianfalcionelli on 1/16/17.
 */
public class HttpErrorException extends RuntimeException
{
    private Object error;

    public HttpErrorException(Object error)
    {
        this.error = error;
    }

    public Object getError()
    {
        return error;
    }
}
