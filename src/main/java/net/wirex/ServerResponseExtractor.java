package net.wirex;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
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
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        Gson gson = new Gson();
//        SwingUtilities.invokeLater(() -> {
            try {
                reader.beginObject();
                while (reader.hasNext()) {
                    String name = reader.nextName();
                    if (name.equals("mainTable")) {
                        reader.beginArray();
                        Model model = AppEngine.checkoutModel(responseModel);
                        XList list = (XList) model.returnLiveData();
                        System.out.println("hahahaha");
                        System.out.println(list);
                        while (reader.hasNext()) {
                            Thread.sleep(10);
//                            SwingUtilities.invokeLater(() -> {
                            Object object = gson.fromJson(reader, model.returnLiveType());
                            list.add(object);
//                            });
                        }
                    }
                }
            } catch (IOException e) {
            } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(ServerResponseExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
                reader.close();
            }
//        });

//        String body = IOUtils.toString(in, "UTF-8");
//        Gson gson = new GsonBuilder().create();
//
//        switch (status) {
//            case OK:
//                result = new ServerResponse<>(HttpStatus.OK, gson.fromJson(body, responseModel));
//                LOG.info("[{}] Successful server transaction", status.value());
//                return result;
//            case ACCEPTED:
//                result = new ServerResponse<>(HttpStatus.ACCEPTED, response.getHeaders().get("SessionID").get(0));
//                LOG.info("[{}] Successful authorization", status.value());
//                return result;
//            case FOUND:
//                String location = response.getHeaders().get("Location").get(0);
//                result = new ServerResponse<>(HttpStatus.FOUND, location);
//                LOG.info("[{}] Redirected to {}", status.value(), location);
//                return result;
//            default:
//                LOG.info("[{}] Server has encountered error", status.value());
//                return new ServerResponse<>(HttpStatus.valueOf(status.value()), "");
//        }
        return null;
    }

}
