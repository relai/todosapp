package sample.todosapp.vertx.domain;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;



/**
 * The entity class for an to-do item.
 * 
 * @author Re Lai
 */

@Entity
@Table(name = "Todos")
//@NamedQuery(name = "Todo.findAll", query = "SELECT t FROM Todo t")
public class Todo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue Long id;    
    @Version int version;
    @NotNull @Size(min=1) String name;
    String priority;
    String description;
    boolean completed;
     
    public Todo() {  }

    public Long getId() {return id;}
    
    public int getVersion() {return version;}
    public void putVersion(int version) {this.version = version;}
    
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = name;}

    public String getPriority() {return priority;}
    public void setPriority(String priority) {this.priority = priority;}
    
    public boolean getCompleted() {return completed;}
    public void setCompleted(boolean completed) {this.completed = completed;}
}
