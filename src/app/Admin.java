package app;

import app.audio.Collections.Album;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.player.Player;
import app.user.Announcement;
import app.user.Artist;
import app.user.Event;
import app.user.Host;
import app.user.Merchandise;
import app.user.User;
import app.user.UserAbstract;
import fileio.input.CommandInput;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * The type Admin.
 */
public final class Admin {
    @Getter
    private List<User> users = new ArrayList<>();
    @Getter
    private List<Artist> artists = new ArrayList<>();
    @Getter
    private List<Host> hosts = new ArrayList<>();
    private List<Song> songs = new ArrayList<>();
    private List<Podcast> podcasts = new ArrayList<>();
    private int timestamp = 0;
    private final int limit = 5;
    private final int dateStringLength = 10;
    private final int dateFormatSize = 3;
    private final int dateYearLowerLimit = 1900;
    private final int dateYearHigherLimit = 2023;
    private final int dateMonthLowerLimit = 1;
    private final int dateMonthHigherLimit = 12;
    private final int dateDayLowerLimit = 1;
    private final int dateDayHigherLimit = 31;
    private final int dateFebHigherLimit = 28;
    private static Admin instance;
    @Getter
    @Setter
    private static Integer prvTmp = 0;
    @Getter
    @Setter
    private static Song ad;
    @Getter
    @Setter
    private static int noOfAds = 0;
    @Getter
    @Setter
    private static final double roundNumber = 100.0;

    private Admin() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static Admin getInstance() {
        if (instance == null) {
            instance = new Admin();
        }
        return instance;
    }

    /**
     * Reset instance.
     */
    public static void resetInstance() {
        instance = null;
    }

