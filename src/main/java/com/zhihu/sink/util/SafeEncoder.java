package com.zhihu.sink.util;

import com.zhihu.sink.Protocol;
import com.zhihu.sink.exception.SinkDataException;

import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: shenchen
 * Date: 7/13/12
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class SafeEncoder {
    public static byte[] encode(final String str) {
        try {
            if (str == null) {
                throw new SinkDataException(
                        "value sent to redis cannot be null");
            }
            return str.getBytes(Protocol.CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new SinkDataException(e);
        }
    }

    public static String encode(final byte[] data) {
        try {
            return new String(data, Protocol.CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new SinkDataException(e);
        }
    }
}