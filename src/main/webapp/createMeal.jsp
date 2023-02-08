<%@ page import="java.time.LocalDateTime" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>CreateMeal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Create new meal</h2>
<ul>

  <form action = "meals?action=create" method = "POST">
    Date and time:<input type = "datetime-local" value="<%=LocalDateTime.now()%>" name = "dateTime" required>
    <br/>
    Description: <input type = "text" name = "description" required/>
    <br/>
    Calories: <input type="number" name="calories" required/>
    <br/>
    <input type = "submit" value = "SAVE" />
    <button onclick="window.history.back()" type="button">Cancel</button>
  </form>

</ul>
</body>
</html>
