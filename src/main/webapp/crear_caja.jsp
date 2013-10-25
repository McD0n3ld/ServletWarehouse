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
	<form name="gestionForm" action="/examen/servlet" method="POST">
		<b>Lista de cajas:</b> <select name=CAJA>
			<%
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
			%>
			<option>
				<%=c.getCode()%> | <%=c.getContents()%> | <%=c.getValue()%> | <%=c.getWarehouse_location()%> (<%=c.getCode_warehouse()%>)
			</option>
			<%
				}
			%>
		</select> 
		<input type="hidden" name="action" value="SACAR"/> <input
			type="submit" name="Submit" value="Sacar caja">
	</form>
	<p>
		<jsp:include page="historial.jsp" flush="true" />
</body>
</html>