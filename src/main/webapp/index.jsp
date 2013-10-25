<%@ page session="true"	
	import="java.util.*, objetos.Caja, java.sql.*, javax.naming.*, javax.sql.DataSource"%>
<html>
<head>
<title>Gestión de cajas</title>
</head>
<body bgcolor="#FEA9A9">
	<font face="Times New Roman,Times" size="+3">Gestión de cajas</font>
	<hr>
	<p style="text-align: center;">
	<%
		ArrayList<Caja> lista = (ArrayList<Caja>) session.getValue("historial");
		if (lista != null && (lista.size() > 0)) {
			%>Ya has iniciado una sesión<%
		}
	%>
	<form name="gestionForm" action="/examen/servlet" method="POST">
		<b>Lista de cajas:</b> <select name=CAJA>
			<%
				boolean encontrado = false;
				int elementos = 0;
				Context envContext = new InitialContext();
				Context initContext = (Context) envContext.lookup("java:/comp/env");
				DataSource ds = (DataSource) initContext.lookup("jdbc/datasource_warehouse");
				Connection con = ds.getConnection();
				Statement stmt = con.createStatement();
				String query = "SELECT boxes.*,warehouses.location FROM boxes INNER JOIN warehouses ON (boxes.warehouse=warehouses.code)";
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					Caja c = new Caja();
					c.setCode(rs.getString("code"));
					c.setCode_warehouse(rs.getInt("warehouse"));
					c.setContents(rs.getString("contents"));
					c.setValue(rs.getInt("value"));
					c.setWarehouse_location(rs.getString("location"));
					encontrado = false;
					if (lista != null && (lista.size() > 0)) {
						for(int i=0;i<lista.size();i++){
							if (c.getCode().equals(lista.get(i).getCode()))
									encontrado = true;
							if (encontrado)
								break;
						}
					}
					if (!encontrado) {
						elementos++;
			%>
			<option>
				<%=c.getCode()%> | <%=c.getContents()%> | <%=c.getValue()%> | <%=c.getWarehouse_location()%> (<%=c.getCode_warehouse()%>)
			</option>
			<%
					}
				}
			%>
		</select> 
		<input type="hidden" name="action" value="SACAR"/> 
		<% if(elementos>0) {%>
		<input type="submit" name="Submit" value="Sacar caja">
		<% } %>
	</form>
	<p>
		<jsp:include page="crear_caja.jsp" flush="true" />
		<jsp:include page="historial.jsp" flush="true" />
</body>
</html>