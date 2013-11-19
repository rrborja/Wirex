/*
 * The MIT License
 *
 * Copyright 2013 Ritchie Borja.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.wirex;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.client.RequestCallback;

/**
 *
 * @author Ritchie Borja
 */
public class ServerRequestCallback implements RequestCallback {

    protected final Log logger = LogFactory.getLog(getClass());
    private final HttpEntity requestEntity;

    public ServerRequestCallback(HttpEntity requestEntity) {
        this.requestEntity = requestEntity;
    }

    @Override
    public void doWithRequest(ClientHttpRequest httpRequest) throws IOException {
        HttpHeaders httpHeaders = httpRequest.getHeaders();
        HttpHeaders requestHeaders = requestEntity.getHeaders();
        if (!requestHeaders.isEmpty()) {
            httpHeaders.putAll(requestHeaders);
        }
        InputStream inputStream = new ByteArrayInputStream(requestEntity.getBody().toString().getBytes());
        OutputStream requestOutputStream = httpRequest.getBody();
        try {
            int copiedBytes = IOUtils.copy(inputStream, requestOutputStream);
        } finally {
            IOUtils.closeQuietly(requestOutputStream);
        }
    }
}
