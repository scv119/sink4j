package com.zhihu.sink.exception;

/**
 * Created with IntelliJ IDEA.
 * User: shenchen
 * Date: 7/20/12
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class SinkException extends RuntimeException{
    private static final long serialVersionUID = -294626642333282677L;

    public SinkException(String message) {
        super(message);
    }

    public SinkException(Throwable e) {
        super(e);
    }

    public SinkException(String message, Throwable cause) {
        super(message, cause);
    }
}
