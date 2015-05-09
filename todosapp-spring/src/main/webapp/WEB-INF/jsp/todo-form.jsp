<!-- todo-form.jsp -->
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<t:todosapp-layout title="to-do item" pageScript="/webjars/todosapp/static/js/todo-form.js" >
 
    <jsp:attribute  name="navigationBarActions">
		<a title="Save" class="element" href="#" id="save"> <span
			class="icon-checkmark on-left"></span><span>save</span>
		</a> 			
		<a title="Cancel" class="element" href="/todos"> <span
			class="icon-undo on-left"></span><span>cancel</span>
		</a>
		
		<c:if test="${not empty todo.id}">
			<form id="deleteForm" method="POST">
			    <input type="hidden" name="action" value="delete" />
			</form>    
			<a title="Delete" class="element" href="#" id="delete"> <span
				class="icon-cancel-2 on-left"></span><span>delete</span>
			</a> 
		</c:if>
    </jsp:attribute>

    <jsp:attribute  name="mainBody"> 
      	<h1>
			<a href="list.html"><i class="icon-clipboard fg-darker smaller"></i>
			</a>to-do<small class="on-right">item</small>
		</h1>

		<form:form modelAttribute="todo" class="span10" id="todoForm">
		    <input type="hidden" name="action" value="save" />
            <form:input type="hidden" path="id" />
            <form:input type="hidden" path="version" />
              
			<label>Name <span class="fg-red" id="nameMessage"></span></label>
			<div class="input-control text" data-role="input-control" id="nameControl">
				<form:input type="text" placeholder="todo name" path="name"></form:input>
				<button class="btn-clear" tabindex="-1" type="button"></button>			
			</div>

			<label>Description</label>
			<div class="input-control textarea" data-role="input-control">
				<form:textarea path="description"></form:textarea>
			</div>

			<label>Priority</label>
			<div class="input-control select">
				<form:select path="priority">
				    <form:option value="">  </form:option>
					<form:option value="High">High</form:option>
					<form:option value="Normal">Normal</form:option>
					<form:option value="Low">Low</form:option>
				</form:select>
			</div>

			<label class="nbm">Done</label>
			<div class="input-control checkbox" data-role="input-control">
				<label> <form:checkbox path="completed"/> <span class="check"></span>
				</label>
			</div>
		</form:form>            
    </jsp:attribute>
    
</t:todosapp-layout>