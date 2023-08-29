package co.com.bancolombia.security;

import co.com.bancolombia.model.commons.SecretModelApiKeyInt;
import org.springframework.security.authentication.BadCredentialsException;

public class FilterSecurityApi {
    static public APIKeyAuthFilter filter(SecretModelApiKeyInt secretModelApiKeyInt, String headerName) {
        String validTokenProfile = secretModelApiKeyInt.getApiKeyInt();
        APIKeyAuthFilter filterProfile = new APIKeyAuthFilter(headerName);
        filterProfile.setAuthenticationManager(authenticationProfile -> {
            String token = (String) authenticationProfile.getPrincipal();
            if (!validTokenProfile.equals(token)) {
                throw new BadCredentialsException("Invalid API Key.");
            }
            authenticationProfile.setAuthenticated(true);
            return authenticationProfile;
        });

        return filterProfile;
    }
}
