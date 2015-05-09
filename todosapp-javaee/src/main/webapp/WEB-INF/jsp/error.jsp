<%-- 
  error page

  Reference: http://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
--%>

<!-- error.jsp -->
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<t:todosapp-layout title="to-do error" >
 
    <jsp:attribute  name="navigationBarActions">		
		<a title="Back" class="element" href="/todos"> <span
			class="icon-arrow-left on-left"></span><span>back</span>
		</a>	
    </jsp:attribute>

    <jsp:attribute  name="mainBody"> 
      	<h1>
			<i class="icon-bug fg-darker smaller"></i>
			Oops
		</h1>

        <p class="padding20 margin20"> ${message} Click back button to reload.

       <!--
            This is for DEV sample only. DO NOT USE FOR PRODUCTION!
            
            Failed URL: ${url}
       <c:if test="${not empty exception}">
             Exception:  ${exception.message}
             <c:forEach items="${exception.stackTrace}" var="ste">    
                  ${ste} 
            </c:forEach>
       </c:if>
        -->
    </jsp:attribute>
    
</t:todosapp-layout>