<%@ page session="true" import="java.util.*, objetos.Caja, java.sql.*, javax.naming.*, javax.sql.DataSource"%>
<%
	ArrayList<Caja> lista = (ArrayList<Caja>) session.getValue("historial");
	if (lista != null && (lista.size() > 0)) {
%>
<center>
	<table border="0" cellpadding="0" width="100%" bgcolor="#FFFFFF">
		<tr>
			<td><b>Código</b></td>
			<td><b>Contenido</b></td>
			<td><b>Valor</b></td>
			<td><b>Estado</b></td>
			<td><b>Localización</b></td>
			<td><b>Acción</b></td>
			<td></td>
		</tr>
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
			
			for (int index = 0; index < lista.size(); index++) {
					Caja c = (Caja) lista.get(index);
		%>
		<tr>
			<td><b><%=c.getCode()%></b></td>
			<td><b><%=c.getContents()%></b></td>
			<td><b><%=c.getValue()%></b></td>
			<td><b><%=c.getEstado()%></b></td>
			<td>	
<%
	if (c.getEstado() == false) {
		%><form name="gestionForm" action="/examen/servlet" method="POST">
			<select name=ALMACEN><%
		for(int i = 0;i<almacenes.size();i++){
			%>
			<option><%=almacenes.get(i)%>
			<%
		}
		%></select> 
		<input type="hidden" name="action" value="GUARDAR"/> 
		<input type="hidden" name="modindex" value='<%=index%>'>
		<input type="submit" name="Submit" value="Guardar caja">
	</form><%
	} else {
%>		<%=c.getWarehouse_location() %><% } %>
		</td>
			
			
			
			
			
			
			
			
			<td>
				<form name="deleteForm" action="/examen/servlet"
					method="POST">
					<input type="submit" value="Eliminar cambio"> <input type="hidden"
						name="delindex" value='<%=index%>'> <input type="hidden"
						name="action" value="DELETE">
				</form>
			</td>
		</tr>
		<%
			}
		%>
	</table>
	<p>
	<form name="checkoutForm" action="/ServletConcierto/ComprarEntradas"
		method="POST">
		<input type="hidden" name="action" value="CHECKOUT"> <input
			type="submit" name="Checkout" value="NONONONONO">
	</form>
</center>
<%
	}
%>