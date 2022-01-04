package util;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
@SuppressWarnings("unchecked")
public class JerseyRequestHandlerSpec {

    @Path("/")
    public static class TestResource {
        @GET
        @Path("test")
        public String test() {
            return "It works!";
        }
    }

    private int filterNumber = 41;

    @Test
    public void handleRequest() throws Exception {
        assertEquals(
            new TestResource().test(),
            new JerseyRequestHandler(
                "root",
                new ResourceConfig(TestResource.class),
                Optional.empty(),
                new Filter() {
                    @Override
                    public void init(FilterConfig filterConfig) throws ServletException {}
                    @Override
                    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                        throws IOException, ServletException {
                        filterNumber = 42;
                        chain.doFilter(request, response);
                    }
                    @Override
                    public void destroy(){}
                }
            ).handleRequest(new HashMap<String, Object>() {{
                put("body", null);
                put("path", "/root/test");
                put("httpMethod", "GET");
                put("headers", new HashMap<String, String>());
                put("queryStringParameters", new HashMap<String, String>());
                put("requestContext", new HashMap<String, String>(){{
                    put("sourceIp", "test-invoke-source-ip");
                }});
            }}, null).get("body")
        );
        // check that the filter works
        assertEquals(42, filterNumber);
    }
}
