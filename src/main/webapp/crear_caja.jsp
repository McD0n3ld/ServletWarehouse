<%@ page session="true" import="java.util.*, objetos.Caja, java.sql.*, javax.naming.*, javax.sql.DataSource"%>
<%
			Context envContext = new InitialContext();
			Context initContext = (Context) envContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) initContext.lookup("jdbc/datasource_warehouse");
			Connection con = ds.getConnection();
			Statement stmt = con.createStatement();
			String query = "SELECT location, code FROM warehouses";
			ResultSet rs = stmt.executeQuery(query);
			ArrayList<String> almacenes = new ArrayList<String>();
			while(rs.next()){
				almacenes.add(rs.getString("location")+" ("+rs.getInt("code")+")");
			}
%>
	<center>
		<form name="crearForm" action="/examen/servlet" method="POST">
			<b>Código: </b><input type="text" name="codigo" SIZE="4" value="DSA1">
			<b>Contenido: </b><input type="text" name="contenido" SIZE="25" value="CONTENIDO">
			<b>Valor: </b><input type="text" name="valor" SIZE="5" value=99>
			<b>Localización: </b><select name=ALMACEN>
			<% for(int i = 0;i<almacenes.size();i++) {%>
				<option><%=almacenes.get(i)%>
			<% } %> </select> <% %>

			<input type="hidden" name="action" value="CREAR"/> 
			<input type="submit" name="Submit" value="Crear caja">
		</form>
	</center>