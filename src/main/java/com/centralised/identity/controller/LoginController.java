package com.centralised.identity.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    private static final String REGISTER_SHOWN = "registerShownFor";
    private final RequestCache requestCache = new HttpSessionRequestCache();

    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        // The website's "Create account" button starts a normal sign-in with screen=register:
        // show the sign-up page first (once per sign-in attempt); after sign-up the customer signs in
        // here and Spring continues the original sign-in back to the website.
        SavedRequest saved = requestCache.getRequest(request, response);
        if (saved != null && request.getQueryString() == null) {
            String[] screen = saved.getParameterValues("screen");
            String url = saved.getRedirectUrl();
            if (screen != null && "register".equals(screen[0]) && !url.equals(session.getAttribute(REGISTER_SHOWN))) {
                session.setAttribute(REGISTER_SHOWN, url);
                return "redirect:/register";
            }
        }
        return "login";
    }
}
