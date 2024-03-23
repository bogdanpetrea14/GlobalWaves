package app.user;

import app.Admin;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Playlist;
import app.audio.Collections.PlaylistOutput;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.pages.HomePage;
import app.pages.LikedContentPage;
import app.pages.NavigationHistory;
import app.pages.Page;
import app.player.Player;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.utils.Enums;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * The type User.
 */
public final class User extends UserAbstract implements Observer {
    @Getter
    private ArrayList<Playlist> playlists;
    @Getter
    private ArrayList<Song> likedSongs;
    @Getter
    private ArrayList<Playlist> followedPlaylists;
    @Getter
    private final Player player;
    @Getter
    private boolean status;
    @Getter
    private final SearchBar searchBar;
    private boolean lastSearched;
    @Getter
    @Setter
    private Page currentPage;
    @Getter
    @Setter
    private HomePage homePage;
    @Getter
    @Setter
    private LikedContentPage likedContentPage;
    @Getter
    @Setter
    private HashMap<String, Integer> topArtists;
    @Getter
    @Setter
    private HashMap<String, Integer> topGenres;
    @Setter
    @Getter
    private HashMap<String, Integer> topSongs;
    @Getter
    @Setter
    private HashMap<String, Integer> topAlbums;
    @Getter
    @Setter
    private HashMap<String, Integer> topEpisodes;
    @Getter
    @Setter
    private boolean hasStatistics = false;
    @Getter
    @Setter
    private boolean premium = false;
    @Getter
    @Setter
    private HashMap<String, Integer> premiumHistorySong = new HashMap<>();
    @Getter
    @Setter
    private HashMap<String, Integer> premiumHistoryArtist = new HashMap<>();
    @Getter
    @Setter
    private static final Double subscriptionPrice = 1000000.0;
    @Getter
    private AdAdministration adAdministration = new AdAdministration();
    @Getter
    @Setter
    private Integer adsNumber = 0;
    @Getter
    @Setter
    private ArrayList<String> merch = new ArrayList<>();
    @Getter
    @Setter
    private Song songRecommendations;
    @Getter
    @Setter
    private Playlist playlistRecommendations;
    @Getter
    @Setter
    private ArrayList<String> messages = new ArrayList<>();
    @Getter
    @Setter
    private ArrayList<String> description = new ArrayList<>();
    @Getter
    @Setter
    private NavigationHistory history = new NavigationHistory();
    @Getter
    @Setter
    private String lastTypeOfRecommendations = "empty";
    @Getter
    @Setter
    private LibraryEntry lastRecommendation = null;
    @Getter
    @Setter
    private Integer adPrice = 0;
    @Getter
    @Setter
    private static final Integer MAX_ADS = 5;

    public class AdAdministration {
        @Getter
        @Setter
        private HashMap<String, Integer> allSongs = new HashMap<>();
        @Getter
        @Setter
        private HashMap<String, Integer> allArtists = new HashMap<>();

        /**
         * Adds a song to the collection, incrementing its count if it already exists, or adding
         * it with a count of 1 if it's a new song.
         *
         * @param songName The name of the song to be added.
         */
        public void addSong(final String songName) {
            if (allSongs.containsKey(songName)) {
                allSongs.put(songName, allSongs.get(songName) + 1);
            } else {
                allSongs.put(songName, 1);
            }
        }

        /**
         * Adds an artist to the collection, incrementing its count if it already exists, or
         * adding it with a count of 1 if it's a new artist.
         *
         * @param artistName The name of the artist to be added.
         */
        public void addArtist(final String artistName) {
            if (allArtists.containsKey(artistName)) {
                allArtists.put(artistName, allArtists.get(artistName) + 1);
            } else {
                allArtists.put(artistName, 1);
            }
        }

