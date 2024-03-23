package app.pages;

import app.user.User;
import lombok.Getter;
import lombok.Setter;

public class BackCommand implements Command {
    @Setter
    @Getter
    private NavigationHistory history;
    @Override
    public void execute(final User user) {
        history.goBack(user);
    }
}
