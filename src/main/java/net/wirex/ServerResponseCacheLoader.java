package net.wirex;

import com.google.common.cache.CacheLoader;
import java.awt.Window;
import java.util.Map;
import net.wirex.enums.Media;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

/**
 *
 * @author Ritchie Borja
 */
final class ServerResponseCacheLoader extends CacheLoader<ServerRequest, ServerResponse> {

    private static final Logger LOG = LoggerFactory.getLogger(ServerResponseCacheLoader.class.getName());

    private final WirexLock semaphore;
    
    ApplicationContext applicationContext;
    RestTemplate rt;

    void init() {
        if (applicationContext == null) {
            applicationContext = new ClassPathXmlApplicationContext("ServerContext.xml", AppEngine.class);
            rt = applicationContext.getBean("restTemplate", RestTemplate.class);
        }
    }
    
    public ServerResponseCacheLoader(WirexLock semaphore) {
        this.semaphore = semaphore;
    }

    public @Override
    ServerResponse load(ServerRequest request) throws Exception {
        init();
        
        HttpMethod rest = request.getRest();
        String uri = request.getPath();
        MediaType type = request.getMedia();
        Map variables = request.getVariables();
        String body = request.getBody();
        Class model = request.getModel();
        String requestBody = request.getRequestBody();
        Window parent = request.getParent();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(type);

        HttpEntity entity = new HttpEntity(body, headers);

        RequestCallback requestCallback = new ServerRequestCallback(entity);
        ResponseExtractor<ServerResponse> responseExtractor = new ServerResponseExtractor(parent, model, rt.getMessageConverters(), semaphore);
        
        LOG.info("Attempting {} {} {}", rest, new UriTemplate(uri).expand(variables), requestBody);

        ServerResponse resultModel = rt.execute(uri, rest, requestCallback, responseExtractor, variables);
        
        if (resultModel.getStatus().equals(HttpStatus.FOUND)) {
            ServerRequest newRequest = new ServerRequest("GET", resultModel.getMessage().toString(), Media.URLENCODED, null, null, null);
            return load(newRequest);
        }

        return resultModel;
    }
}
