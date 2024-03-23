package app.pages;

import app.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
public class NavigationHistory {
    @Getter
    @Setter
    private List<Page> history = new ArrayList<>();
    @Getter
    @Setter
    private int currentIndex = -1;

    /**
     * Adds a page to the user's browsing history and updates the current index.
     *
     * @param page The page to be added to the browsing history.
     */
    public void addPageToHistory(final Page page) {
        this.getHistory().add(page);
        this.setCurrentIndex(this.getHistory().size() - 1);
    }

    /**
     * Navigates the user to the previous page in the browsing history.
     * If the current index is already at the beginning of the history, no action is taken.
     *
     * @param user The user for whom the navigation is performed.
     */
    public void goBack(final User user) {
        if (currentIndex > 0) {
            currentIndex--;
            // change the currentPage of the user
            user.setCurrentPage(history.get(currentIndex));
        }
    }

    /**
     * Navigates the user to the next page in the browsing history.
     * If the current index is already at the end of the history, no action is taken.
     *
     * @param user The user for whom the navigation is performed.
     */
    public void goForward(final User user) {
        if (currentIndex < history.size() - 1) {
            currentIndex++;
            user.setCurrentPage(history.get(currentIndex));
        }
    }

    /**
     * Deletes all pages in the browsing history that come after the current page.
     * If the current index is already at the end of the history, no action is taken.
     */
    public void deletePagesAfterCurrentPage() {
        if (currentIndex < history.size() - 1) {
            history.subList(currentIndex + 1, history.size()).clear();
        }
    }
}
