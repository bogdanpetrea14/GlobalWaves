package app.pages;

import app.audio.Collections.Album;
import app.user.Artist;
import app.user.Event;
import app.user.Merchandise;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The type Artist page.
 */
public final class ArtistPage implements Page {
    private List<Album> albums;
    @Getter
    private List<Merchandise> merch;
    private List<Event> events;
    @Getter
    @Setter
    private String owner;

    /**
     * Instantiates a new Artist page.
     *
     * @param artist the artist
     */
    public ArtistPage(final Artist artist) {
        albums = artist.getAlbums();
        merch = artist.getMerch();
        events = artist.getEvents();
    }

    @Override
    public String printCurrentPage() {
        return "Albums:\n\t%s\n\nMerch:\n\t%s\n\nEvents:\n\t%s"
                .formatted(albums.stream().map(Album::getName).toList(),
                           merch.stream().map(merchItem -> "%s - %d:\n\t%s"
                                .formatted(merchItem.getName(),
                                           merchItem.getPrice(),
                                           merchItem.getDescription()))
                                .toList(),
                           events.stream().map(event -> "%s - %s:\n\t%s"
                                 .formatted(event.getName(),
                                            event.getDate(),
                                            event.getDescription()))
                                 .toList());
    }

    /**
     * Retrieves the names of the merchandise available for purchase.
     *
     * This method returns a list of strings containing the names of all available merchandise.
     *
     * @return A list of strings representing the names of available merchandise.
     */
    public List<String> getMerch() {
        List<String> merchNames = merch.stream().map(Merchandise::getName).toList();
        return merchNames;
    }

    /**
     * Retrieves the price of a specific merchandise item by its name.
     *
     * This method searches for a merchandise item with the specified name and returns its price.
     * If no matching merchandise item is found, null is returned.
     *
     * @param merchName The name of the merchandise item to retrieve the price for.
     * @return The price of the specified merchandise item, or null if the item is not found.
     */
    public Integer getMerchPrice(final String merchName) {
        for (Merchandise merchItem : merch) {
            if (merchItem.getName().equals(merchName)) {
                return merchItem.getPrice();
            }
        }
        return null;
    }
}
