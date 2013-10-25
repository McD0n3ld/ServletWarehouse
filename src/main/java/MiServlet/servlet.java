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
		// try {
		// Context envContext = new InitialContext();
		// Context initContext = (Context) envContext.lookup("java:/comp/env");
		// DataSource ds = (DataSource)
		// initContext.lookup("jdbc/datasource_conciertos");
		// Connection con = ds.getConnection();
		// Statement stmt = con.createStatement();
		// String query =
		// "SELECT boxes.*,warehouses.location FROM boxes INNER JOIN warehouses ON (boxes.warehouse=warehouses.code)";
		// ResultSet rs = stmt.executeQuery(query);
		// ArrayList<Caja> lista = new ArrayList<Caja>();
		// while (rs.next()) {
		// Caja c = new Caja();
		// c.setCode(rs.getString("code"));
		// c.setCode_warehouse(rs.getInt("warehouse"));
		// c.setContents(rs.getString("contents"));
		// c.setValue(rs.getInt("value"));
		// c.setWarehouse_location(rs.getString("location"));
		// lista.add(c);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// System.out.println("FALLA 1");
		// }
		// System.out.println("Si que entro");
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
			//buscar la caja y modificarla
			Caja c = lista.get(Integer.parseInt(req.getParameter("modindex")));
			c = modificarCaja(req, c);
		}
		
		
		// refrescamos la session
		session.putValue("historial", lista);
        String url = "/crear_caja.jsp";
        ServletContext sc = getServletContext();
        RequestDispatcher rd = sc.getRequestDispatcher(url);
        rd.forward(req, res);
	}

	private Caja sacarCaja(HttpServletRequest req) {
		String myCaja = req.getParameter("CAJA");
		StringTokenizer t = new StringTokenizer(myCaja, " | ");
		Caja c = new Caja();
		c.setCode(t.nextToken());
		c.setContents(t.nextToken());
		c.setValue(Integer.parseInt(t.nextToken()));
		c.setWarehouse_location(t.nextToken());
		String tmp = t.nextToken();
		tmp = tmp.substring(1,tmp.length()-1);
		c.setCode_warehouse(Integer.parseInt(tmp));
		c.setEstado(false);	//la hemos sacado
		return c;
	}
	
	private Caja modificarCaja(HttpServletRequest req, Caja c)  {
		String temp = req.getParameter("ALMACEN");
		StringTokenizer t = new StringTokenizer(temp, "(");
		c.setWarehouse_location(t.nextToken());
		String tmp = t.nextToken();
		tmp = tmp.substring(0,tmp.length()-1);
		int id = Integer.parseInt(tmp);
		c.setCode_warehouse(id);
		c.setEstado(true);
		return c;
	}

}