        /**
         * Clears all songs and artists from the collection, resetting the counters.
         */
        public void clearLists() {
            allSongs.clear();
            allArtists.clear();
        }
    }


    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public User(final String username, final int age, final String city) {
        super(username, age, city);
        playlists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player();
        searchBar = new SearchBar(username);
        lastSearched = false;
        status = true;

        homePage = new HomePage(this);
        currentPage = homePage;
        likedContentPage = new LikedContentPage(this);
        topArtists = new HashMap<>();
        topGenres = new HashMap<>();
        topSongs = new HashMap<>();
        topAlbums = new HashMap<>();
        topEpisodes = new HashMap<>();
    }

    /**
     * Updates the observer with new information and adds a notification message.
     *
     * @param key   The key indicating the type of update.
     * @param value The value associated with the update.
     */
    public void update(final String key, final String value) {
        this.getMessages().add(key);
        this.getDescription().add(value);
    }

    /**
     * Clears the list of notifications, removing all messages and descriptions.
     */
    public void clearNotifications() {
        this.getMessages().clear();
        this.getDescription().clear();
    }

    @Override
    public String userType() {
        return "user";
    }

    /**
     * Search array list.
     *
     * @param filters the filters
     * @param type    the type
     * @return the array list
     */
    public ArrayList<String> search(final Filters filters, final String type) {
        searchBar.clearSelection();
        player.stop();

        lastSearched = true;
        ArrayList<String> results = new ArrayList<>();

        if (type.equals("artist") || type.equals("host")) {
            List<ContentCreator> contentCreatorsEntries =
            searchBar.searchContentCreator(filters, type);

            for (ContentCreator contentCreator : contentCreatorsEntries) {
                results.add(contentCreator.getUsername());
            }
        } else {
            List<LibraryEntry> libraryEntries = searchBar.search(filters, type);

            for (LibraryEntry libraryEntry : libraryEntries) {
                results.add(libraryEntry.getName());
            }
        }
        return results;
    }

    /**
     * Select string.
     *
     * @param itemNumber the item number
     * @return the string
     */
    public String select(final int itemNumber) {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (!lastSearched) {
            return "Please conduct a search before making a selection.";
        }

        lastSearched = false;

        if (searchBar.getLastSearchType().equals("artist")
            || searchBar.getLastSearchType().equals("host")) {
            ContentCreator selected = searchBar.selectContentCreator(itemNumber);

            if (selected == null) {
                return "The selected ID is too high.";
            }

            currentPage = selected.getPage();
            return "Successfully selected %s's page.".formatted(selected.getUsername());
        } else {
            LibraryEntry selected = searchBar.select(itemNumber);

            if (selected == null) {
                return "The selected ID is too high.";
            }

            return "Successfully selected %s.".formatted(selected.getName());
        }
    }

    /**
     * Load string.
     *
     * @return the string
     */
    public String load() {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (searchBar.getLastSelected() == null) {
            return "Please select a source before attempting to load.";
        }

        if (!searchBar.getLastSearchType().equals("song")
            && ((AudioCollection) searchBar.getLastSelected()).getNumberOfTracks() == 0) {
            return "You can't load an empty audio collection!";
        }
        player.setComeBack(false);
        player.setShouldInsertAd(false);
        player.setBeforeAdSource(null);
        adPrice = 0;

        player.setSource(searchBar.getLastSelected(), searchBar.getLastSearchType());
        searchBar.clearSelection();

        player.pause();
        Admin.getInstance().updateWrappedIndividually(this);
        return "Playback loaded successfully.";
    }

    /**
     * Play pause string.
     *
     * @return the string
     */
    public String playPause() {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to pause or resume playback.";
        }

        player.pause();

