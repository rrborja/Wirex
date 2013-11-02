/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.wirex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import net.wirex.interfaces.Model;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.HttpMessageConverterExtractor;

/**
 *
 * @author RBORJA
 */
public class ServerResponseExtractor extends HttpMessageConverterExtractor<ServerResponse>{
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
                result = new ServerResponse<>("SUCCESS", gson.fromJson(body, responseModel));
                break;
            case ACCEPTED:
                result = gson.fromJson(body, ServerResponse.class);
                break;
            default:
                result = null;
        }
        
        return result;
    }
    
}
