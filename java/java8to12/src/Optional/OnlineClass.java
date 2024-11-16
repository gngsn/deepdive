package Optional;

import javax.swing.text.html.Option;
import java.util.Optional;

public class OnlineClass {
    private int id;
    private String title;
    private boolean closed;
    private Progress progress;

    public OnlineClass(int id, String title, boolean closed) {
        this.id = id;
        this.title = title;
        this.closed = closed;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isClosed() {
        return closed;
    }

    public Optional<Progress> getProgress() {
        return Optional.ofNullable(progress);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public void setProgress(Optional<Progress> progress) {
        progress.ifPresent(p -> this.progress = p);
//        this.progress = progress;
    }
}
