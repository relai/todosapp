package models;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * The entity class for to-do items. 
 * 
 * The class is named <code>TodoItem</code>  instead of <code>Todo</code> as in 
 * other todosapp examples because of  name conflict with 
 * <code>play.mvc.Results.Todo</code>.
 * 
 * @author Re Lai
 */

@Entity
@Table(name = "TODOS")
@NamedQueries({
    @NamedQuery(name = "Todo.findAll", query = "SELECT t FROM TodoItem t"),
    @NamedQuery(name = "Todo.findByCompleted", 
                query = "SELECT t FROM TodoItem t WHERE t.completed = :done")
})
public class TodoItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue private Long id = 0L;    
    @Version private int version;
    @NotNull @Size(min=1) private String name;
    private String priority;
    private String description;
    private boolean completed;
     
    public TodoItem() {  }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    
    public int getVersion() {return version;}
    public void setVersion(int version) {this.version = version;}
    
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getPriority() {return priority;}
    public void setPriority(String priority) {this.priority = priority;}
    
    public boolean getCompleted() {return completed;}
    public void setCompleted(boolean completed) {this.completed = completed;}
}
