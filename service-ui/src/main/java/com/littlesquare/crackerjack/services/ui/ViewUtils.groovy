package com.littlesquare.crackerjack.services.ui

import com.littlesquare.crackerjack.services.common.auth.SessionAuthInjectable
import com.littlesquare.crackerjack.services.ui.views.BaseViewModel

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

public class ViewUtils {
    public static BaseViewModel processView(BaseViewModel view, HttpServletRequest request) {
        HttpSession session = request.getSession(false)
        if (session) {
            view.data.put("currentUser", session.getAttribute(SessionAuthInjectable.SESSION_USER))
        }

        return view
    }
}