    /**
     * Sets users.
     *
     * @param userInputList the user input list
     */
    public void setUsers(final List<UserInput> userInputList) {
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(), userInput.getCity()));
        }
    }

    /**
     * Sets songs.
     *
     * @param songInputList the song input list
     */
    public void setSongs(final List<SongInput> songInputList) {
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }

    /**
     * Sets podcasts.
     *
     * @param podcastInputList the podcast input list
     */
    public void setPodcasts(final List<PodcastInput> podcastInputList) {
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(),
                                         episodeInput.getDuration(),
                                         episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    /**
     * Gets songs.
     *
     * @return the songs
     */
    public List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    /**
     * Gets podcasts.
     *
     * @return the podcasts
     */
    public List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }

    /**
     * Gets playlists.
     *
     * @return the playlists
     */
    public List<Playlist> getPlaylists() {
        return users.stream()
                    .flatMap(user -> user.getPlaylists().stream())
                    .collect(Collectors.toList());
    }

    /**
     * Gets albums.
     *
     * @return the albums
     */
    public List<Album> getAlbums() {
        return artists.stream()
                      .flatMap(artist -> artist.getAlbums().stream())
                      .collect(Collectors.toList());
    }

    /**
     * Gets all users.
     *
     * @return the all users
     */
    public List<String> getAllUsers() {
        List<String> allUsers = new ArrayList<>();

        allUsers.addAll(users.stream().map(UserAbstract::getUsername).toList());
        allUsers.addAll(artists.stream().map(UserAbstract::getUsername).toList());
        allUsers.addAll(hosts.stream().map(UserAbstract::getUsername).toList());

        return allUsers;
    }

    /**
     * Gets user.
     *
     * @param username the username
     * @return the user
     */
    public User getUser(final String username) {
        return users.stream()
                    .filter(user -> user.getUsername().equals(username))
                    .findFirst()
                    .orElse(null);
    }

    /**
     * Gets artist.
     *
     * @param username the username
     * @return the artist
     */
    public Artist getArtist(final String username) {
        return artists.stream()
                .filter(artist -> artist.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets host.
     *
     * @param username the username
     * @return the host
     */
    public Host getHost(final String username) {
        return hosts.stream()
                .filter(artist -> artist.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Update timestamp.
     *
     * @param newTimestamp the new timestamp
     */
    public void updateTimestamp(final int newTimestamp) {
        prvTmp = timestamp;
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;

        if (elapsed == 0) {
            return;
        } else if (elapsed < 0) {
            throw new IllegalArgumentException("Invalid timestamp" + newTimestamp);
        }

        users.forEach(user -> user.simulateTime(elapsed, user));
    }

    /**
     * Checks if a specified key exists in a given hash map.
     *
     * This method checks whether the specified key exists in the provided hash map.
     *
     * @param map The hash map to be checked.
     * @param key The key to be checked for existence.
     * @return {@code true} if the key exists in the map, {@code false} otherwise.
     */
    private static boolean exists(final HashMap<String, Integer> map, final String key) {
        return map.containsKey(key);
    }

     /**
     * Updates statistics in a given hash map by incrementing the count of a specified key.
     *
     * This method checks if the provided key already exists in the hash map. If it does, it
     * increments the count associated with that key. If the key does not exist, a new entry
     * is created with the key and a count of 1.
     *
     * @param map The hash map to be updated.
     * @param key The key for which the count is to be updated.
     */
    public static void updateStatistics(final HashMap<String, Integer> map, final String key) {
        if (exists(map, key)) {
            map.put(key, map.get(key) + 1);
        } else {
            map.put(key, 1);
        }
    }

    /**
     * Updates user and artist statistics when an audio file is played from the user's library.
     *
     * This method updates the statistics for both the user and the artist when an audio file is
     * played from the user's library. If the audio file is a podcast episode, it updates the top
     * episodes for the user. For songs, it increments the listener count, updates top songs,
     * albums, and fans for the artist. It also updates top artists, genres, songs, and albums
     * for the user. Premium users additionally update their premium history, while non-premium
     * users update their advertisement administration.
     *
     * @param user The user for whom statistics are being updated.
     * @param player The player containing the current audio file being played.
     */
    private static void updateLibrary(final User user, final Player player) {
        if (player.getType().equals("podcast")) {
            Episode episode = (Episode) player.getCurrentAudioFile();
            String name = episode.getName();
            updateStatistics(user.getTopEpisodes(), name);
            return;
        }

        Song song = (Song) player.getCurrentAudioFile();

        Artist artist = Admin.getInstance().getArtist(song.getArtist());
        artist.setHasStatistics(true);
        updateStatistics(artist.getTopSongs(), song.getName());
        updateStatistics(artist.getTopAlbums(), song.getAlbum());
        updateStatistics(artist.getTopFans(), user.getUsername());
        artist.setListeners(artist.getListeners() + 1);
        artist.setListened(true);

        updateStatistics(user.getTopArtists(), song.getArtist());
        updateStatistics(user.getTopGenres(), song.getGenre());
        updateStatistics(user.getTopSongs(), song.getName());
        updateStatistics(user.getTopAlbums(), song.getAlbum());
        if (user.isPremium()) {
            updateStatistics(user.getPremiumHistorySong(), song.getName());
            updateStatistics(user.getPremiumHistoryArtist(), song.getArtist());
        } else {
            updateStatistics(user.getAdAdministration().getAllSongs(), song.getName());
            updateStatistics(user.getAdAdministration().getAllArtists(), song.getArtist());
        }
    }

    /**
     * Updates user and artist statistics when a song from a playlist is played.
     *
     * This method updates the statistics for both the user and the artist when a song from a
     * playlist is played. It increments the listener count, updates top songs, albums, and
     * fans for the artist. It also updates top artists, genres, songs, and albums for the user.
     * Premium users additionally update their premium history, while non-premium users update
     * their advertisement administration.
     *
     * @param user The user for whom statistics are being updated.
     * @param player The player containing the current audio file being played.
     */
    private static void updatePlaylist(final User user, final Player player) {
        Song song = (Song) player.getCurrentAudioFile();

        Artist artist = Admin.getInstance().getArtist(song.getArtist());
        artist.setHasStatistics(true);
        updateStatistics(artist.getTopSongs(), song.getName());
        updateStatistics(artist.getTopAlbums(), song.getAlbum());
        updateStatistics(artist.getTopFans(), user.getUsername());
        artist.setListeners(artist.getListeners() + 1);
        artist.setListened(true);

        updateStatistics(user.getTopArtists(), song.getArtist());
        updateStatistics(user.getTopGenres(), song.getGenre());
        updateStatistics(user.getTopSongs(), song.getName());
        updateStatistics(user.getTopAlbums(), song.getAlbum());
        if (user.isPremium()) {
            updateStatistics(user.getPremiumHistorySong(), song.getName());
            updateStatistics(user.getPremiumHistoryArtist(), song.getArtist());
        } else {
            user.getAdAdministration().addSong(song.getName());
            user.getAdAdministration().addArtist(song.getArtist());
        }
    }

    /**
     * Updates user and host statistics when a podcast episode is played.
     *
     * This method updates the statistics for both the user and the host when a podcast
     * episode is played. It updates the user's top episodes and the host's top episodes
     * and listeners count.
     *
     * @param user The user for whom statistics are being updated.
     * @param player The player containing the current audio file being played.
     */
    private void updatePodcast(final User user, final Player player) {
        Episode episode = (Episode) player.getCurrentAudioFile();
        String name = episode.getName();
        updateStatistics(user.getTopEpisodes(), name);

        Host host = Admin.getInstance().
                getHost(player.getSource().getAudioCollection().getOwner());
        if (host == null) {
            return;
        }
        updateStatistics(host.getTopEpisodes(), name);
        updateStatistics(host.getListeners(), user.getUsername());
    }

    /**
     * Updates user and artist statistics when a song from an album is played.
     *
     * This method updates the statistics for both the user and the artist when a song from
     * an album is played. It increments the listener count, updates top songs, albums, and
     * fans for the artist.It also updates top artists, genres, songs, and albums for the user.
     * Premium users additionally update their premium history, while non-premium users update
     * their advertisement administration.
     *
     * @param user The user for whom statistics are being updated.
     * @param player The player containing the current audio file being played.
     */
    public void updateAlbum(final User user, final Player player) {
        Song song = (Song) player.getCurrentAudioFile();

        Artist artist = Admin.getInstance().getArtist(song.getArtist());
        artist.setHasStatistics(true);
        updateStatistics(artist.getTopSongs(), song.getName());
        updateStatistics(artist.getTopAlbums(), song.getAlbum());
        updateStatistics(artist.getTopFans(), user.getUsername());
        artist.setListeners(artist.getListeners() + 1);
        artist.setListened(true);

        updateStatistics(user.getTopArtists(), song.getArtist());
        updateStatistics(user.getTopGenres(), song.getGenre());
        updateStatistics(user.getTopSongs(), song.getName());
        updateStatistics(user.getTopAlbums(), song.getAlbum());
        if (user.isPremium()) {
            updateStatistics(user.getPremiumHistorySong(), song.getName());
            updateStatistics(user.getPremiumHistoryArtist(), song.getArtist());
        } else {
            user.getAdAdministration().addSong(song.getName());
            user.getAdAdministration().addArtist(song.getArtist());
        }
    }

    /**
     * Sorts and updates a user's hash map of statistics based on the specified type.
     *
     * This method takes an old hash map of statistics, sorts it first by value in descending
     * order, and then by key in ascending order. The sorted hash map is then updated in the
     * user's statistics based on the specified type, which can be "songs," "albums," "artists,"
     * "genres," or "episodes."
     *
     * @param user The user for whom the statistics hash map is being sorted and updated.
     * @param oldMap The original hash map of statistics to be sorted.
     * @param type The type of statistics to update in the user's profile (e.g., "songs,"
     *             "albums," etc.).
     * @throws IllegalStateException If an unexpected type value is provided.
     */
    public void keepHashesSorted(final User user,
                                 final HashMap<String, Integer> oldMap,
                                 final String type) {
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

        switch (type) {
            case "songs" -> user.setTopSongs((HashMap<String, Integer>) sortedHashMap);
            case "albums" -> user.setTopAlbums((HashMap<String, Integer>) sortedHashMap);
            case "artists" -> user.setTopArtists((HashMap<String, Integer>) sortedHashMap);
            case "genres" -> user.setTopGenres((HashMap<String, Integer>) sortedHashMap);
            case "episodes" -> user.setTopEpisodes((HashMap<String, Integer>) sortedHashMap);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    /**
     * Updates user statistics based on the currently played source in the player.
     *
     * This method checks the current player source type and updates the user's statistics
     * accordingly. Supported source types include LIBRARY, PLAYLIST, PODCAST, and ALBUM.
     * The user's top songs, albums, artists, genres, and episodes are then sorted and updated.
     *
     * @param user The user for whom statistics are being updated.
     */
    public void updateWrappedIndividually(final User user) {
        Player player = user.getPlayer();
        if (player.getSource() == null) {
            return;
        }
        if (player.isComeBack()) {
            return;
        }
        user.setHasStatistics(true);
        String type = player.getSource().getType().toString();
        switch (type) {
            case "LIBRARY" -> updateLibrary(user, player);
            case "PLAYLIST" -> updatePlaylist(user, player);
            case "PODCAST" -> updatePodcast(user, player);
            case "ALBUM" -> updateAlbum(user, player);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
        keepHashesSorted(user, user.getTopSongs(), "songs");
        keepHashesSorted(user, user.getTopAlbums(), "albums");
        keepHashesSorted(user, user.getTopArtists(), "artists");
        keepHashesSorted(user, user.getTopGenres(), "genres");
        keepHashesSorted(user, user.getTopEpisodes(), "episodes");
    }

    /**
     * Update wrapped.
     */
    private UserAbstract getAbstractUser(final String username) {
        ArrayList<UserAbstract> allUsers = new ArrayList<>();

        allUsers.addAll(users);
        allUsers.addAll(artists);
        allUsers.addAll(hosts);

        return allUsers.stream()
                       .filter(userPlatform -> userPlatform.getUsername().equals(username))
                       .findFirst()
                       .orElse(null);
    }

    /**
     * Add new user string.
     *
     * @param commandInput the command input
     * @return the string
     */
    public String addNewUser(final CommandInput commandInput) {
        String username = commandInput.getUsername();
        String type = commandInput.getType();
        int age = commandInput.getAge();
        String city = commandInput.getCity();

        UserAbstract currentUser = getAbstractUser(username);
        if (currentUser != null) {
            return "The username %s is already taken.".formatted(username);
        }

        if (type.equals("user")) {
            users.add(new User(username, age, city));
        } else if (type.equals("artist")) {
            artists.add(new Artist(username, age, city));
        } else {
            hosts.add(new Host(username, age, city));
        }

        return "The username %s has been added successfully.".formatted(username);
    }

    /**
     * Delete user string.
     *
     * @param username the username
     * @return the string
     */
    public String deleteUser(final String username) {
        UserAbstract currentUser = getAbstractUser(username);

        if (currentUser == null) {
            return "The username %s doesn't exist.".formatted(username);
        }

        if (currentUser.userType().equals("user")) {
            return deleteNormalUser((User) currentUser);
        }

        if (currentUser.userType().equals("host")) {
            return deleteHost((Host) currentUser);
        }

        return deleteArtist((Artist) currentUser);
    }

    /**
     * Delete normal user string.
     *
     * @param user the user
     * @return the string
     */
    private String deleteNormalUser(final User user) {
        if (user.getPlaylists().stream().anyMatch(playlist -> users.stream().map(User::getPlayer)
                .filter(player -> player != user.getPlayer())
                .map(Player::getCurrentAudioCollection)
                .filter(Objects::nonNull)
                .anyMatch(collection -> collection == playlist))) {
            return "%s can't be deleted.".formatted(user.getUsername());
        }

        user.getLikedSongs().forEach(Song::dislike);
        user.getFollowedPlaylists().forEach(Playlist::decreaseFollowers);

        users.stream().filter(otherUser -> otherUser != user)
             .forEach(otherUser -> otherUser.getFollowedPlaylists()
                                            .removeAll(user.getPlaylists()));

        users.remove(user);
        return "%s was successfully deleted.".formatted(user.getUsername());
    }

    /**
     * Delete host string.
     *
     * @param host the host
     * @return the string
     */
    private String deleteHost(final Host host) {
        if (host.getPodcasts().stream().anyMatch(podcast -> getAudioCollectionsStream()
                .anyMatch(collection -> collection == podcast))
                || users.stream().anyMatch(user -> user.getCurrentPage() == host.getPage())) {
            return "%s can't be deleted.".formatted(host.getUsername());
        }

        host.getPodcasts().forEach(podcast -> podcasts.remove(podcast));
        hosts.remove(host);

        return "%s was successfully deleted.".formatted(host.getUsername());
    }

    /**
     * Delete artist string.
     *
     * @param artist the artist
     * @return the string
     */
    private String deleteArtist(final Artist artist) {
        if (artist.getAlbums().stream().anyMatch(album -> album.getSongs().stream()
            .anyMatch(song -> getAudioFilesStream().anyMatch(audioFile -> audioFile == song))
            || getAudioCollectionsStream().anyMatch(collection -> collection == album))
            || users.stream().anyMatch(user -> user.getCurrentPage() == artist.getPage())) {
            return "%s can't be deleted.".formatted(artist.getUsername());
        }

        users.forEach(user -> artist.getAlbums().forEach(album -> album.getSongs().forEach(song -> {
            user.getLikedSongs().remove(song);
            user.getPlaylists().forEach(playlist -> playlist.removeSong(song));
        })));

        songs.removeAll(artist.getAllSongs());
        artists.remove(artist);
        return "%s was successfully deleted.".formatted(artist.getUsername());
    }

    /**
     * Add album string.
     *
     * @param commandInput the command input
     * @return the string
     */
    public String addAlbum(final CommandInput commandInput) {
        String username = commandInput.getUsername();
        String albumName = commandInput.getName();
        UserAbstract currentUser = getAbstractUser(username);

        if (currentUser == null) {
            return "The username %s doesn't exist.".formatted(username);
        } else if (!currentUser.userType().equals("artist")) {
            return "%s is not an artist.".formatted(username);
        }

        Artist currentArtist = (Artist) currentUser;
        if (currentArtist.getAlbums().stream()
            .anyMatch(album -> album.getName().equals(albumName))) {
            return "%s has another album with the same name.".formatted(username);
        }

        List<Song> newSongs = commandInput.getSongs().stream()
                                       .map(songInput -> new Song(songInput.getName(),
                                                                  songInput.getDuration(),
                                                                  albumName,
                                                                  songInput.getTags(),
                                                                  songInput.getLyrics(),
                                                                  songInput.getGenre(),
                                                                  songInput.getReleaseYear(),
                                                                  currentArtist.getUsername()))
                                       .toList();

        Set<String> songNames = new HashSet<>();
        if (!newSongs.stream().filter(song -> !songNames.add(song.getName()))
                  .collect(Collectors.toSet()).isEmpty()) {
            return "%s has the same song at least twice in this album.".formatted(username);
        }

        songs.addAll(newSongs);
        currentArtist.getAlbums().add(new Album(albumName,
                                                commandInput.getDescription(),
                                                username,
                                                newSongs,
                                                commandInput.getReleaseYear()));
        currentArtist.notifyObservers("New Album", "New Album from %s.".formatted(username));
        return "%s has added new album successfully.".formatted(username);
    }

    /**
     * Remove album string.
     *
     * @param commandInput the command input
     * @return the string
     */
    public String removeAlbum(final CommandInput commandInput) {
        String username = commandInput.getUsername();
        String albumName = commandInput.getName();

        UserAbstract currentUser = getAbstractUser(username);

        if (currentUser == null) {
            return "The username %s doesn't exist.".formatted(username);
        } else if (!currentUser.userType().equals("artist")) {
            return "%s is not an artist.".formatted(username);
        }

        Artist currentArtist = (Artist) currentUser;
        Album searchedAlbum = currentArtist.getAlbum(albumName);
        if (searchedAlbum == null) {
            return "%s doesn't have an album with the given name.".formatted(username);
        }

        if (getAudioCollectionsStream().anyMatch(collection -> collection == searchedAlbum)) {
            return "%s can't delete this album.".formatted(username);
        }

        for (Song song : searchedAlbum.getSongs()) {
            if (getAudioCollectionsStream().anyMatch(collection -> collection.containsTrack(song))
                || getAudioFilesStream().anyMatch(audioFile -> audioFile == song)) {
                return "%s can't delete this album.".formatted(username);
            }
        }

        for (Song song: searchedAlbum.getSongs()) {
            users.forEach(user -> {
                user.getLikedSongs().remove(song);
                user.getPlaylists().forEach(playlist -> playlist.removeSong(song));
            });
            songs.remove(song);
        }

        currentArtist.getAlbums().remove(searchedAlbum);
        return "%s deleted the album successfully.".formatted(username);
    }

    /**
     * Add podcast string.
     *
     * @param commandInput the command input
     * @return the string
     */
    public String addPodcast(final CommandInput commandInput) {
        String username = commandInput.getUsername();
        String podcastName = commandInput.getName();
        UserAbstract currentUser = getAbstractUser(username);

        if (currentUser == null) {
            return "The username %s doesn't exist.".formatted(username);
        } else if (!currentUser.userType().equals("host")) {
            return "%s is not a host.".formatted(username);
        }

        Host currentHost = (Host) currentUser;
        if (currentHost.getPodcasts().stream()
            .anyMatch(podcast -> podcast.getName().equals(podcastName))) {
            return "%s has another podcast with the same name.".formatted(username);
        }

        List<Episode> episodes = commandInput.getEpisodes().stream()
                                             .map(episodeInput ->
                                                     new Episode(episodeInput.getName(),
                                                                 episodeInput.getDuration(),
                                                                 episodeInput.getDescription()))
                                             .collect(Collectors.toList());

        Set<String> episodeNames = new HashSet<>();
        if (!episodes.stream().filter(episode -> !episodeNames.add(episode.getName()))
                     .collect(Collectors.toSet()).isEmpty()) {
            return "%s has the same episode in this podcast.".formatted(username);
        }

        Podcast newPodcast = new Podcast(podcastName, username, episodes);
        currentHost.getPodcasts().add(newPodcast);
        podcasts.add(newPodcast);

        return "%s has added new podcast successfully.".formatted(username);
    }


    /**
     * Remove podcast string.
     *
     * @param commandInput the command input
     * @return the string
     */
    public String removePodcast(final CommandInput commandInput) {
        String username = commandInput.getUsername();
        String podcastName = commandInput.getName();
        UserAbstract currentUser = getAbstractUser(username);

        if (currentUser == null) {
            return "The username %s doesn't exist.".formatted(username);
        } else if (!currentUser.userType().equals("host")) {
            return "%s is not a host.".formatted(username);
        }

        Host currentHost = (Host) currentUser;
        Podcast searchedPodcast = currentHost.getPodcast(podcastName);

        if (searchedPodcast == null) {
            return "%s doesn't have a podcast with the given name.".formatted(username);
        }

        if (getAudioCollectionsStream().anyMatch(collection -> collection == searchedPodcast)) {
            return "%s can't delete this podcast.".formatted(username);
        }

        currentHost.getPodcasts().remove(searchedPodcast);
        podcasts.remove(searchedPodcast);
        return "%s deleted the podcast successfully.".formatted(username);
    }

    /**
     * Add event string.
     *
     * @param commandInput the command input
     * @return the string
     */
    public String addEvent(final CommandInput commandInput) {
        String username = commandInput.getUsername();
        String eventName = commandInput.getName();

        UserAbstract currentUser = getAbstractUser(username);

        if (currentUser == null) {
            return "The username %s doesn't exist.".formatted(username);
        } else if (!currentUser.userType().equals("artist")) {
            return "%s is not an artist.".formatted(username);
        }

        Artist currentArtist = (Artist) currentUser;
        if (currentArtist.getEvent(eventName) != null) {
            return "%s has another event with the same name.".formatted(username);
        }

        String date = commandInput.getDate();

        if (!checkDate(date)) {
            return "Event for %s does not have a valid date.".formatted(username);
        }

        currentArtist.getEvents().add(new Event(eventName,
                                                commandInput.getDescription(),
                                                commandInput.getDate()));
        currentArtist.notifyObservers("New Event", "New Event from %s.".formatted(username));
        return "%s has added new event successfully.".formatted(username);
    }

    /**
     * Remove event string.
     *
     * @param commandInput the command input
     * @return the string
     */
    public String removeEvent(final CommandInput commandInput) {
        String username = commandInput.getUsername();
        String eventName = commandInput.getName();

        UserAbstract currentUser = getAbstractUser(username);

        if (currentUser == null) {
            return "The username %s doesn't exist.".formatted(username);
        } else if (!currentUser.userType().equals("artist")) {
            return "%s is not an artist.".formatted(username);
        }

        Artist currentArtist = (Artist) currentUser;
        Event searchedEvent = currentArtist.getEvent(eventName);
        if (searchedEvent == null) {
            return "%s doesn't have an event with the given name.".formatted(username);
        }

        currentArtist.getEvents().remove(searchedEvent);
        return "%s deleted the event successfully.".formatted(username);
    }

    private boolean checkDate(final String date) {
        if (date.length() != dateStringLength) {
            return false;
        }

        List<String> dateElements = Arrays.stream(date.split("-", dateFormatSize)).toList();

        if (dateElements.size() != dateFormatSize) {
            return false;
        }

        int day = Integer.parseInt(dateElements.get(0));
        int month = Integer.parseInt(dateElements.get(1));
        int year = Integer.parseInt(dateElements.get(2));

        if (day < dateDayLowerLimit
            || (month == 2 && day > dateFebHigherLimit)
            || day > dateDayHigherLimit
            || month < dateMonthLowerLimit || month > dateMonthHigherLimit
            || year < dateYearLowerLimit || year > dateYearHigherLimit) {
            return false;
        }

        return true;
    }

    /**
     * Add merch string.
     *
     * @param commandInput the command input
     * @return the string
     */
    public String addMerch(final CommandInput commandInput) {
        String username = commandInput.getUsername();
        UserAbstract currentUser = getAbstractUser(username);

        if (currentUser == null) {
            return "The username %s doesn't exist.".formatted(username);
        } else if (!currentUser.userType().equals("artist")) {
            return "%s is not an artist.".formatted(username);
        }

        Artist currentArtist = (Artist) currentUser;
        if (currentArtist.getMerch().stream()
                         .anyMatch(merch -> merch.getName().equals(commandInput.getName()))) {
            return "%s has merchandise with the same name.".formatted(currentArtist.getUsername());
        } else if (commandInput.getPrice() < 0) {
            return "Price for merchandise can not be negative.";
        }

        currentArtist.getMerch().add(new Merchandise(commandInput.getName(),
                                                     commandInput.getDescription(),
                                                     commandInput.getPrice()));
        currentArtist.notifyObservers("New Merchandise",
                "New Merchandise from %s.".formatted(username));
        return "%s has added new merchandise successfully.".formatted(username);
    }

    /**
     * Add announcement string.
     *
     * @param commandInput the command input
     * @return the string
     */
    public String addAnnouncement(final CommandInput commandInput) {
        String username = commandInput.getUsername();
        String announcementName = commandInput.getName();
        String announcementDescription = commandInput.getDescription();

        UserAbstract currentUser = getAbstractUser(username);

        if (currentUser == null) {
            return "The username %s doesn't exist.".formatted(username);
        } else if (!currentUser.userType().equals("host")) {
            return "%s is not a host.".formatted(username);
        }

        Host currentHost = (Host) currentUser;
        Announcement searchedAnnouncement = currentHost.getAnnouncement(announcementName);
        if (searchedAnnouncement != null) {
            return "%s has already added an announcement with this name.";
        }

        currentHost.getAnnouncements().add(new Announcement(announcementName,
                                                            announcementDescription));
        return "%s has successfully added new announcement.".formatted(username);
    }

    /**
     * Remove announcement string.
     *
     * @param commandInput the command input
     * @return the string
     */
    public String removeAnnouncement(final CommandInput commandInput) {
        String username = commandInput.getUsername();
        String announcementName = commandInput.getName();

        UserAbstract currentUser = getAbstractUser(username);

        if (currentUser == null) {
            return "The username %s doesn't exist.".formatted(username);
        } else if (!currentUser.userType().equals("host")) {
            return "%s is not a host.".formatted(username);
        }

        Host currentHost = (Host) currentUser;
        Announcement searchAnnouncement = currentHost.getAnnouncement(announcementName);
        if (searchAnnouncement == null) {
            return "%s has no announcement with the given name.".formatted(username);
        }

        currentHost.getAnnouncements().remove(searchAnnouncement);
        return "%s has successfully deleted the announcement.".formatted(username);
    }

    /**
     * Get the artist based on what the user is listening to at the moment.
     *
     * @param user the user that is listening to the artist
     * @return the string
     */
    public Artist getArtistByUser(final User user) {
        Player player = user.getPlayer();
        if (player.getSource() == null) {
            return null;
        }
        Song song = (Song) player.getCurrentAudioFile();
        return Admin.getInstance().getArtist(song.getArtist());
    }

    /**
     * Get the host based on what the user is listening to at the moment.
     *
     * @param user the user that is listening to the host
     * @return the string
     */
    public Host getHostByUser(final User user) {
        Player player = user.getPlayer();
        if (player.getSource() == null) {
            return null;
        }
        Podcast podcast = (Podcast) player.getCurrentAudioCollection();
        return Admin.getInstance().getHost(podcast.getOwner());
    }

    /**
     * Change page string.
     *
     * @param commandInput the command input
     * @return the string
     */
    public String changePage(final CommandInput commandInput) {
        String username = commandInput.getUsername();
        String nextPage = commandInput.getNextPage();

        UserAbstract currentUser = getAbstractUser(username);

        if (currentUser == null) {
            return "The username %s doesn't exist.".formatted(username);
        } else if (!currentUser.userType().equals("user")) {
            return "%s is not a normal user.".formatted(username);
        }

        User user = (User) currentUser;
        if (!user.isStatus()) {
            return "%s is offline.".formatted(user.getUsername());
        }
        // delete all the pages from the history that are after the current page
        user.getHistory().deletePagesAfterCurrentPage();
        switch (nextPage) {
            case "Home" -> {
                user.setCurrentPage(user.getHomePage());
                user.getHistory().addPageToHistory(user.getHomePage());
            }
            case "LikedContent" -> {
                user.setCurrentPage(user.getLikedContentPage());
                user.getHistory().addPageToHistory(user.getLikedContentPage());
            }
            case "Artist" -> {
                Artist artist = getArtistByUser(user);
                user.setCurrentPage(artist.getPage());
                user.getHistory().addPageToHistory(artist.getPage());
            }
            case "Host" -> {
                Host host = getHostByUser(user);
                user.setCurrentPage(host.getPage());
                user.getHistory().addPageToHistory(host.getPage());
            }
            default -> {
                return "%s is trying to access a non-existent page.".formatted(username);
            }
        }

        return "%s accessed %s successfully.".formatted(username, nextPage);
    }

    /**
     * Print current page string.
     *
     * @param commandInput the command input
     * @return the string
     */
    public String printCurrentPage(final CommandInput commandInput) {
        String username = commandInput.getUsername();
        UserAbstract currentUser = getAbstractUser(username);

        if (currentUser == null) {
            return "The username %s doesn't exist.".formatted(username);
        } else if (!currentUser.userType().equals("user")) {
            return "%s is not a normal user.".formatted(username);
        }

        User user = (User) currentUser;
        if (!user.isStatus()) {
            return "%s is offline.".formatted(user.getUsername());
        }

        return user.getCurrentPage().printCurrentPage();
    }

    /**
     * Switch status string.
     *
     * @param username the username
     * @return the string
     */
    public String switchStatus(final String username) {
        UserAbstract currentUser = getAbstractUser(username);

        if (currentUser == null) {
            return "The username %s doesn't exist.".formatted(username);
        }

        if (currentUser.userType().equals("user")) {
            ((User) currentUser).switchStatus();
            return username + " has changed status successfully.";
        } else {
            return username + " is not a normal user.";
        }
    }

    /**
     * Gets online users.
     *
     * @return the online users
     */
    public List<String> getOnlineUsers() {
        return users.stream().filter(User::isStatus).map(User::getUsername).toList();
    }

    private Stream<AudioCollection> getAudioCollectionsStream() {
        return users.stream().map(User::getPlayer)
                    .map(Player::getCurrentAudioCollection).filter(Objects::nonNull);
    }

    private Stream<AudioFile> getAudioFilesStream() {
        return users.stream().map(User::getPlayer)
                    .map(Player::getCurrentAudioFile).filter(Objects::nonNull);
    }

    /**
     * Gets top 5 album list.
     *
     * @return the top 5 album list
     */
    public List<String> getTop5AlbumList() {
        List<Album> albums = artists.stream().map(Artist::getAlbums)
                                    .flatMap(List::stream).toList();

        final Map<Album, Integer> albumLikes = new HashMap<>();
        albums.forEach(album -> albumLikes.put(album, album.getSongs().stream()
                                          .map(Song::getLikes).reduce(0, Integer::sum)));

        return albums.stream().sorted((o1, o2) -> {
            if ((int) albumLikes.get(o1) == albumLikes.get(o2)) {
                return o1.getName().compareTo(o2.getName());
            }
            return albumLikes.get(o2) - albumLikes.get(o1);
        }).limit(limit).map(Album::getName).toList();
    }

    /**
     * Gets top 5 artist list.
     *
     * @return the top 5 artist list
     */
    public List<String> getTop5ArtistList() {
        final Map<Artist, Integer> artistLikes = new HashMap<>();
        artists.forEach(artist -> artistLikes.put(artist, artist.getAllSongs().stream()
                                              .map(Song::getLikes).reduce(0, Integer::sum)));

        return artists.stream().sorted(Comparator.comparingInt(artistLikes::get).reversed())
                               .limit(limit).map(Artist::getUsername).toList();
    }

    /**
     * Gets top 5 songs.
     *
     * @return the top 5 songs
     */
    public List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= limit) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    /**
     * Gets top 5 playlists.
     *
     * @return the top 5 playlists
     */
    public List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= limit) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    /**
     * Updates the ranking, total revenue, and most profitable songs for listened artists.
     *
     * This method filters the list of artists to include only those that have been listened to,
     * calculates the total revenue for each listened artist, sorts the artists based on total
     * revenue in descending order, sets rankings for the listened artists, and determines the
     * most profitable songs for each artist. The updates are applied to the original list of
     * artists obtained from the Admin.
     */
    public void updateRanking() {
        List<Artist> listenedArtists = filterListenedArtists(Admin.getInstance().getArtists());
        calculateTotalRevenue(listenedArtists);
        listenedArtists.sort(Comparator.comparing(Artist::getTotalRevenue).
                reversed().thenComparing(Artist::getUsername));
        setRankings(listenedArtists);
        setMostProfitableSongs(listenedArtists);
    }

    /**
     * Filters the list of artists to include only those that have been listened to,
     * and calculates the total revenue for each listened artist.
     *
     * This method uses Java Streams to filter the input list of artists, keeping only
     * those that have been listened to (based on the 'isListened' property). Additionally,
     * it calculates the total revenue for each listened artist by summing the song revenue
     * and merchandise revenue. The modified list is then collected and returned.
     *
     * @param artists A list of artists to be filtered based on whether they have been listened to.
     * @return A new list containing only the listened artists with calculated total revenue.
     */
    private List<Artist> filterListenedArtists(final List<Artist> artists) {
        return artists.stream()
                .filter(Artist::isListened)
                .peek(artist -> artist.setTotalRevenue(artist.getSongRevenue()
                        + artist.getMerchRevenue()))
                .collect(Collectors.toList());
    }

    /**
     * Calculates the total revenue for each artist by summing song revenue and merchandise
     * revenue.
     *
     * This method iterates through the list of artists, calculates the total revenue for each
     * artist, and sets the total revenue by summing the song revenue and merchandise revenue.
     *
     * @param artists A list of artists for whom the total revenue is being calculated.
     */
    private void calculateTotalRevenue(final List<Artist> artists) {
        artists.forEach(artist -> artist.setTotalRevenue(artist.getSongRevenue()
                + artist.getMerchRevenue()));
    }

    /**
     * Sets rankings for a list of artists based on their total revenue.
     *
     * This method assigns rankings to the artists in the provided list based on their total
     * revenue. The artists are ranked in descending order of total revenue, with the first
     * artist having a ranking of 1, the second artist with a ranking of 2, and so on.
     *
     * @param artists A list of artists for whom rankings are being determined and set.
     */
    private void setRankings(final List<Artist> artists) {
        IntStream.range(0, artists.size())
                .forEach(i -> artists.get(i).setRanking(i + 1));
    }

    /**
     * Sets the most profitable songs for a list of artists based on their profit details.
     *
     * This method iterates through the provided list of artists, determines the most profitable
     * song for each artist, and sets the most profitable song accordingly. If an artist has
     * no profit details, "N/A" is set as the most profitable song.
     *
     * @param artists A list of artists for whom the most profitable songs are being determined
     *                and set.
     */
    private void setMostProfitableSongs(final List<Artist> artists) {
        artists.forEach(artist -> {
            if (artist.getProfitForEachSong().isEmpty()) {
                artist.setMostProfitableSong("N/A");
            } else {
                setMostProfitableSongForArtist(artist);
            }
        });
    }

    /**
     * Sets the most profitable song for a given artist based on their profit details.
     *
     * This method determines the most profitable song for the artist by sorting their profit
     * details in descending order of revenue. In case of a tie, the song names are compared
     * in ascending order. The most profitable song is then set for the artist.
     *
     * @param artist The artist for whom the most profitable song is being determined and set.
     */
    private void setMostProfitableSongForArtist(final Artist artist) {
        List<Map.Entry<String, Double>> entryList = new ArrayList<>(artist.
                getProfitForEachSong().entrySet());
        entryList.sort(Map.Entry.<String, Double>comparingByValue().
                reversed().thenComparing(Map.Entry::getKey));
        if (!entryList.isEmpty()) {
            artist.setMostProfitableSong(entryList.get(0).getKey());
        }
    }


    /**
     * Distributes money to artists and their songs when a user cancels their premium subscription.
     *
     * This method calculates and distributes money to artists and their songs based on the user's
     * premium history of listened songs and artists when the user cancels their premium
     * subscription. The money distribution is proportional to the number of listens each artist
     * and song received during the user's premium subscription.
     *
     * @param user The user who is canceling their premium subscription.
     */
    public void giveMoneyWhenCancel(final User user) {
        // extract from the user's history the songs that he listened to
        HashMap<String, Integer> history = user.getPremiumHistorySong();

        Double totalListenedSongs = calculateTotalListenedSongs(history);

        for (Map.Entry<String, Integer> hist : user.getPremiumHistoryArtist().entrySet()) {
            Artist artist = Admin.getInstance().getArtist(hist.getKey());
            Double listenedSongs = Double.valueOf(hist.getValue());
            Double money = (listenedSongs / totalListenedSongs) * user.getSubscriptionPrice();
            artist.setSongRevenue(artist.getSongRevenue() + money);

            // add the money for each song
            for (Map.Entry<String, Integer> entry : user.getPremiumHistorySong().entrySet()) {
                String songName = entry.getKey();
                double interMoney = (entry.getValue() / totalListenedSongs)
                        * user.getSubscriptionPrice();
                money = Math.round(interMoney * roundNumber) / roundNumber;
                // check if the artist has the song
                if (artist.getAllSongs().stream().anyMatch(song ->
                        song.getName().equals(songName))) {
                    // add the song in the artist profit list if it does not exist, and if
                    // exists, just add the money
                    if (artist.getProfitForEachSong().containsKey(songName)) {
                        artist.getProfitForEachSong().put(songName,
                                (double) (artist.getProfitForEachSong().get(songName) + money));
                    } else {
                        artist.getProfitForEachSong().put(songName, money);
                    }
                }
            }
        }
    }

    /**
     * Distributes free money to artists based on the user's history of listened songs.
     *
     * This method calculates the total listened songs from the user's history, and then
     * distributes the given amount of free money to artists based on the proportion of listens
     * each artist received. The user's advertisement price is reset to zero after distribution.
     *
     * @param price The amount of free money to be distributed.
     * @param user The user for whom free money is being distributed to artists.
     */
    public void giveMoneyFree(final Double price, final User user) {
        // Extract from the user's history the songs that he listened to
        HashMap<String, Integer> history = user.getAdAdministration().getAllSongs();

        // Calculate total listened songs
        Double totalListenedSongs = calculateTotalListenedSongs(history);

        // Distribute money to artists based on listened songs
        distributeMoneyToArtists(price, totalListenedSongs, user,
                user.getAdAdministration().getAllArtists());

        // Reset ad price
        user.setAdPrice(0);
    }

    /**
     * Calculates the total number of listened songs from a history of songs and their listen
     * counts.
     *
     * This method takes a map representing the history of songs and their corresponding
     * listen counts, calculates the total number of listened songs, and returns the result.
     *
     * @param history A map containing songs and their listen counts.
     * @return The total number of listened songs from the provided history.
     */
    private Double calculateTotalListenedSongs(final HashMap<String, Integer> history) {
        return history.values().stream()
                .mapToDouble(Integer::doubleValue)
                .sum();
    }

    /**
     * Distributes money to artists based on the total number of listens and total listened songs.
     *
     * This method calculates the money to be distributed to each artist based on the number of
     * listens and the total number of listened songs. It updates the song revenue for each
     * artist and further distributes money to individual songs for each artist.
     *
     * @param price The price per listen used for calculating the money.
     * @param totalListenedSongs The total number of listened songs, used for normalization.
     * @param user The user for whom the money is being distributed to artists.
     * @param map A map containing the artists and their corresponding listen counts.
     *            The money distribution is based on the listens in this map.
     */
    private void distributeMoneyToArtists(final Double price,
                                          final Double totalListenedSongs,
                                          final User user,
                                          final HashMap<String, Integer> map) {
        for (Map.Entry<String, Integer> hist : map.entrySet()) {
            String artistName = hist.getKey();
            Artist artist = Admin.getInstance().getArtist(artistName);

            Double listenedSongs = Double.valueOf(hist.getValue());
            Double money = (listenedSongs / totalListenedSongs) * price;

            // Update artist song revenue
            artist.setSongRevenue(artist.getSongRevenue() + money);

            // Distribute money to each song
            distributeMoneyToSongs(price, totalListenedSongs, artist,
                    user.getAdAdministration().getAllSongs());
        }
    }

    /**
     * Distributes money to songs based on the number of listens and total listened songs.
     *
     * This method calculates the money produced by each song based on the number of listens
     * and the total number of listened songs. It then updates the artist's profit for each song.
     *
     * @param price The price per listen used for calculating the money.
     * @param totalListenedSongs The total number of listened songs, used for normalization.
     * @param artist The artist to whom the money is being distributed.
     * @param map A map containing the songs and their corresponding listen counts.
     *            The money distribution is based on the listens in this map.
     */
    private void distributeMoneyToSongs(final Double price,
                                        final Double totalListenedSongs,
                                        final Artist artist,
                                        final HashMap<String, Integer> map) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String songName = entry.getKey();
            Integer songListens = entry.getValue();

            // Calculate money produced by each song
            Double moneyPerSong = (songListens / totalListenedSongs) * price;
            moneyPerSong = Math.round(moneyPerSong * roundNumber) / roundNumber;

            // Update artist profit for each song
            updateArtistProfitForEachSong(artist, songName, moneyPerSong);
        }
    }

    /**
     * Updates the profit for a specific song in the artist's profit details.
     *
     * This method checks if the artist has the specified song and updates the profit
     * for that song. If the song is not present in the artist's collection, no action is taken.
     *
     * @param artist The artist whose profit details are being updated.
     * @param songName The name of the song for which the profit is being updated.
     * @param money The amount of money to be added to the song's profit.
     *              If the song is not present, this value will not be added.
     */
    private void updateArtistProfitForEachSong(final Artist artist,
                                               final String songName,
                                               final Double money) {
        if (artist.getAllSongs().stream().anyMatch(song -> song.getName().equals(songName))) {
            artist.getProfitForEachSong().merge(songName, money, Double::sum);
        }
    }

    /**
     * Distributes merchandise revenue to the artist associated with the given merchandise name.
     *
     * This method finds the artist associated with the provided merchandise name,
     * and if found, adds the given merchandise revenue to the artist's total merchandise revenue.
     *
     * @param price The merchandise revenue to be distributed.
     * @param name The name of the merchandise for which revenue is being distributed.
     */
    public void giveMerchMoney(final Integer price, final String name) {
        // find the artist based on the merch name's
        Artist artist = Admin.getInstance().getArtists().stream().
                filter(artist1 -> artist1.getMerch().stream().
                        anyMatch(merch -> merch.getName().equals(name))).findFirst().orElse(null);

        if (artist != null) {
            artist.setMerchRevenue(artist.getMerchRevenue() + price);
        }
    }
}
