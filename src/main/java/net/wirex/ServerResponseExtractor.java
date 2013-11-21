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
public final class ServerResponseExtractor extends HttpMessageConverterExtractor<ServerResponse>{
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
            case FOUND:
                String location = response.getHeaders().get("Location").get(0);
                result = new ServerResponse<>(HttpStatus.FOUND, location);
                LOG.info("[{}] Redirected to {}", status.value(), location);
                return result;
            default: 
                LOG.info("[{}] Server has encountered error", status.value());
                return new ServerResponse<>(HttpStatus.valueOf(status.value()), "");
        }
        
    }
    
}
