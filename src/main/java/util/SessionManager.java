package util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public interface SessionManager {
    HttpSession getSession(HttpServletRequest request);
    HttpSession getSession(HttpServletRequest request, boolean create);
}
