package info.stasha.testosterone.jersey.junit4.integration.app.task;

import info.stasha.testosterone.jersey.junit4.integration.app.user.User;
import javax.ws.rs.PathParam;

/**
 *
 * @author stasha
 */
public class Task {

    @PathParam("id")
    private Long id;
    private String title;
    private String description;
    private Boolean done;

    private User user;

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

    public Task(String title, String description, Boolean done, User user) {
        this.title = title;
        this.description = description;
        this.done = done;
        this.user = user;
    }

    public Task(Long id, String title, String description, Boolean done) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.done = done;
    }

    public Task(Long id, String title, String description, Boolean done, User user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.done = done;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public Task setUser(User user) {
        this.user = user;
        return this;
    }

}
