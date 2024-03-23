package app.pages;

import app.user.User;
import lombok.Getter;
import lombok.Setter;

public class ChangePage implements Command {
    @Getter
    @Setter
    private Page page;
    @Getter
    @Setter
    private NavigationHistory history;

    public ChangePage(final Page page, final NavigationHistory history) {
        this.page = page;
        this.history = history;
    }

    @Override
    public void execute(final User user) {
        history.addPageToHistory(page);
    }
}
