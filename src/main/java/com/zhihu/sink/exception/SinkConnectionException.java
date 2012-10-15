package com.zhihu.sink.exception;

/**
 * Created with IntelliJ IDEA.
 * User: shenchen
 * Date: 7/20/12
 * Time: 3:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class SinkConnectionException extends SinkException {
    public SinkConnectionException(String message) {
        super(message);
    }

    public SinkConnectionException(Throwable e) {
        super(e);
    }

    public SinkConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
