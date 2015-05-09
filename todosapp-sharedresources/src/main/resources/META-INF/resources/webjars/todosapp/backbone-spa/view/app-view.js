var todosapp = todosapp || {};
todosapp.view = todosapp.view || {};


todosapp.view.AppView = Backbone.View.extend({
	el: "body",
	subview: null,
	
	events: {
	  'click #save':   '_save',	
	  'click #delete': '_remove'
	},
	
	initialize: function() {
		$("#filterSet button").click(function(event){
			var type = this.id.substring(0, this.id.length - 5);
			window.location.replace("#"+type);
		});
	},
	
	showTodoTable: function(filter) {
		if (!this.subview || this.subview.tagName !== 'table') {
			if (this.subview) this.subview.remove();
			this.subview = new todosapp.view.TodoTable({filter: filter});
		} else {
			this.subview.setFilter(filter);
		}
	},
	
	showTodoForm: function(id) {
		if (this.subview) this.subview.remove();		
		this.subview = new todosapp.view.TodoForm({modelId: id});							
	},
	
	_save: function(event) {
		if (this.subview) this.subview.save(event);
	},
	
	_remove: function() {
		if (this.subview) this.subview.deleteItem();
	}		
});