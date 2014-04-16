package net.wirex;

import com.google.common.cache.CacheLoader;
import java.awt.Window;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import net.wirex.enums.Media;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

    private static ServerResponseCacheLoader INSTANCE;

    ApplicationContext applicationContext;
    RestTemplate rt;

    public static ServerResponseCacheLoader getInstance(WirexLock semaphore) {
        if (INSTANCE == null) {
            INSTANCE = new ServerResponseCacheLoader(semaphore);
        }
        return INSTANCE;
    }

    void init() {
        if (applicationContext == null) {
            applicationContext = new ClassPathXmlApplicationContext("ServerContext.xml", AppEngine.class);
            rt = applicationContext.getBean("restTemplate", RestTemplate.class);
        }
    }

    private ServerResponseCacheLoader(WirexLock semaphore) {
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

        Map<String, String> userInfo = SessionController.credentials();
        String username = userInfo.get("username");
        String password = userInfo.get("password");
        String token = username + ":" + password;
        String encodedAuthorization = Base64.getEncoder().encodeToString(token.getBytes());

        headers.add("User-Agent", "Ultimate-Back-Office/7.0");
        headers.add("Authorization", "Basic " + encodedAuthorization);
        headers.add("Cookie", "JSESSIONID=" + SessionController.getCookie());

        HttpEntity entity = new HttpEntity(body, headers);

        Map<String, String> uploadFiles = request.getAttachments();
        if (uploadFiles != null && !uploadFiles.isEmpty()) {
            String zipFilename = "attachment" + SessionController.getCookie() + ".zip";
            FileOutputStream fout = new FileOutputStream(zipFilename);
            ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(fout));
            for (Map.Entry<String, String> entry : uploadFiles.entrySet()) {
                String url = entry.getKey();
                String contentType = entry.getValue();
                File file = new File(url);
                byte[] data = new byte[1024];
                FileInputStream fis = new FileInputStream(file);
                zout.putNextEntry(new ZipEntry(file.getName()));
                int length;
                while ((length = fis.read(data)) > 0) {
                    zout.write(data, 0, length);
                }
                zout.closeEntry();
                fis.close();
            }
            fout.close();
            zout.close();
            MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
            parts.add("file", new FileSystemResource(zipFilename));
            LOG.info("Attempting to upload ({}) files to ", uploadFiles.size(), new UriTemplate(uri).expand(variables));
            ResponseStructure response = rt.postForObject(uri+"/upload", parts, ResponseStructure.class);
            if (response.getType() != 0) {
                LOG.error("Uploading failed");
                return null;
            }
        }

        RequestCallback requestCallback = new ServerRequestCallback(entity);
        ResponseExtractor<ServerResponse> responseExtractor = new ServerResponseExtractor(parent, model, rt.getMessageConverters(), semaphore);

        LOG.info("Attempting {} {} {}", rest, new UriTemplate(uri).expand(variables), requestBody);

        ServerResponse resultModel;
        semaphore.lockSending();
        try {
            resultModel = rt.execute(uri, rest, requestCallback, responseExtractor, variables);
        } finally {
            semaphore.unlockSending();
        }

        if (resultModel.getStatus().equals(HttpStatus.FOUND)) {
            ServerRequest newRequest = new ServerRequest("GET", resultModel.getMessage().toString(), Media.URLENCODED, null, null, null);
            return load(newRequest);
        }

        return resultModel;
    }
}