        if (player.getPaused()) {
            return "Playback paused successfully.";
        } else {
            return "Playback resumed successfully.";
        }
    }

    /**
     * Repeat string.
     *
     * @return the string
     */
    public String repeat() {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before setting the repeat status.";
        }

        Enums.RepeatMode repeatMode = player.repeat();
        String repeatStatus = "";

        switch (repeatMode) {
            case NO_REPEAT -> {
                repeatStatus = "no repeat";
            }
            case REPEAT_ONCE -> {
                repeatStatus = "repeat once";
            }
            case REPEAT_ALL -> {
                repeatStatus = "repeat all";
            }
            case REPEAT_INFINITE -> {
                repeatStatus = "repeat infinite";
            }
            case REPEAT_CURRENT_SONG -> {
                repeatStatus = "repeat current song";
            }
            default -> {
                repeatStatus = "";
            }
        }

        return "Repeat mode changed to %s.".formatted(repeatStatus);
    }

    /**
     * Shuffle string.
     *
     * @param seed the seed
     * @return the string
     */
    public String shuffle(final Integer seed) {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before using the shuffle function.";
        }

        if (!player.getType().equals("playlist")
            && !player.getType().equals("album")) {
            return "The loaded source is not a playlist or an album.";
        }

        player.shuffle(seed);

        if (player.getShuffle()) {
            return "Shuffle function activated successfully.";
        }
        return "Shuffle function deactivated successfully.";
    }

    /**
     * Forward string.
     *
     * @return the string
     */
    public String forward() {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to forward.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipNext();

        return "Skipped forward successfully.";
    }

    /**
     * Backward string.
     *
     * @return the string
     */
    public String backward() {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please select a source before rewinding.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipPrev();

        return "Rewound successfully.";
    }

    /**
     * Like string.
     *
     * @return the string
     */
    public String like() {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before liking or unliking.";
        }

        if (!player.getType().equals("song") && !player.getType().equals("playlist")
            && !player.getType().equals("album")) {
            return "Loaded source is not a song.";
        }

        Song song = (Song) player.getCurrentAudioFile();

        if (likedSongs.contains(song)) {
            likedSongs.remove(song);
            song.dislike();

            return "Unlike registered successfully.";
        }

        likedSongs.add(song);
        song.like();
        return "Like registered successfully.";
    }

    /**
     * Next string.
     *
     * @return the string
     */
    public String next() {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        player.next(this);

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        return "Skipped to next track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Prev string.
     *
     * @return the string
     */
    public String prev() {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before returning to the previous track.";
        }

        player.prev();

        return "Returned to previous track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Create playlist string.
     *
     * @param name      the name
     * @param timestamp the timestamp
     * @return the string
     */
    public String createPlaylist(final String name, final int timestamp) {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (playlists.stream().anyMatch(playlist -> playlist.getName().equals(name))) {
            return "A playlist with the same name already exists.";
        }

        playlists.add(new Playlist(name, getUsername(), timestamp));

        return "Playlist created successfully.";
    }

    /**
     * Add remove in playlist string.
     *
     * @param id the id
     * @return the string
     */
    public String addRemoveInPlaylist(final int id) {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before adding to or removing from the playlist.";
        }

        if (player.getType().equals("podcast")) {
            return "The loaded source is not a song.";
        }

        if (id > playlists.size()) {
            return "The specified playlist does not exist.";
        }

        Playlist playlist = playlists.get(id - 1);

        if (playlist.containsSong((Song) player.getCurrentAudioFile())) {
            playlist.removeSong((Song) player.getCurrentAudioFile());
            return "Successfully removed from playlist.";
        }

        playlist.addSong((Song) player.getCurrentAudioFile());
        return "Successfully added to playlist.";
    }

    /**
     * Switch playlist visibility string.
     *
     * @param playlistId the playlist id
     * @return the string
     */
    public String switchPlaylistVisibility(final Integer playlistId) {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (playlistId > playlists.size()) {
            return "The specified playlist ID is too high.";
        }

        Playlist playlist = playlists.get(playlistId - 1);
        playlist.switchVisibility();

        if (playlist.getVisibility() == Enums.Visibility.PUBLIC) {
            return "Visibility status updated successfully to public.";
        }

        return "Visibility status updated successfully to private.";
    }

    /**
     * Show playlists array list.
     *
     * @return the array list
     */
    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> playlistOutputs = new ArrayList<>();
        for (Playlist playlist : playlists) {
            playlistOutputs.add(new PlaylistOutput(playlist));
        }

        return playlistOutputs;
    }

    /**
     * Follow string.
     *
     * @return the string
     */
    public String follow() {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        LibraryEntry selection = searchBar.getLastSelected();
        String type = searchBar.getLastSearchType();

        if (selection == null) {
            return "Please select a source before following or unfollowing.";
        }

        if (!type.equals("playlist")) {
            return "The selected source is not a playlist.";
        }

        Playlist playlist = (Playlist) selection;

        if (playlist.getOwner().equals(getUsername())) {
            return "You cannot follow or unfollow your own playlist.";
        }

        if (followedPlaylists.contains(playlist)) {
            followedPlaylists.remove(playlist);
            playlist.decreaseFollowers();

            return "Playlist unfollowed successfully.";
        }

        followedPlaylists.add(playlist);
        playlist.increaseFollowers();


        return "Playlist followed successfully.";
    }

    /**
     * Gets player stats.
     *
     * @return the player stats
     */
    public PlayerStats getPlayerStats() {
        return player.getStats();
    }

    /**
     * Show preferred songs array list.
     *
     * @return the array list
     */
    public ArrayList<String> showPreferredSongs() {
        ArrayList<String> results = new ArrayList<>();
        for (AudioFile audioFile : likedSongs) {
            results.add(audioFile.getName());
        }

        return results;
    }

    /**
     * Gets preferred genre.
     *
     * @return the preferred genre
     */
    public String getPreferredGenre() {
        String[] genres = {"pop", "rock", "rap"};
        int[] counts = new int[genres.length];
        int mostLikedIndex = -1;
        int mostLikedCount = 0;

        for (Song song : likedSongs) {
            for (int i = 0; i < genres.length; i++) {
                if (song.getGenre().equals(genres[i])) {
                    counts[i]++;
                    if (counts[i] > mostLikedCount) {
                        mostLikedCount = counts[i];
                        mostLikedIndex = i;
                    }
                    break;
                }
            }
        }

        String preferredGenre = mostLikedIndex != -1 ? genres[mostLikedIndex] : "unknown";
        return "This user's preferred genre is %s.".formatted(preferredGenre);
    }

    /**
     * Switch status.
     */
    public void switchStatus() {
        status = !status;
    }

    /**
     * Simulate time.
     *
     * @param time the time
     */
    public void simulateTime(final int time, final User user) {
        if (!status) {
            return;
        }

        player.simulatePlayer(time, user);
    }

    /**
     * Sorts the provided hash map by values in descending order and then by keys in ascending
     * order.
     *
     * @param oldMap The hash map to be sorted.
     * @return A new hash map sorted by values in descending order and then by keys in ascending
     * order.
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
     * Retrieves the top songs and their play counts, limited to a maximum of 5 entries,
     * in a sorted order.
     *
     * @return A new hash map containing the top songs and their play counts, sorted by play
     * counts in descending order.
     */
    public HashMap<String, Integer> getTopSongsM() {
        HashMap<String, Integer> result = new HashMap<>();
        Integer max = Math.min(this.topSongs.size(), MAX_ADS);
        for (int i = 0; i < max; i++) {
            result.put(this.topSongs.keySet().toArray()[i].toString(),
                    (Integer) this.topSongs.values().toArray()[i]);
        }
        result = keepHashesSorted(result);
        return result;
    }

    /**
     * Retrieves the top artists and their play counts, limited to a maximum of 5 entries,
     * in a sorted order.
     *
     * @return A new hash map containing the top artists and their play counts, sorted by play
     * counts in descending order.
     */
    public HashMap<String, Integer> getTopArtistsM() {
        HashMap<String, Integer> result = new HashMap<>();
        Integer max = Math.min(this.topArtists.size(), MAX_ADS);
        for (int i = 0; i < max; i++) {
            result.put(this.topArtists.keySet().toArray()[i].toString(),
                    (Integer) this.topArtists.values().toArray()[i]);
        }
        result = keepHashesSorted(result);
        return result;
    }

    /**
     * Retrieves the top genres and their play counts, limited to a maximum of 5 entries,
     * in a sorted order.
     *
     * @return A new hash map containing the top genres and their play counts, sorted by play
     * counts in descending order.
     */
    public HashMap<String, Integer> getTopGenresM() {
        HashMap<String, Integer> result = new HashMap<>();
        Integer max = Math.min(this.topGenres.size(), MAX_ADS);
        for (int i = 0; i < max; i++) {
            result.put(this.topGenres.keySet().toArray()[i].toString(),
                    (Integer) this.topGenres.values().toArray()[i]);
        }
        result = keepHashesSorted(result);
        return result;
    }

    /**
     * Retrieves the top albums and their play counts, limited to a maximum of 5 entries,
     * in a sorted order.
     *
     * @return A new hash map containing the top albums and their play counts, sorted by play
     * counts in descending order.
     */
    public HashMap<String, Integer> getTopAlbumsM() {
        HashMap<String, Integer> result = new HashMap<>();
        Integer max = Math.min(this.topAlbums.size(), MAX_ADS);
        for (int i = 0; i < max; i++) {
            result.put(this.topAlbums.keySet().toArray()[i].toString(),
                    (Integer) this.topAlbums.values().toArray()[i]);
        }
        result = keepHashesSorted(result);
        return result;
    }

    /**
     * Retrieves the top episodes and their play counts, limited to a maximum of 5 entries,
     * in a sorted order.
     *
     * @return A new hash map containing the top episodes and their play counts, sorted by play
     * counts in descending order.
     */
    public HashMap<String, Integer> getTopEpisodesM() {
        HashMap<String, Integer> result = new HashMap<>();
        Integer max = Math.min(this.topEpisodes.size(), MAX_ADS);
        for (int i = 0; i < max; i++) {
            result.put(this.topEpisodes.keySet().toArray()[i].toString(),
                    (Integer) this.topEpisodes.values().toArray()[i]);
        }
        result = keepHashesSorted(result);
        return result;
    }

    /**
     * Retrieves a list of songs based on the specified genre.
     *
     * @param genre The genre to filter songs by.
     * @return An ArrayList of Song objects matching the specified genre.
     */
    public ArrayList<Song> getSongsByGenre(final String genre) {
        ArrayList<Song> result = new ArrayList<>();
        for (Song song : Admin.getInstance().getSongs()) {
            if (song.getGenre().equals(genre)) {
                result.add(song);
            }
        }
        return result;
    }

    /**
     * Updates the currently playing song based on the genre of the current song.
     * If the remaining duration of the current song is less than 30 seconds, no update is
     * performed.
     *
     * @return The selected Song object to be played next, or null if no update is required.
     */
    public Song updateSongs() {
        Song song = (Song) this.getPlayer().getSource().getAudioFile();
        if (song == null) {
            return null;
        }
        Integer seed = this.getPlayer().getSource().getAudioFile().getDuration()
                - this.getPlayer().getSource().getRemainedDuration();
        if (seed < 30) {
            return null;
        }
        ArrayList<Song> songs = getSongsByGenre(song.getGenre());
        // choose a song from songs using seed
        Random random = new Random(seed);
        Integer index = random.nextInt(songs.size());
        Song newSong = songs.get(index);

        return newSong;
    }

    /**
     * Updates the user's recommendations playlist based on liked songs and followed playlists.
     * The playlist includes top genres and a selection of songs from those genres.
     *
     * @return The updated recommendations playlist.
     */
    public Playlist updatePlaylists() {
        // Create a new playlist for recommendations
        Playlist recommendationsPlaylist = new Playlist(
                String.format("%s's recommendations", this.getUsername()), this.getUsername());

        // Count the occurrences of each genre in liked songs, followed playlists
        // and user playlists
        HashMap<String, Integer> topGenres = countTopGenres();

        // Sort genres based on occurrence count
        topGenres = keepHashesSorted(topGenres);

        // Limit the number of top genres to consider
        int maxGenres = Math.min(topGenres.size(), 3);

        // Populate the recommendations playlist with songs from top genres
        addSongsFromTopGenres(recommendationsPlaylist, topGenres, maxGenres);

        return recommendationsPlaylist;
    }

    /**
     * Counts the occurrences of each genre in liked songs, followed playlists, and user playlists.
     *
     * @return A HashMap containing genres and their occurrence counts.
     */
    private HashMap<String, Integer> countTopGenres() {
        HashMap<String, Integer> topGenres = new HashMap<>();

        // Count genres in liked songs
        countGenresInSongs(this.getLikedSongs(), topGenres);

        // Count genres in followed playlists
        for (Playlist playlist : this.getFollowedPlaylists()) {
            countGenresInSongs(playlist.getSongs(), topGenres);
        }

        // Count genres in user playlists
        for (Playlist playlist : this.getPlaylists()) {
            countGenresInSongs(playlist.getSongs(), topGenres);
        }

        return topGenres;
    }

    /**
     * Counts the occurrences of each genre in a list of songs and updates the genre count HashMap.
     *
     * @param songs     The list of songs to analyze.
     * @param topGenres The HashMap to update with genre occurrences.
     */
    private void countGenresInSongs(final List<Song> songs,
                                    final HashMap<String, Integer> topGenres) {
        for (Song song : songs) {
            String genre = song.getGenre();
            topGenres.put(genre, topGenres.getOrDefault(genre, 0) + 1);
        }
    }

    /**
     * Adds songs from the top genres to the recommendations playlist.
     *
     * @param recommendationsPlaylist The playlist to update with recommended songs.
     * @param topGenres              The sorted top genres HashMap.
     * @param maxGenres              The maximum number of top genres to consider.
     */
    private void addSongsFromTopGenres(final Playlist recommendationsPlaylist,
                                       final HashMap<String, Integer> topGenres,
                                       final int maxGenres) {
        int maxSongsPerGenre = MAX_ADS; // Maximum songs to include per genre
        int addedSongs = 0;

        for (Map.Entry<String, Integer> entry : topGenres.entrySet()) {
            if (addedSongs >= maxGenres * maxSongsPerGenre) {
                break;
            }

            List<Song> songs = getSongsByGenre(entry.getKey());
            int maxSongs = Math.min(songs.size(), maxSongsPerGenre);

            for (int j = 0; j < maxSongs; j++) {
                recommendationsPlaylist.addSong(songs.get(j));
                addedSongs++;
            }
        }
    }


    /**
     * Updates the Fan Club recommendations playlist based on the top fans' liked songs.
     *
     * @return The updated Fan Club recommendations playlist.
     */
    public Playlist updateFanPlaylists() {
        // Check if the player and its source are available
        if (player == null || player.getSource() == null
                || player.getSource().getAudioFile() == null) {
            return null;
        }

        // Get the current playing song and its artist
        Song song = (Song) this.getPlayer().getSource().getAudioFile();
        Artist artist = Admin.getInstance().getArtist(song.getArtist());

        // Retrieve the top fans for the artist
        HashMap<String, Integer> fans = artist.getTopFans();

        // Sort the fans based on their activity
        fans = keepHashesSorted(fans);

        // Create a playlist for Fan Club recommendations
        Playlist playlist = new Playlist(
                String.format("%s Fan Club recommendations", artist.getUsername()),
                artist.getUsername());

        // Limit the number of fans to consider
        int maxFans = Math.min(fans.size(), MAX_ADS);

        // Populate the playlist with songs from the top fans
        addSongsFromTopFans(playlist, fans, maxFans);

        return playlist;
    }

    /**
     * Adds songs from the liked songs of the top fans to the Fan Club recommendations playlist.
     *
     * @param playlist The Fan Club recommendations playlist to update.
     * @param fans     The sorted top fans HashMap.
     * @param maxFans  The maximum number of top fans to consider.
     */
    private void addSongsFromTopFans(final Playlist playlist,
                                     final HashMap<String, Integer> fans, final
                                     int maxFans) {
        int maxSongsPerFan = MAX_ADS; // Maximum songs to include per fan
        int addedSongs = 0;

        for (Map.Entry<String, Integer> entry : fans.entrySet()) {
            if (addedSongs >= maxFans * maxSongsPerFan) {
                break;
            }

            // Get the user associated with the fan
            User user = Admin.getInstance().getUser(entry.getKey());

            // Retrieve liked songs of the user
            ArrayList<Song> songs = user.getLikedSongs();

            // Limit the number of songs to consider per fan
            int maxSongs = Math.min(songs.size(), maxSongsPerFan);

            // Add songs to the Fan Club recommendations playlist
            for (int j = 0; j < maxSongs; j++) {
                playlist.addSong(songs.get(j));
                addedSongs++;
            }
        }
    }

    /**
     * Checks for new recommendations based on the specified type.
     *
     * @param type The type of recommendations to check for ("random_song",
     *             "random_playlist", or "fans_playlist").
     * @return True if new recommendations are available, false otherwise.
     */
    public boolean checkForNewRecommendations(final String type) {
        switch (type) {
            case "random_song" -> {
                Song newSong = updateSongs();
                if (newSong == null) {
                    return false;
                }
                if (newSong.equals(getSongRecommendations())) {
                    return false;
                }
            }
            case "random_playlist" -> {
                Playlist playlist = updatePlaylists();
                if (playlist == null || playlist.getSongs().size() == 0) {
                    return false;
                }
                if (playlist.equals(getPlaylistRecommendations())) {
                    return false;
                }
            }
            case "fans_playlist" -> {
                Playlist playlist = updateFanPlaylists();
                if (playlist == null || playlist.getSongs().size() == 0) {
                    return false;
                }
                if (playlist.equals(getPlaylistRecommendations())) {
                    return false;
                }
            }
            default -> System.out.println("Invalid update");
        }
        return true;
    }

    /**
     * Updates recommendations based on the specified type and sets them for the user's home page.
     *
     * @param type The type of recommendations to update ("random_song", "random_playlist"
     *             or "fans_playlist").
     */
    public void updateRecommendations(final String type) {
        switch (type) {
            case "random_song" -> {
                Song newSong = updateSongs();
                HomePage homePage = this.getHomePage();
                homePage.setSongRecommendations(newSong);
                this.setSongRecommendations(newSong);
                this.setLastTypeOfRecommendations("song");
                this.setLastRecommendation(newSong);
            }
            case "random_playlist" -> {
                Playlist playeList =  updatePlaylists();
                this.setPlaylistRecommendations(playeList);
                HomePage homePage = this.getHomePage();
                homePage.setPlaylistRecommendations(playeList);
                this.setLastTypeOfRecommendations("playlist");
                this.setLastRecommendation(playeList);
            }
            case "fans_playlist" -> {
                Playlist playlist = updateFanPlaylists();
                this.setPlaylistRecommendations(playlist);
                HomePage homePage = this.getHomePage();
                homePage.setPlaylistRecommendations(playlist);
                this.setLastTypeOfRecommendations("playlist");
                this.setLastRecommendation(playlist);
            }
            default -> System.out.println("Invalid update");
        }
    }
}
