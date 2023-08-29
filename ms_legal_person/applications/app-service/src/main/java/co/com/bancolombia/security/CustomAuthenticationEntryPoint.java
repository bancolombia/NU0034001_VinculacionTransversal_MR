package co.com.bancolombia.security;

import co.com.bancolombia.api.model.MetaResponse;
import co.com.bancolombia.api.model.util.Error;
import co.com.bancolombia.api.model.util.ErrorItem;
import co.com.bancolombia.api.model.util.Request;
import com.amazonaws.util.json.Jackson;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        BufferedReader reader = request.getReader();
        StringBuilder builder = new StringBuilder();
        String jsonRep;
        while ((jsonRep = reader.readLine()) != null) {
            builder.append(jsonRep);
        }
        jsonRep = builder.toString();
        Request requestObject = Jackson.fromJsonString(jsonRep, Request.class);
        List<ErrorItem> errorItems = Stream.of(new ErrorItem("Error ApiKey", "TVNT010",
                "El apikey no est\u00e1 presente en la petici\u00f3n o " +
                        "es incorrecto"))
                .collect(Collectors.toList());
        MetaResponse metaResponse = MetaResponse.fromMeta(requestObject.getMeta());
        Error error = new Error(errorItems, metaResponse);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(Jackson.toJsonString(error));
    }
}
