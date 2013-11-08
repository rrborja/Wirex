/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex;

import com.google.common.cache.CacheLoader;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * @author RBORJA
 */
class ServerResponseCacheLoader extends CacheLoader<ServerRequest, ServerResponse> {

    ApplicationContext applicationContext;
    RestTemplate rt;

    private static final Logger LOG = Logger.getLogger(ServerResponseCacheLoader.class.getName());

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

        LOG.log(Level.INFO, "Attempting {0} {1} {2}", new Object[]{rest, new UriTemplate(uri).expand(variables), requestBody});

        ServerResponse resultModel = rt.execute(uri, rest, requestCallback, responseExtractor, variables);

        return resultModel;
    }
}
