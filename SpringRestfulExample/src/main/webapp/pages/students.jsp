<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<p>Classroom of students</p>
 
<c:forEach var="student" items="${classRoom.students}">
	${student.id} - ${student.name}<br/>
</c:forEach>