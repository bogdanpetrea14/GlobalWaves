package app.user;

public interface Observer {
    /**
     * Updates the observer with new information.
     *
     * @param key   The key associated with the update.
     * @param value The new value to be communicated to the observer.
     */
    void update(String key, String value);
}
