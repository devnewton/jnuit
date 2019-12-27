/*
 The MIT License (MIT)

 Copyright (c) 2019 devnewton <devnewton@bci.im>

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */
package im.bci.jnuit.teavm;

import java.io.IOException;
import org.teavm.interop.Async;
import org.teavm.interop.AsyncCallback;
import org.teavm.jso.ajax.XMLHttpRequest;

/**
 *
 * @author devnewton
 */
public class Ajax {
    @Async
    public static native String get(String url) throws IOException;
    private static void get(String url, AsyncCallback<String> callback) {
        XMLHttpRequest xhr = XMLHttpRequest.create();
        xhr.open("get", url);
        xhr.setOnReadyStateChange(() -> {
            if (xhr.getReadyState() != XMLHttpRequest.DONE) {
                return;
            }
            
            int statusGroup = xhr.getStatus() / 100;
            if (statusGroup != 2 && statusGroup != 3) {
                callback.error(new IOException("HTTP status: " + 
                        xhr.getStatus() + " " + xhr.getStatusText()));
            } else {
                callback.complete(xhr.getResponseText());
            }
        });
        xhr.send();
    }
    
    @Async
    public static native boolean exists(String url);
    private static void exists(String url, AsyncCallback<Boolean> callback) {
        XMLHttpRequest xhr = XMLHttpRequest.create();
        xhr.open("head", url);
        xhr.setOnReadyStateChange(() -> {
            if (xhr.getReadyState() != XMLHttpRequest.DONE) {
                return;
            }
            switch(xhr.getStatus()) {
                case 200:
                    callback.complete(true);
                    break;
                case 404:
                    callback.complete(false);
                default:
                    callback.error(new IOException("HTTP status: " + 
                        xhr.getStatus() + " " + xhr.getStatusText()));
            }
        });
        xhr.send();
    }
}