package app.user;

public interface Subject {
    /**
     * Adds the specified observer to the list of registered observers.
     *
     * @param observer The observer to be added.
     */
    void addObserver(Observer observer);

    /**
     * Removes the specified observer from the list of registered observers.
     *
     * @param observer The observer to be removed.
     */
    void removeObserver(Observer observer);

    /**
     * Notifies all registered observers with new information.
     *
     * @param key   The key associated with the update.
     * @param value The new value to be communicated to the observers.
     */
    void notifyObservers(String key, String value);
}
