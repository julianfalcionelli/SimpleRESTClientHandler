package net.lateralview.simplerestclienthandler.base;

import java.lang.reflect.Type;

/**
 * Created by julianfalcionelli on 11/11/16.
 */
public interface ICallbackTypes
{
    Type getResponseType();
    Type getErrorType();
}
