package MiServlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import objetos.Caja;

/**
 * Servlet implementation class servlet
 */
public class servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public servlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(false); // cojemos la sesion
		if (session == null)
			res.sendRedirect("http://localhost:8080/error.html");
		ArrayList<Caja> lista = (ArrayList<Caja>) session.getValue("historial");
		if (lista == null)
			lista = new ArrayList<Caja>();
		// vamos a hacer una accion:
		String action = req.getParameter("action");

		if (action.equals("SACAR")) {
			Caja c = sacarCaja(req);
			lista.add(c);
		} else if (action.equals("DELETE")) {
			lista.remove(Integer.parseInt(req.getParameter("delindex")));
		} else if (action.equals("GUARDAR")) {
			// buscar la caja y modificarla
			Caja c = lista.get(Integer.parseInt(req.getParameter("modindex")));
			c = modificarCaja(req, c);
		} else if (action.equals("CREAR")) {
			Caja c = new Caja();
			c.setCode(req.getParameter("codigo"));
			c.setContents(req.getParameter("contenido"));
			c.setValue(Integer.parseInt(req.getParameter("valor")));
			c = modificarCaja(req, c);
			lista.add(c);
		} else if (action.equals("VALIDAR"))

		// refrescamos la session
		session.putValue("historial", lista);
		String url = "/index.jsp";
		ServletContext sc = getServletContext();
		RequestDispatcher rd = sc.getRequestDispatcher(url);
		rd.forward(req, res);
	}

	private Caja sacarCaja(HttpServletRequest req) {
		String myCaja = req.getParameter("CAJA");
		StringTokenizer t = new StringTokenizer(myCaja, "|");
		Caja c = new Caja();
		String code = t.nextToken();
		String content = t.nextToken();
		String value = t.nextToken();
		String tmp = t.nextToken();
		t = new StringTokenizer(tmp, "(");
		String location = t.nextToken();
		String id_location = t.nextToken();

		c.setCode(code.substring(0, code.length() - 1));
		c.setContents(content.substring(1, content.length() - 1));
		c.setValue(Integer.parseInt(value.substring(1, value.length() - 1)));
		c.setWarehouse_location(location.substring(1, location.length() - 1));
		c.setCode_warehouse(Integer.parseInt(id_location.substring(0, id_location.length() - 1)));
		c.setEstado(false); // la hemos sacado
		return c;
	}

	private Caja modificarCaja(HttpServletRequest req, Caja c) {
		String temp = req.getParameter("ALMACEN");
		StringTokenizer t = new StringTokenizer(temp, "(");
		c.setWarehouse_location(t.nextToken());
		String tmp = t.nextToken();
		tmp = tmp.substring(0, tmp.length() - 1);
		int id = Integer.parseInt(tmp);
		c.setCode_warehouse(id);
		c.setEstado(true);
		return c;
	}

}
