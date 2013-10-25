<%@ page session="true" import="java.util.*, objetos.Caja"%>
<html>
<head>
<title>Validar cambios</title>
</head>
<body bgcolor="#9DDEF4">
	<font face="Times New Roman,Times" size=+3> Validar cambios </font>
	<hr>
	<p>
	<table border="0" cellpadding="0" width="100%" bgcolor="#FFFFFF">
		<tr>
			<td><b>Código</b></td>
			<td><b>Contenido</b></td>
			<td><b>Valor</b></td>
			<td><b>Localización</b></td>
			<td></td>
		</tr>
		<%
			ArrayList<Caja> lista = (ArrayList<Caja>) session.getValue("historial");
			for (int index = 0; index < lista.size(); index++) {
				Caja c = (Caja) lista.get(index);
		%>
		<tr>
			<td><b><%=c.getCode()%></b></td>
			<td><b><%=c.getContents()%></b></td>
			<td><b><%=c.getValue()%></b></td>
			<td><b><%=c.getWarehouse_location()%></b></td>
		</tr>
		<%
			}
			session.invalidate();
		%>
	</table>
	<p>
		<a href="/examen/index.jsp">Realizar más cambios</a>
</body>
</html>