package app.user;

import java.util.*;

import app.audio.Collections.Album;
import app.audio.Collections.AlbumOutput;
import app.audio.Files.Song;
import app.pages.ArtistPage;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Artist.
 */
public final class Artist extends ContentCreator implements Subject {
    private ArrayList<Album> albums;
    private ArrayList<Merchandise> merch;
    private ArrayList<Event> events;
    @Getter
    @Setter
    private HashMap<String, Integer> topAlbums = new HashMap<>();
    @Getter
    @Setter
    private HashMap<String, Double> profitForEachSong = new HashMap<>();
    @Getter
    @Setter
    private Double totalRevenue = 0.0;
    @Getter
    @Setter
    private HashMap<String, Integer> topSongs = new HashMap<>();
    @Getter
    @Setter
    private HashMap<String, Integer> topFans = new HashMap<>();
    @Getter
    @Setter
    private Integer listeners = 0;
    @Getter
    @Setter
    private Double songRevenue = 0.0;
    @Getter
    @Setter
    private Double merchRevenue = 0.0;
    @Getter
    @Setter
    private Integer ranking = 1;
    @Getter
    @Setter
    private String mostProfitableSong = "N/A";
    @Getter
    @Setter
    private boolean listened = false;
    @Getter
    @Setter
    private ArrayList<Observer> observers = new ArrayList<>();
    @Getter
    @Setter
    private boolean hasStatistics = false;
    @Getter
    @Setter
    private static final Integer MAX = 5;


    /**
     * Instantiates a new Artist.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public Artist(final String username, final int age, final String city) {
        super(username, age, city);
        albums = new ArrayList<>();
        merch = new ArrayList<>();
        events = new ArrayList<>();

        super.setPage(new ArtistPage(this));
        ArtistPage artistPage = (ArtistPage) super.getPage();
        artistPage.setOwner(username);
    }

    /**
     * Adds an observer to the list of observers.
     *
     * @param observer The observer to be added.
     */
    public void addObserver(final Observer observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from the list of observers.
     *
     * @param observer The observer to be removed.
     */
    public void removeObserver(final Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all registered observers with the specified key and value.
     *
     * @param key   The key associated with the update.
     * @param value The value associated with the update.
     */
    public void notifyObservers(final String key, final String value) {
        for (Observer observer : observers) {
            observer.update(key, value);
        }
    }

    /**
     * Gets albums.
     *
     * @return the albums
     */
    public ArrayList<Album> getAlbums() {
        return albums;
    }

    /**
     * Gets merch.
     *
     * @return the merch
     */
    public ArrayList<Merchandise> getMerch() {
        return merch;
    }

    /**
     * Gets events.
     *
     * @return the events
     */
    public ArrayList<Event> getEvents() {
        return events;
    }

    /**
     * Gets event.
     *
     * @param eventName the event name
     * @return the event
     */
    public Event getEvent(final String eventName) {
        for (Event event : events) {
            if (event.getName().equals(eventName)) {
                return event;
            }
        }

        return null;
    }

    /**
     * Gets album.
     *
     * @param albumName the album name
     * @return the album
     */
    public Album getAlbum(final String albumName) {
        for (Album album : albums) {
            if (album.getName().equals(albumName)) {
                return album;
            }
        }

        return null;
    }

    /**
     * Gets all songs.
     *
     * @return the all songs
     */
    public List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>();
        albums.forEach(album -> songs.addAll(album.getSongs()));

        return songs;
    }

    /**
     * Show albums array list.
     *
     * @return the array list
     */
    public ArrayList<AlbumOutput> showAlbums() {
        ArrayList<AlbumOutput> albumOutput = new ArrayList<>();
        for (Album album : albums) {
            albumOutput.add(new AlbumOutput(album));
        }

        return albumOutput;
    }

    /**
     * Get user type
     *
     * @return user type string
     */
    public String userType() {
        return "artist";
    }

    /**
     * Sorts the entries of the input HashMap by values in descending order and then by keys in
     * ascending order.
     *
     * @param oldMap The input HashMap to be sorted.
     * @return A new HashMap containing the sorted entries.
     */
    public HashMap<String, Integer> keepHashesSorted(final HashMap<String, Integer> oldMap) {
        // sort all 5 hashes that a user have by value, and after by key
        HashMap<String, Integer> hashMap = new HashMap<>(oldMap);

        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(hashMap.entrySet());

        Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(final Map.Entry<String, Integer> o1,
                               final Map.Entry<String, Integer> o2) {
                if (o1.getValue().equals(o2.getValue())) {
                    return o1.getKey().compareTo(o2.getKey());
                }
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        Map<String, Integer> sortedHashMap = new LinkedHashMap<>();

        for (Map.Entry<String, Integer> entry : entryList) {
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }

        return (HashMap<String, Integer>) sortedHashMap;
    }

    /**
     * Retrieves the top songs along with their corresponding play counts, limited to the maximum
     * of 5 entries.
     *
     * @return A new HashMap containing the top songs and their play counts, sorted by play counts
     * in descending order.
     */
    public HashMap<String, Integer> getTopSongsM() {
        HashMap<String, Integer> result = new HashMap<>();
        this.topSongs = keepHashesSorted(this.topSongs);
        Integer max = Math.min(this.topSongs.size(), MAX);
        for (int i = 0; i < max; i++) {
            result.put(this.topSongs.keySet().toArray()[i].toString(),
                    (Integer) this.topSongs.values().toArray()[i]);
        }
        result = keepHashesSorted(result);
        return result;
    }

    /**
     * Retrieves a list of the top fans, limited to a maximum of 5 entries.
     *
     * @return An ArrayList containing the usernames of the top fans, sorted by their engagement
     * in descending order.
     */
    public ArrayList<String> getTopFansM() {
        ArrayList<String> result = new ArrayList<>();
        this.topFans = keepHashesSorted(this.topFans);
        Integer max = Math.min(this.topFans.size(), MAX);

        for (int i = 0; i < max; i++) {
            result.add(this.topFans.keySet().toArray()[i].toString());
        }
        return result;
    }

    /**
     * Retrieves a list of the top albums, limited to a maximum of 5 entries.
     *
     * @return A HashMap containing the names of the top albums and their corresponding
     * engagement, sorted by engagement in descending order.
     */
    public HashMap<String, Integer> getTopAlbumsM() {
        HashMap<String, Integer> result = new HashMap<>();
        this.topAlbums = keepHashesSorted(this.topAlbums);
        Integer max = Math.min(this.topAlbums.size(), MAX);
        for (int i = 0; i < max; i++) {
            result.put(this.topAlbums.keySet().toArray()[i].toString(),
                    (Integer) this.topAlbums.values().toArray()[i]);
        }
        result = keepHashesSorted(result);
        return result;
    }
}
