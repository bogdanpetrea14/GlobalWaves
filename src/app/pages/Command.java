package app.pages;

import app.user.User;

public interface Command {
    /**
     * Executes the command, applying specific actions on the given user.
     *
     * @param user The user on whom the command will be executed.
     */
    void execute(User user);
}
