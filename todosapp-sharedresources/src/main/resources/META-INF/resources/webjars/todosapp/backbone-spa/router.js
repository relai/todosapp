var todosapp = todosapp || {};

todosapp.TodoRouter = Backbone.Router.extend({
	routes: {
		'':         function() { this.list('all'); },
		'all':      function() { this.list('all'); },
		'open':     function() { this.list('open'); },
		'done':     function() { this.list('done'); },
        'create':   'create',		
		'item/:id': 'edit'
	},
	
	list: function(filter) {				
        this.view.showTodoTable(filter);
	},
	
	create: function() {
        this.view.showTodoForm(null);
	},
	
	edit: function(id) {
		this.view.showTodoForm(id);
	},
		
	initialize: function() {
		this.view = new todosapp.view.AppView();
	}
});