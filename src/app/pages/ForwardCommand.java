package app.pages;

import app.user.User;
import lombok.Getter;
import lombok.Setter;

public class ForwardCommand implements Command {
    @Getter
    @Setter
    private NavigationHistory history;
    @Override
    public void execute(final User user) {
        history.goForward(user);
    }
}
