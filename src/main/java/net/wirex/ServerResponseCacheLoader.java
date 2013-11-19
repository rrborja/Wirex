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

import com.google.common.cache.CacheLoader;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.wirex.exceptions.UnsupportedMediaTypeException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

/**
 *
 * @author Ritchie Borja
 */
class ServerResponseCacheLoader extends CacheLoader<ServerRequest, ServerResponse> {

    ApplicationContext applicationContext;
    RestTemplate rt;

    private static final Logger LOG = LoggerFactory.getLogger(ServerResponseCacheLoader.class.getName());

    void init() {
        if (applicationContext == null) {
            applicationContext = new ClassPathXmlApplicationContext("ServerContext.xml", AppEngine.class);
            rt = applicationContext.getBean("restTemplate", RestTemplate.class);
        }
    }

    public @Override
    ServerResponse load(ServerRequest request) throws Exception {
        init();

        HttpMethod rest = request.getRest();
        String uri = request.getPath();
        MediaType type = request.getMedia();
        Map variables = request.getVariables();
        MultiValueMap headerMap = request.getHeaderMap();
        String body = request.getBody();
        Class model = request.getModel();
        String requestBody = request.getRequestBody();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(type);

        HttpEntity entity;

        if (type == MediaType.APPLICATION_JSON) {
            entity = new HttpEntity(body, headers);
        } else if (type == MediaType.APPLICATION_FORM_URLENCODED) {
            entity = new HttpEntity(headerMap, headers);
        } else {
            throw new UnsupportedMediaTypeException(type + " is not yet supported.");
        }

        RequestCallback requestCallback = new ServerRequestCallback(entity);
        ResponseExtractor<ServerResponse> responseExtractor = new ServerResponseExtractor(model, rt.getMessageConverters());

        LOG.info("Attempting {} {} {}", rest, new UriTemplate(uri).expand(variables), requestBody);

        ServerResponse resultModel = rt.execute(uri, rest, requestCallback, responseExtractor, variables);

        return resultModel;
    }
}
