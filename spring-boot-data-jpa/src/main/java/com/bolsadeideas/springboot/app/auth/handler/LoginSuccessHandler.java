package com.bolsadeideas.springboot.app.auth.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// crea un handler
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        
        // obtengo un array de mensajes flash, en concreto de success
        SessionFlashMapManager flashMapManager = new SessionFlashMapManager();

        // crea un map de mensajes
        FlashMap flashMap = new FlashMap();

        flashMap.put("success", "Hola: " + authentication.getName() + " has iniciado sesión con éxito");

        // registra el mensaje en el manager
        flashMapManager.saveOutputFlashMap(flashMap, request, response);

        if (authentication != null) {
            logger.info("el usuario '" + authentication.getName() + "' ha iniciado sesión con éxito");
        }

        super.onAuthenticationSuccess(request, response, authentication);

        // a continuación se pasa en SpringSecurityConfig a login()
    }

}
