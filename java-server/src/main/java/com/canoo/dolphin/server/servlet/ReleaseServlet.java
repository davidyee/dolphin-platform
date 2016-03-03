package com.canoo.dolphin.server.servlet;

import com.canoo.dolphin.server.context.DolphinContext;
import com.canoo.dolphin.server.context.WeakDolphinContextCollection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by hendrikebbers on 03.03.16.
 */
public class ReleaseServlet extends HttpServlet {

    private WeakDolphinContextCollection dolphinContextCollection;

    public ReleaseServlet(WeakDolphinContextCollection dolphinContextCollection) {
        this.dolphinContextCollection = dolphinContextCollection;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contextId = req.getParameter("DOLPHIN_CONTEXT_ID");
        DolphinContext dolphinContext = findDolphinContext(contextId);
        if(dolphinContext != null) {
            dolphinContext.getEventMonitor().release();
        } else {
            throw new RuntimeException("Can not find Dolphin Platform Context");
        }
    }

    private DolphinContext findDolphinContext(String contextId) {
        return dolphinContextCollection.get(contextId);
    }
}
