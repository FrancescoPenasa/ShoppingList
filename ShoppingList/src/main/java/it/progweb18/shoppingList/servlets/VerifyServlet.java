package it.progweb18.shoppingList.servlets;

import it.progweb18.shoppingList.dao.UserDAO;
import it.progweb18.shoppingList.dao.entities.User;
import it.unitn.aa1718.webprogramming.persistence.utils.dao.exceptions.DAOException;
import it.unitn.aa1718.webprogramming.persistence.utils.dao.exceptions.DAOFactoryException;
import it.unitn.aa1718.webprogramming.persistence.utils.dao.factories.DAOFactory;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that handles the user verify action.
 */
public class VerifyServlet extends HttpServlet {
    private UserDAO userDao;
    
    /**
     * Initializes daos
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get dao factory for user storage system");
        }
        try {
            userDao = daoFactory.getDAO(UserDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for user storage system", ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean verified=false;
        String url="ooops.jsp";
        
        String code = request.getParameter("code");
        
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }
        
        try {
            User user=userDao.deleteByVerificationCode(code);
            if(user!=null){
                verified=true;
                url="guest.setup";
            }
            
            request.getSession().setAttribute("verified", verified);
            response.sendRedirect(response.encodeRedirectURL(contextPath + url));
        } catch (DAOException ex) {
            Logger.getLogger(VerifyServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
