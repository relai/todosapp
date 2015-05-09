<!-- todo-list.jsp -->
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<t:todosapp-layout title="to-do list" pageScript="/webjars/todosapp/static/js/todo-list.js" >
 
    <jsp:attribute  name="navigationBarActions">
        <a title="create a new to-do todo" class="element" href="/todos/create"> 
            <span class="icon-plus-2 on-left"></span><span>new todo</span>
        </a>
    </jsp:attribute>

    <jsp:attribute  name="mainBody"> 
       <div id="controlSection" class="grid pos-rel">
         <h1> 
            <i class="icon-clipboard-2 smaller"></i> to-do<small class="on-right">list</small>
         </h1>

         <div class="button-set place-bottom-right" data-role="button-group" id="filterButtons">
            <span class="place-left on-left-more icon-arrow-right-3" style="margin-top: 5px"></span>
             <button id="all"  ${filter == 'all' ?  'class="active"' : ''} 
                title="view all todos"> all </button>
            <button id="open" ${filter == 'open' ? 'class="active"' : ''} 
                title="view only open todos"> open </button>
            <button id="done" ${filter == 'done' ? 'class="active"' : ''}
                title="view only done todos" > done </button>           
         </div>
       </div>
       <table class="table hovered">
          <thead>
             <tr>
                <th class="text-left span1" style="border-bottom: 1px solid #111;">Done</th>
                <th class="text-left span8" style="border-bottom: 1px solid #111;">Name</th>
                <th class="text-left span3" style="border-bottom: 1px solid #111;">Priority</th>
             </tr>
          </thead>
          <tbody>
             <c:forEach items="${todos}" var="item">  
                <spring:url value="/todos/{id}" var="todoUrl">
                        <spring:param name="id" value="${item.id}"/>
                </spring:url>
                <tr>
                     <td>
                         <i class="icon-checkbox${item.completed ? ' ' : '-unchecked'} fg-grayLight"></i>
                      </td>
                     <td>
                         <a href="${todoUrl}"> <c:out value="${item.name}"/> </a>
                     </td>
                     <td>${item.priority}</td>
                </tr>
             </c:forEach>  
         </tbody>
       </table>
    </jsp:attribute>
    
</t:todosapp-layout>