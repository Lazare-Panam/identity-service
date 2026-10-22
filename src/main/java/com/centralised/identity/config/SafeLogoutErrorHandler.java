package com.centralised.identity.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SafeLogoutErrorHandler implements AuthenticationFailureHandler {
    private static final String DEFAULT_CLIENT = "kosaplus-web";
    private final RegisteredClientRepository clients;

    public SafeLogoutErrorHandler(RegisteredClientRepository clients) {
        this.clients = clients;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();

        String clientId = request.getParameter("client_id") != null ? request.getParameter("client_id") : DEFAULT_CLIENT;
        RegisteredClient client = clients.findByClientId(clientId);
        String target = request.getParameter("post_logout_redirect_uri");
        if (client != null && target != null && client.getPostLogoutRedirectUris().contains(target)) {
            response.sendRedirect(target);
        } else {
            response.sendRedirect("/login?logout");
        }
    }
}
