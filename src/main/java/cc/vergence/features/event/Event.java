package cc.vergence.features.event;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Event {
    private Stage stage;
    private boolean cancel;

    public Event() {
        this.cancel = false;
    }

    public Event(Stage stage) {
        this.cancel = false;
        this.stage = stage;
    }

    public void cancel() {
        this.cancel = true;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public boolean isPost() {
        return stage == Stage.Post;
    }

    public boolean isPre() {
        return stage == Stage.Pre;
    }

    public enum Stage {
        Pre, Post
    }
}
