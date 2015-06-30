/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/8.0.21
 * Generated at: 2015-06-29 20:33:38 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("javax.servlet");
    _jspx_imports_packages.add("javax.servlet.http");
    _jspx_imports_packages.add("javax.servlet.jsp");
    _jspx_imports_classes = null;
  }

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public java.util.Set<java.lang.String> getPackageImports() {
    return _jspx_imports_packages;
  }

  public java.util.Set<java.lang.String> getClassImports() {
    return _jspx_imports_classes;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

final java.lang.String _jspx_method = request.getMethod();
if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method) && !javax.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {
response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSPs only permit GET POST or HEAD");
return;
}

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html lang=\"en\">\n");
      out.write("\n");
      out.write("<head>\n");
      out.write("\t<title>MikeGames</title>\n");
      out.write("\t<meta charset=\"utf-8\" />\n");
      out.write("\t\n");
      out.write("\t<link rel=\"stylesheet\" href=\"stylesheet.css\" type=\"text/css\" />\n");
      out.write("\t<meta name=\"viewpot\" content=\"width=device-width, initial-scale=1.0\">\n");
      out.write("</head>\n");
      out.write("\n");
      out.write("<body class=\"body\">\n");
      out.write("\t\n");
      out.write("\t<header class=\"mainheader\">\n");
      out.write("\t\t<img src=\"img/logo.gif\">\n");
      out.write("\t\t\n");
      out.write("\t\t<nav><ul>\n");
      out.write("\t\t\t<li class=\"active\"><a href=\"#\">Home</a></li>\n");
      out.write("\t\t\t<li><a href=\"screenshots.html\">Screenshots</a></li>\n");
      out.write("\t\t\t<li><a href=\"troubleshoot.html\">Troubleshoot</a></li>\n");
      out.write("\t\t\t<li><a href=\"about.html\">About</a></li>\n");
      out.write("\t\t\t<li><a href=\"login.html\">Login</a></li>\n");
      out.write("\t\t</ul></nav>\n");
      out.write("\t</header>\n");
      out.write("\t\n");
      out.write("\t<div class=\"maincontent\">\n");
      out.write("\t\t<div class=\"content\">\n");
      out.write("\t\t\t<article class=\"topcontent\">\n");
      out.write("\t\t\t\t<header>\n");
      out.write("\t\t\t\t\t<h2 class=\"title\">GAME PLAYER</p></h2>\n");
      out.write("\t\t\t\t</header>\n");
      out.write("\n");
      out.write("\t");
			
 	if (session.getAttribute("userName") != null) {
 		//|| (session.getAttribute("userName") != "null")) {
 		out.println("Welcome " + session.getAttribute("userName"));
 	}
	 
      out.write(" \t\n");
      out.write("\t\t\n");
      out.write("\t\t\t\t\t\n");
      out.write("\n");
      out.write("\t\t\t<!--\n");
      out.write("\t\t\t\t<applet code=\"games/TetrisApplet.class\" archive=\"Tetris.jar\" width=\"800\" height=\"600\">\n");
      out.write("\t\t\t\t</applet>\n");
      out.write("\t\t\t\t-->\n");
      out.write("\t\t\t</article>\n");
      out.write("\n");
      out.write("\t\t</div>\n");
      out.write("\t</div>\n");
      out.write("\t\n");
      out.write("\n");
      out.write("\t\n");
      out.write("\t<footer class=\"mainfooter\">\n");
      out.write("\t\t<p>copyright &copy: <a href=\"=\"# title=\"mikeGAMES\">mikeGAMES.com</a></p>\n");
      out.write("\t</footer>\n");
      out.write("</body>\n");
      out.write("</html>");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
