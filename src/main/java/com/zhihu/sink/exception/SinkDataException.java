package com.zhihu.sink.exception;

/**
 * Created with IntelliJ IDEA.
 * User: shenchen
 * Date: 7/20/12
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class SinkDataException extends SinkException {
    public SinkDataException(String message) {
        super(message);
    }

    public SinkDataException(Throwable e) {
        super(e);
    }

    public SinkDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
