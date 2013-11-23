package com.littlesquare.crackerjack.services.common.resources

import com.littlesquare.crackerjack.services.common.resources.auth.AuthHelper
import com.littlesquare.crackerjack.services.common.views.BaseViewModel

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

public class ViewUtils {
    static BaseViewModel processView(BaseViewModel view, HttpServletRequest request) {
        HttpSession session = request.getSession(false)
        if (session) {
            view.data.put("currentUser", session.getAttribute(AuthHelper.SESSION_USER))
        }

        return view
    }
}
