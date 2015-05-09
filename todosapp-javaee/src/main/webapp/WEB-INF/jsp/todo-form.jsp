<!-- item.jsp -->
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<t:todosapp-layout title="to-do item"
                   pageScript="/webjars/todosapp/static/js/todo-form.js">
 
    <jsp:attribute  name="navigationBarActions">           
		<a title="Save" class="element" href="#" id="save"> <span
			class="icon-checkmark on-left"></span><span>save</span>
		</a> 			
		<a title="Cancel" class="element" href="/todos"> <span
			class="icon-undo on-left"></span><span>cancel</span>
		</a>
		
		<c:if test="${!empty todo}">
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

		<form class="span10" id="todoForm" method="POST">
		    <input type="hidden" name="action"  value="save" />
            <input type="hidden" name="version" value="${todo.version}"/>
                       
			<label>Name <span class="fg-red" id="nameMessage"></span></label>
			<div class="input-control text" data-role="input-control" id="nameControl">
				<input type="text" placeholder="todo name" id="name" name="name" value="${todo.name}"></input>
				<button class="btn-clear" tabindex="-1" type="button"></button>			
			</div>

			<label>Description</label>
			<div class="input-control textarea" data-role="input-control">
				<textarea name="description">${todo.description}</textarea>
			</div>

			<label>Priority</label>
			<div class="input-control select">
				<select name="priority">
				    <option value="">  </option>
					<option value="High"   ${todo.priority == 'High' ? 'selected':''}>High</option>
					<option value="Normal" ${todo.priority == 'Normal' ? 'selected':''}>Normal</option>
					<option value="Low"    ${todo.priority == 'Low' ? 'selected':''}>Low</option>
				</select>
			</div>

			<label class="nbm">Done</label>
			<div class="input-control checkbox" data-role="input-control">
				<label> <input type="checkbox" name="completed" ${todo.completed? 'checked' : ''}/> <span class="check"></span>
				</label>
			</div>

		</form>
                
        <c:if test="${not empty errorText}">
            <script type="text/html" id="errorContent">        
                <label> ${errorText} </label>
                <div class="form-actions place-right padding25">
                    <button class="button primary" onclick="location.reload()">Refresh</button>
                    <button class="button" type="button" onclick="$.Dialog.close()">Cancel</button>
                </div>        
            </script>
        </c:if>        
          
    </jsp:attribute>
    
</t:todosapp-layout>