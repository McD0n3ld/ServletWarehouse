package MiServlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

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
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String url = "/index.jsp";
		ServletContext sc = getServletContext();
		RequestDispatcher rd = sc.getRequestDispatcher(url);
		rd.forward(req, res);
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
		boolean validarSesion = true;
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
			c.setNuevo(true);
			
			boolean encontrado = false;
			try {
				Context envContext = new InitialContext();
				Context initContext = (Context) envContext.lookup("java:/comp/env");
				DataSource ds = (DataSource) initContext.lookup("jdbc/datasource_warehouse");
				Connection con = ds.getConnection();
				Statement stmt = con.createStatement();
				String query = "SELECT code FROM boxes WHERE code='"+c.getCode()+"'";
				ResultSet rs = stmt.executeQuery(query);
				if (rs.next())
					encontrado = true;
			} catch(Exception e) { e.printStackTrace(); }
			
			// buscar en la lista
			if (!encontrado) {
				for (int i = 0; i<lista.size(); i++) {
					if (c.getCode().equals(lista.get(i).getCode()))
						encontrado = true;
					if (encontrado)
						break;
				}
			}

			// si no lo encuentra, lo añade a la lista
			if(!encontrado)
				lista.add(c);
		} else if (action.equals("VALIDAR")) {
			validarSesion = false;
			try {
				Context envContext = new InitialContext();
				Context initContext = (Context) envContext.lookup("java:/comp/env");
				DataSource ds = (DataSource) initContext.lookup("jdbc/datasource_warehouse");
				Connection con = ds.getConnection();
				Statement stmt = con.createStatement();
				for (int i = 0; i < lista.size(); i++) {
					Caja c = lista.get(i);
					if (c.getEstado() == false) { // hemos sacado la caja, la borramos de la base de datos.
						String query = "DELETE FROM boxes WHERE code='"+c.getCode()+"'";
						stmt.executeUpdate(query);
					} else {
						if (c.isNuevo() == true) {
							String query = "INSERT INTO boxes VALUES('"+c.getCode()+"','"+c.getContents()+"','"+c.getValue()+"','"+c.getCode_warehouse()+"')";
							stmt.executeUpdate(query);
						} else {
							String query = "UPDATE boxes SET warehouse='"+c.getCode_warehouse()+"' WHERE code='"+c.getCode()+"'";
							stmt.executeUpdate(query);
						}
					}
				}
			}
			catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		// refrescamos la session
		if (validarSesion == true) {
			session.putValue("historial", lista);
			String url = "/index.jsp";
			ServletContext sc = getServletContext();
			RequestDispatcher rd = sc.getRequestDispatcher(url);
			rd.forward(req, res);
		} else {
			String url = "/validar.jsp";
            ServletContext sc = getServletContext();
            RequestDispatcher rd = sc.getRequestDispatcher(url);
            rd.forward(req, res);
		}
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
