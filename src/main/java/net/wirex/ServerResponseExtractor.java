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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.wirex.interfaces.Model;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.HttpMessageConverterExtractor;

/**
 *
 * @author Ritchie Borja
 */
public class ServerResponseExtractor extends HttpMessageConverterExtractor<ServerResponse>{
    private static final Logger LOG = LoggerFactory.getLogger(ServerResponseExtractor.class.getName());
    
    private final Class<? extends Model> responseModel;

    public ServerResponseExtractor(Class<? extends Model> responseModel, List<HttpMessageConverter<?>> messageConverters) {
        super(ServerResponse.class, messageConverters);
        this.responseModel = responseModel;
    }

    @Override
    public ServerResponse extractData(ClientHttpResponse response) throws IOException {
        ServerResponse result;
        
        HttpStatus status = response.getStatusCode();
        InputStream in = response.getBody();
        String body = IOUtils.toString(in, "UTF-8");
        Gson gson = new GsonBuilder().create();
        
        switch(status) {
            case OK:
                result = new ServerResponse<>(HttpStatus.OK, gson.fromJson(body, responseModel));
                LOG.info("[{}] Successful server transaction", status.value());
                return result;
            case ACCEPTED:
                result = new ServerResponse<>(HttpStatus.ACCEPTED, response.getHeaders().get("SessionID").get(0));
                LOG.info("[{}] Successful authorization", status.value());
                return result;
            default: 
                LOG.info("[{}] Server has encountered error", status.value());
                return new ServerResponse<>(HttpStatus.valueOf(status.value()), "");
        }
        
    }
    
}
