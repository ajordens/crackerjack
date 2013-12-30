package com.littlesquare.crackerjack.services.ui

import com.littlesquare.crackerjack.services.common.auth.SessionAuthInjectable
import com.littlesquare.crackerjack.services.common.auth.api.ExternalPerson

import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession
import javax.servlet.http.HttpServletRequest

/**
 * @author Adam Jordens (adam@jordens.org)
 */
class AuthHelper {
    public static String SESSION_USER = SessionAuthInjectable.SESSION_USER

    public void afterSuccess(ExternalPerson user, String redirectUrl, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true)
        session.setAttribute(SESSION_USER, user)

        if (!redirectUrl) {
            redirectUrl = "/members"
        }

        response.sendRedirect(redirectUrl)
    }
}
