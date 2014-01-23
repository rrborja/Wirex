package net.wirex;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.awt.Window;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import net.wirex.exceptions.ViewClassNotBindedException;
import net.wirex.exceptions.WrongComponentException;
import net.wirex.gui.DetailModel;
import net.wirex.gui.ErrorReportPanel;
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

    public static final int MESSAGE = 3;

    public static final int BUG = 4;

    public static final int COMMAND = 5;

    private static final Logger LOG = LoggerFactory.getLogger(ServerResponseExtractor.class.getName());

    private final Class<? extends Model> responseModel;

    private final Window parent;

    public ServerResponseExtractor(Window parent, Class<? extends Model> responseModel, List<HttpMessageConverter<?>> messageConverters) {
        super(ServerResponse.class, messageConverters);
        this.parent = parent;
        this.responseModel = responseModel;
    }

    @Override
    public ServerResponse extractData(ClientHttpResponse response) throws IOException {
        ServerResponse result;

        HttpStatus status = response.getStatusCode();
        InputStream in = response.getBody();

        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        reader.beginObject();

        switch (status) {
            case OK:
                return ok(reader, status.value());
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

    private ServerResponse ok(JsonReader reader, int status) throws IOException {
        try {
            return read(reader, status);
        } catch (IOException e) {
            LOG.error("Invalid Server Response class. Check your server implementation!");
            return null;
        } finally {

        }
    }

    private ServerResponse read(JsonReader reader, int status) throws IOException {
        Integer type = 1;
        String feature = "";
        while (reader.hasNext()) {
            String property = reader.nextName();
            switch (property) {
                case "feature":
                    feature = reader.nextString();
                    break;
                case "type":
                    type = reader.nextInt();
                    break;
                case "body":
                    return processBody(reader, status, feature, type);
            }
        }

        LOG.info("Successful server transaction from feature {}", status, feature);
        return new ServerResponse(HttpStatus.OK, new Model() {
        });
    }

    private ServerResponse processBody(JsonReader reader, int status, String feature, int type) throws IOException {
        Gson gson = new Gson();
        switch (type) {
            case OBJECT:
                LOG.info("[{}] Successful server transaction from feature {}", status, feature);
                return new ServerResponse<>(HttpStatus.OK, gson.fromJson(reader, responseModel));
            case LIST:
                reader.beginArray();
                Model model = AppEngine.checkoutModel(responseModel);
                XList list = (XList) model.streamData();
                while (reader.hasNext()) {
                    Object object = gson.fromJson(reader, model.streamType());
                    list.add(object);
                }
                reader.endArray();
                reader.endObject();
                reader.close();
                break;
            case MESSAGE:
                MessageResponseStructure messageResponse = gson.fromJson(reader, MessageResponseStructure.class);
                String title = messageResponse.getTitle();
                int messageType = messageResponse.getType();
                String body = messageResponse.getBody();
                int dialogType = messageResponse.getKind();
                ArrayList<String> selections = messageResponse.getSelections();
                switch (dialogType) {
                    case MessageResponseDialogType.CONFIRM:
                        JOptionPane.showConfirmDialog(parent, body, title, dialogType, messageType);
                        break;
                    case MessageResponseDialogType.INPUT:
                        break;
                    case MessageResponseDialogType.MESSAGE:
                        JOptionPane.showMessageDialog(parent, body, title, messageType);
                        break;
                    case MessageResponseDialogType.OPTION:
                        Object[] options = selections.toArray();
                        JOptionPane.showOptionDialog(parent, body, title, dialogType, messageType, null, options, options[0]);
                        break;
                }
                break;
            case BUG:
                ErrorResponseStructure errorResponse = gson.fromJson(reader, ErrorResponseStructure.class);
                MVP mvp;
                try {
                    mvp = AppEngine.prepare(ErrorReportPanel.class);
                } catch (ViewClassNotBindedException | WrongComponentException ex) {
                    reader.close();
                    break;
                }
                DetailModel errorModel = (DetailModel) AppEngine.checkoutModel(DetailModel.class);
                errorModel.setReport("Caused by: " + errorResponse.getException() + ": " + errorResponse.getMessage() + "\n" + errorResponse.getLogs());
                mvp.display(JDialog.class);
                break;
            case COMMAND:
                Integer command = gson.fromJson(reader, Integer.class);
                LOG.info("[{}] Server returned a command response {} from feature {}", status, command, feature);
                return new ServerResponse<>(HttpStatus.OK, command);
        }
        return new ServerResponse(HttpStatus.OK, new Model() {
        });
    }

    private static class MessageResponseDialogType {

        public static final int CONFIRM = 1;
        public static final int INPUT = 2;
        public static final int MESSAGE = 3;
        public static final int OPTION = 4;

    }

    private static class MessageResponseType {

        public static final int ERROR = 0;
        public static final int INFO = 1;
        public static final int WARN = 2;
        public static final int QUESTION = 3;

    }

    private static class LegacyCommands {

        public static final int LOGIN = 0;

    }

    private class ErrorResponseStructure {

        private int kind;
        private String message;
        private String exception;
        private String cause;
        private String logs;

        public int getKind() {
            return kind;
        }

        public void setKind(int kind) {
            this.kind = kind;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getException() {
            return exception;
        }

        public void setException(String exception) {
            this.exception = exception;
        }

        public String getCause() {
            return cause;
        }

        public void setCause(String cause) {
            this.cause = cause;
        }

        public String getLogs() {
            return logs;
        }

        public void setLogs(String logs) {
            this.logs = logs;
        }

    }

    private class MessageResponseStructure {

        private boolean show;
        private String title;
        private int type;
        private String body;
        private int kind;
        private ArrayList<String> selections;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public int getKind() {
            return kind;
        }

        public void setKind(int kind) {
            this.kind = kind;
        }

        public ArrayList<String> getSelections() {
            return selections;
        }

        public void setSelections(ArrayList<String> selections) {
            this.selections = selections;
        }

        public boolean isShow() {
            return show;
        }

        public void setShow(boolean show) {
            this.show = show;
        }

    }

}
