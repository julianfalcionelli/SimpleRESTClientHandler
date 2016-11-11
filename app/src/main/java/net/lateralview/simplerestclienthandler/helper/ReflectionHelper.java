package net.lateralview.simplerestclienthandler.helper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by julianfalcionelli on 11/11/16.
 */
public class ReflectionHelper
{
    public static Type getTypeArgument(Object object, int position)
    {
        Type genericType = null;

        if (object != null)
        {
            try
            {
                genericType = ((ParameterizedType) object.getClass().getGenericSuperclass()).getActualTypeArguments()[position];
            }
            catch (Exception e)
            {
                //do nothing
            }
        }

        return genericType;
    }
}
