package info.stasha.testosterone.junit4.integration.app.task;

import info.stasha.testosterone.junit4.integration.app.Validation;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;

/**
 *
 * @author stasha
 */
public class Task {

    @PathParam("id")
    @NotNull(groups = {Validation.Read.class, Validation.Update.class, Validation.Delete.class})
    private Long id;

//    @FormParam("title")
    @Size(min = 3, max = 20, groups = {Validation.Create.class, Validation.Update.class})
    private String title;

//    @FormParam("description")
    @Size(min = 3, max = 20, groups = {Validation.Create.class, Validation.Update.class})
    private String description;

//    @FormParam("done")
    private Boolean done;

    public Task() {
    }

    public Task(Long id) {
        this.id = id;
    }

    public Task(String title, String description, Boolean done) {
        this.title = title;
        this.description = description;
        this.done = done;
    }

    public Long getId() {
        return id;
    }

    public Task setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Task setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Task setDescription(String description) {
        this.description = description;
        return this;
    }

    public Boolean getDone() {
        return done;
    }

    public Task setDone(Boolean done) {
        this.done = done;
        return this;
    }

}
