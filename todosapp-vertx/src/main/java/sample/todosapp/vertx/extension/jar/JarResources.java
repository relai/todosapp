package sample.todosapp.vertx.extension.jar;

import com.jetdrone.vertx.yoke.Middleware;
import com.jetdrone.vertx.yoke.middleware.YokeRequest;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.InvalidPathException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.Names.ETAG;
import static io.netty.handler.codec.http.HttpHeaders.Names.IF_NONE_MATCH;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_MODIFIED;
import org.vertx.java.core.http.impl.MimeMapping;

/**
 * Yoke middleware to serve static web resources in the classpath under META-INF/resources.
 * A common use is to serve webjars resources. Refer to <code>TodoApp</code> for example.
 * 
 * @author relai
 */
public class JarResources extends Middleware{
    
    private final Map<String, Resource> cache = new ConcurrentHashMap<>();
    
    public  Handler<YokeRequest> send(String filePath) {
        return (YokeRequest request) -> sendFile(request, filePath);
    }
    
    @Override
    public void handle(YokeRequest request, Handler<Object> next) {
        sendFile(request, request.normalizedPath());
    }
      
    private void sendFile(YokeRequest request, String filePath) {       
        try {           
            Resource r = getResource(filePath);  
            request.response().putHeader(ETAG, r.getEtag());
                       
            String oldEtag = request.getHeader(IF_NONE_MATCH);
            if (r.getEtag().equals(oldEtag)) {
                request.response().setStatusCode(NOT_MODIFIED.code());
                request.response().end();
            } else {
                if (r.getContentType() != null) {
                    request.response().putHeader(CONTENT_TYPE, r.getContentType());
                }
                request.response().end(r.getContent());            
            }
            
        } catch (Exception ex) {
            request.response().setStatusCode(500).end(ex.toString());   
        }          
    }

    private String getMimeType(String filePath) {        
        String result = null;
        int pos = filePath.lastIndexOf('.');
        if (pos != -1) {
             String extention = filePath.substring(pos + 1, filePath.length());
             result = MimeMapping.getMimeTypeForExtension(extention);
        }
        return result;     
    }
    
    private Resource getResource(String filePath) throws IOException {
        Resource r = cache.get(filePath);
        
        if (r == null) {
            String loc = "META-INF/resources" + filePath;
            ClassLoader classLoader = this.getClass().getClassLoader();

            URL url = classLoader.getResource(loc);
            if (url == null) {
                throw new InvalidPathException(filePath, "Does not exist in a jar file");
            }
           
            URLConnection con = url.openConnection();

            int size = con.getContentLength();
            byte[] raw = new byte[size];

            try (InputStream st = con.getInputStream();
                BufferedInputStream bst = new BufferedInputStream(st)) {
                 bst.read(raw);                      
            }
            Buffer buffer = new Buffer(raw);
            
            String etag =  "W/\"" + Long.toHexString(size) + "_" + 
                Long.toHexString(con.getLastModified()) + "\"";
                       
            r = new Resource(etag, getMimeType(filePath), buffer);
            cache.putIfAbsent(filePath, r);       
        }
        
        return r;
    }
    
    private class Resource {
        private final String etag;
        private final String contentType;
        private final Buffer content;
        Resource(String etag, String contentType, Buffer content) {
            this.etag = etag;
            this.contentType = contentType;
            this.content = content;
        }
        
        String getEtag() { return etag;}
        String getContentType() {return contentType;}
        Buffer getContent() {return content;}
    }   
}
