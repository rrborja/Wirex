package net.wirex;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.wirex.interfaces.Model;
import net.wirex.structures.XList;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.HttpMessageConverterExtractor;

/**
 *
 * @author Ritchie Borja
 */
public final class ServerResponseExtractor extends HttpMessageConverterExtractor<ServerResponse> {

    public static final int OBJECT = 1;

    public static final int LIST = 2;

    private static final Logger LOG = LoggerFactory.getLogger(ServerResponseExtractor.class.getName());

    private final Class<? extends Model> responseModel;

    public ServerResponseExtractor(Class<? extends Model> responseModel, List<HttpMessageConverter<?>> messageConverters) {
        super(ServerResponse.class, messageConverters);
        this.responseModel = responseModel;
    }

    @Override
    public ServerResponse extractData(ClientHttpResponse response) throws IOException {
        ServerResponse result = new ServerResponse(HttpStatus.OK, new Model() {
        });

        HttpStatus status = response.getStatusCode();
        InputStream in = response.getBody();

        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        reader.beginObject();

        switch (status) {
            case OK:
                String feature;
                try {
                    reader.nextName();
                    feature = reader.nextString();
                    reader.nextName();
                    Integer type = reader.nextInt();
                    reader.nextName();
                    switch (type) {
                        case OBJECT:
                            result = new ServerResponse<>(HttpStatus.OK, gson.fromJson(reader, responseModel));
                            break;
                        case LIST:
                            reader.beginArray();
                            Model model = AppEngine.checkoutModel(responseModel);
                            XList list = (XList) model.streamData();
                            while (reader.hasNext()) {
                                try {
                                    Random generator = new Random();
                                    int i = generator.nextInt(300) + 1;
//                                    System.out.println(i);
                                    Thread.sleep(i);
                                } catch (InterruptedException ex) {
                                    java.util.logging.Logger.getLogger(ServerResponseExtractor.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                Object object = gson.fromJson(reader, model.streamType());
                                list.add(object);
                            }
                            reader.endArray();
                            break;
                    }
                } catch (IOException e) {
                    LOG.error("Invalid Server Response class. Check your server implementation!");
                    return null;
                } finally {
                    reader.endObject();
                    reader.close();
                }
                LOG.info("[{}] Successful server transaction from feature {}", status.value(), feature);
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
