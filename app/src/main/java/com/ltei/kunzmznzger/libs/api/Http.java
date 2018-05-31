package com.ltei.kunzmznzger.libs.api;

/**
 * @author Robin
 * @date 22/06/2017
 */
public enum Http
{
    GET, POST, PUT, DELETE;

    @Override
    public String toString ()
    {
        return this.name();
    }
}
