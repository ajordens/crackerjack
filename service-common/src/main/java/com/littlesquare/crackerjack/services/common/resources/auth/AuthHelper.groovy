package com.littlesquare.crackerjack.services.common.resources.auth

import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession
import com.littlesquare.crackerjack.services.common.api.User
import javax.servlet.http.HttpServletRequest

/**
 * @author Adam Jordens (adam@jordens.org)
 */
class AuthHelper {
    public static final String SESSION_USER = "user"

    void afterSuccess(User user, String redirectUrl, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true)
        session.setAttribute(SESSION_USER, user)

        if (!redirectUrl) {
            redirectUrl = "/members"
        }

        response.sendRedirect(redirectUrl)
    }
}
