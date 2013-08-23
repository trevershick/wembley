package com.railinc.wembley.api.util;

import java.util.UUID;

public class UUIDGenerator
{

	private static UUIDGenerator instance = new UUIDGenerator();

    private UUIDGenerator()
    {
        // Do not instantiate
    }

    public static UUIDGenerator getInstance(){
    	return instance;
    }

    public String createUUID()
    {
    	return UUID.randomUUID().toString();
    }

    public synchronized String getKey(String prefix)
    {
        if ( prefix != null && prefix.length() > 0 )
        {
            return prefix + createUUID();
        }
        else
        {
            return createUUID();
        }
    }
}
