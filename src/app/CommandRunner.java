package app;

import app.audio.Collections.AlbumOutput;
import app.audio.Collections.PlaylistOutput;
import app.audio.Collections.PodcastOutput;
import app.pages.ArtistPage;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.user.Artist;
import app.user.Host;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.*;

/**
 * The type Command runner.
 */
public final class CommandRunner {
    /**
     * The Object mapper.
     */
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static Admin admin;

    /**
     * Update admin.
     */
    public static void updateAdmin() {
        admin = Admin.getInstance();
    }

    private CommandRunner() {
    }

    /**
     * Search object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode search(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        Filters filters = new Filters(commandInput.getFilters());
        String type = commandInput.getType();
        ArrayList<String> results = new ArrayList<>();
        String message = "%s is offline.".formatted(user.getUsername());

        if (user.isStatus()) {
            results = user.search(filters, type);
            message = "Search returned " + results.size() + " results";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        objectNode.put("results", objectMapper.valueToTree(results));

        return objectNode;
    }

    /**
     * Select object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode select(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());

        String message = user.select(commandInput.getItemNumber());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Load object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode load(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.load();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Play pause object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode playPause(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.playPause();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Repeat object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode repeat(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.repeat();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Shuffle object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode shuffle(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        Integer seed = commandInput.getSeed();
        String message = user.shuffle(seed);

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Forward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode forward(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.forward();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Backward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode backward(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.backward();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Like object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode like(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.like();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Next object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode next(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.next();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Prev object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode prev(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.prev();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Create playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode createPlaylist(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.createPlaylist(commandInput.getPlaylistName(),
                                             commandInput.getTimestamp());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Add remove in playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addRemoveInPlaylist(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.addRemoveInPlaylist(commandInput.getPlaylistId());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Switch visibility object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode switchVisibility(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.switchPlaylistVisibility(commandInput.getPlaylistId());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Show playlists object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showPlaylists(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        ArrayList<PlaylistOutput> playlists = user.showPlaylists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    /**
     * Follow object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode follow(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.follow();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Status object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode status(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        PlayerStats stats = user.getPlayerStats();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("stats", objectMapper.valueToTree(stats));

        return objectNode;
    }

    /**
     * Show liked songs object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showLikedSongs(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        ArrayList<String> songs = user.showPreferredSongs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    /**
     * Gets preferred genre.
     *
     * @param commandInput the command input
     * @return the preferred genre
     */
    public static ObjectNode getPreferredGenre(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String preferredGenre = user.getPreferredGenre();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(preferredGenre));

        return objectNode;
    }

    /**
     * Switch connection status object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode switchConnectionStatus(final CommandInput commandInput) {
        String message = admin.switchStatus(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Add user object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addUser(final CommandInput commandInput) {
        String message = admin.addNewUser(commandInput);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Delete user object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode deleteUser(final CommandInput commandInput) {
        String message = admin.deleteUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Add album object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addAlbum(final CommandInput commandInput) {
        String message = admin.addAlbum(commandInput);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Remove album object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode removeAlbum(final CommandInput commandInput) {
        String message = admin.removeAlbum(commandInput);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Show albums object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showAlbums(final CommandInput commandInput) {
        Artist artist = admin.getArtist(commandInput.getUsername());
        ArrayList<AlbumOutput> albums = artist.showAlbums();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(albums));

        return objectNode;
    }

    /**
     * Add event object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addEvent(final CommandInput commandInput) {
        String message = admin.addEvent(commandInput);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Remove event object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode removeEvent(final CommandInput commandInput) {
        String message = admin.removeEvent(commandInput);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Add podcast object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addPodcast(final CommandInput commandInput) {
        String message = admin.addPodcast(commandInput);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Remove podcast object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode removePodcast(final CommandInput commandInput) {
        String message = admin.removePodcast(commandInput);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Show podcasts object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showPodcasts(final CommandInput commandInput) {
        Host host = admin.getHost(commandInput.getUsername());
        List<PodcastOutput> podcasts = host.getPodcasts().stream().map(PodcastOutput::new).toList();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(podcasts));

        return objectNode;
    }

    /**
     * Add merch object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addMerch(final CommandInput commandInput) {
        String message = admin.addMerch(commandInput);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Add announcement object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addAnnouncement(final CommandInput commandInput) {
        String message = admin.addAnnouncement(commandInput);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Remove announcement object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode removeAnnouncement(final CommandInput commandInput) {
        String message = admin.removeAnnouncement(commandInput);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Gets online users.
     *
     * @param commandInput the command input
     * @return the online users
     */
    public static ObjectNode getOnlineUsers(final CommandInput commandInput) {
        List<String> onlineUsers = admin.getOnlineUsers();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(onlineUsers));

        return objectNode;
    }

    /**
     * Gets all users.
     *
     * @param commandInput the command input
     * @return the all users
     */
    public static ObjectNode getAllUsers(final CommandInput commandInput) {
        List<String> users = admin.getAllUsers();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(users));

        return objectNode;
    }

    /**
     * Change page object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode changePage(final CommandInput commandInput) {
        String message = admin.changePage(commandInput);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Print current page object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode printCurrentPage(final CommandInput commandInput) {
        String message = admin.printCurrentPage(commandInput);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Gets top 5 album list.
     *
     * @param commandInput the command input
     * @return the top 5 album list
     */
    public static ObjectNode getTop5AlbumList(final CommandInput commandInput) {
        List<String> albums = admin.getTop5AlbumList();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(albums));

        return objectNode;
    }

    /**
     * Gets top 5 artist list.
     *
     * @param commandInput the command input
     * @return the top 5 artist list
     */
    public static ObjectNode getTop5ArtistList(final CommandInput commandInput) {
        List<String> artists = admin.getTop5ArtistList();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(artists));

        return objectNode;
    }

    /**
     * Gets top 5 songs.
     *
     * @param commandInput the command input
     * @return the top 5 songs
     */
    public static ObjectNode getTop5Songs(final CommandInput commandInput) {
        List<String> songs = admin.getTop5Songs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    /**
     * Gets top 5 playlists.
     *
     * @param commandInput the command input
     * @return the top 5 playlists
     */
    public static ObjectNode getTop5Playlists(final CommandInput commandInput) {
        List<String> playlists = admin.getTop5Playlists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    /**
     * Creates a wrapped JSON object based on the provided {@link CommandInput}.
     *
     * This method generates a JSON object containing statistical data based on the user, artist,
     * or host specified in the given {@code commandInput}. The structure of the JSON object
     * varies based on the type of entity, including top albums, songs, fans, and listeners count.
     *
     * @param commandInput The command input containing information about the user, artist,
     *                     or host.
     * @return The wrapped JSON object with statistical data or a message
     * indicating no data available.
     */
    public static ObjectNode wrapped(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        User user = admin.getUser(commandInput.getUsername());
        Artist artist = admin.getArtist(commandInput.getUsername());
        Host host = admin.getHost(commandInput.getUsername());

        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (artist != null) {
            if (!artist.isHasStatistics()) {
                objectNode.put("message", "No data to show for artist %s."
                        .formatted(artist.getUsername()));
                return objectNode;
            }

            JsonNodeFactory factory = JsonNodeFactory.instance;
            ObjectNode result = factory.objectNode();
            result.set("topAlbums", objectMapper.valueToTree(artist.getTopAlbumsM()));
            result.set("topSongs", objectMapper.valueToTree(artist.getTopSongsM()));
            result.set("topFans", objectMapper.valueToTree(artist.getTopFansM()));
            result.set("listeners", objectMapper.valueToTree(artist.getTopFans().size()));
            objectNode.set("result", result);

            return objectNode;
        }
        if (host != null) {
            JsonNodeFactory factory = JsonNodeFactory.instance;
            ObjectNode result = factory.objectNode();

            result.set("topEpisodes", objectMapper.valueToTree(host.getTopEpisodes()));
            result.set("listeners", objectMapper.valueToTree(host.getListeners().size()));

            objectNode.set("result", result);

            return objectNode;
        }
        if (!user.isHasStatistics()) {
            objectNode.put("message", "No data to show for user %s.".
                    formatted(user.getUsername()));

            return objectNode;
        }

        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode result = factory.objectNode();
        result.set("topArtists", objectMapper.valueToTree(user.getTopArtistsM()));
        result.set("topGenres", objectMapper.valueToTree(user.getTopGenresM()));
        result.set("topSongs", objectMapper.valueToTree(user.getTopSongsM()));
        result.set("topAlbums", objectMapper.valueToTree(user.getTopAlbumsM()));
        result.set("topEpisodes", objectMapper.valueToTree(user.getTopEpisodesM()));
        objectNode.set("result", result);

        return objectNode;
    }

    /**
     * Ends the program, processes cancellations for premium users, updates rankings, and
     * generates a summary report.
     *
     * This method initiates the program termination process. It processes cancellations
     * for premium users, updates rankings, and generates a summary report containing information
     * about artists, including song revenue, merch revenue, ranking, and most profitable song.
     *
     * @return The JSON object summarizing the program's end with artist-related information.
     */
    public static ObjectNode endProgramm() {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "endProgram");
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode result = factory.objectNode();

        for (User user : Admin.getInstance().getUsers()) {
            if (!user.isPremium()) {
                continue;
            }
            Admin.getInstance().giveMoneyWhenCancel(user);
        }

        Admin.getInstance().updateRanking();
        for (Artist artist : Admin.getInstance().getArtists()) {
            if (!artist.isListened()) {
                continue;
            }
            HashMap<String, Object> artistMap = new HashMap<>();
            artistMap.put("songRevenue", Math.round(artist.getSongRevenue()
                    * Admin.getRoundNumber()) / Admin.getRoundNumber());
            artistMap.put("merchRevenue", artist.getMerchRevenue());
            artistMap.put("ranking", artist.getRanking());
            artistMap.put("mostProfitableSong", artist.getMostProfitableSong());

            result.set(artist.getUsername(), objectMapper.valueToTree(artistMap));
        }
        objectNode.set("result", result);

        return objectNode;
    }

    /**
     * Processes the purchase of a premium subscription for a user.
     *
     * This method handles the purchase of a premium subscription for the specified user. It
     * checks if the user exists, whether the user is already a premium user, and updates the
     * user's premium status accordingly.
     *
     * @param commandInput The command input containing information about the user's premium
     *                     subscription purchase.
     * @return The JSON object indicating the result of the premium subscription purchase.
     */
    public static ObjectNode buyPremium(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", user.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            objectNode.put("message", "The username %s doesn't exist.".
                    formatted(user.getUsername()));
            return objectNode;
        }
        if (user.isPremium()) {
            objectNode.put("message", "%s is already a premium user.".
                    formatted(user.getUsername()));
            return objectNode;
        }
        user.setPremium(true);
        objectNode.put("message", "%s bought the subscription successfully.".
                formatted(user.getUsername()));

        return objectNode;
    }

    /**
     * Processes the cancellation of a premium subscription for a user.
     *
     * This method handles the cancellation of a premium subscription for the specified user.
     * It checks if the user exists, whether the user is a premium user, and updates the user's
     * premium status accordingly. Additionally, it gives money to the artists, clears the premium
     * history for songs and artists, and generates a JSON object indicating the result
     * of the premium subscription cancellation.
     *
     * @param commandInput The command input containing information about the user's premium
     *                     subscription cancellation.
     * @return The JSON object indicating the result of the premium subscription cancellation.
     */
    public static ObjectNode cancelPremium(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", user.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            objectNode.put("message", "The username %s doesn't exist.".
                    formatted(user.getUsername()));
            return objectNode;
        }
        if (!user.isPremium()) {
            objectNode.put("message", "%s is not a premium user.".
                    formatted(user.getUsername()));
            return objectNode;
        }
        user.setPremium(false);
        objectNode.put("message", "%s cancelled the subscription successfully.".
                formatted(user.getUsername()));

        // give the money to the artists
        Admin.getInstance().giveMoneyWhenCancel(user);
        // clear the list for this user
        user.getPremiumHistorySong().clear();
        user.getPremiumHistoryArtist().clear();

        return objectNode;
    }

    /**
     * Initiates an ad break for a user's current music playback.
     *
     * This method handles the initiation of an ad break for the specified user's current music
     * playback. It checks if the user exists, if the user is currently playing music, and sets
     * the user's ad-related properties accordingly. The command input may include an ad price
     * that determines the cost of the ad break.
     *
     * @param commandInput The command input containing information about the ad break initiation,
     *                     including the username
     *                     and optional ad price.
     * @return The JSON object indicating the result of the ad break initiation.
     */
    public static ObjectNode adBreak(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", user.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            objectNode.put("message", "The username %s doesn't exist.".
                    formatted(user.getUsername()));
            return objectNode;
        }

        if (user.getPlayer().getSource() == null) {
            objectNode.put("message", "%s is not playing any music.".
                    formatted(user.getUsername()));
            return objectNode;
        }

        user.getPlayer().setShouldInsertAd(true);
        Integer price = commandInput.getPrice();
        user.setAdPrice(price);

        objectNode.put("message", "Ad inserted successfully.");
        return objectNode;
    }

    /**
     * Initiates a merch purchase for a user from a specific artist's page.
     *
     * This method handles the process of a user buying merch from a specific artist's page.
     * It checks if the user and artist page exist, verifies the existence of the specified
     * merch, sets the artist as listened, adds the merch to the user's collection, and updates
     * the revenue for the artist. The command input includes information about the username,
     * timestamp, and the name of the merch to be purchased.
     *
     * @param commandInput The command input containing information about the merch purchase,
     *                     including the username, timestamp, and the name of the merch.
     * @return The JSON object indicating the result of the merch purchase initiation.
     */
    public static ObjectNode buyMerch(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", user.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        ArtistPage artistPage = (ArtistPage) user.getCurrentPage();


        if (user == null) {
            objectNode.put("message", "The username %s doesn't exist.".
                    formatted(user.getUsername()));
            return objectNode;
        }

        if (artistPage == null) {
            objectNode.put("message", "Cannot buy merch from this page.");
            return objectNode;
        }
        String merch = commandInput.getName();
        if (!artistPage.getMerch().contains(merch)) {
            objectNode.put("message", "The merch %s doesn't exist.".formatted(merch));
            return objectNode;
        }

        // set the artist as listened
        // Artist extends ContentCreator, which has the field page, from which we can check if it
        // is the artist we are looking for
        for (Artist artist : Admin.getInstance().getArtists()) {
            ArtistPage artistPag = (ArtistPage) artist.getPage();
            if (artistPag.getMerch().contains(merch)) {
                artist.setListened(true);
                break;
            }
        }

        Integer price = artistPage.getMerchPrice(merch);
        user.getMerch().add(merch);
        Admin.getInstance().giveMerchMoney(price, merch);

        objectNode.put("message", "%s has added new merch successfully.".
                formatted(user.getUsername()));
        return objectNode;
    }

    /**
     * Retrieves the list of merch owned by a user.
     *
     * This method fetches and returns the list of merch owned by a user. It checks if the user
     * exists and provides information about the username, timestamp, and the result containing
     * the user's merch collection.
     *
     * @param commandInput The command input containing information about fetching merch,
     *                     including the username and timestamp.
     * @return The JSON object indicating the result of the merch retrieval,
     * including the user's merch collection.
     */
    public static ObjectNode seeMerch(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", user.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            objectNode.put("message", "The username %s doesn't exist.".
                    formatted(user.getUsername()));
            return objectNode;
        }
        objectNode.put("result", objectMapper.valueToTree(user.getMerch()));
        return objectNode;
    }

    /**
     * Updates user recommendations based on the specified recommendation type.
     *
     * This method fetches the user, checks for the existence of the user, and then checks if
     * there are new recommendations based on the specified recommendation type. If new
     * recommendations are found, it updates the user's recommendations, and a success message
     * is included in the result. If no new recommendations are found, a message indicating the
     * absence of new recommendations is returned.
     *
     * @param commandInput The command input containing information about updating
     *                     recommendations, including the username, timestamp, and recommendation
     *                     type.
     * @return The JSON object indicating the result of the recommendation update operation,
     *         including a success message or a message indicating no new recommendations were
     *         found.
     */
    public static ObjectNode updateRecommendations(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", user.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            objectNode.put("message", "The username %s doesn't exist.".
                    formatted(user.getUsername()));
            return objectNode;
        }
        // check if there are new recommendations
        boolean shouldUpdate = user.checkForNewRecommendations(commandInput.
                getRecommendationType());
        if (!shouldUpdate) {
            objectNode.put("message", "No new recommendations were found");
            return objectNode;
        }
        user.updateRecommendations(commandInput.getRecommendationType());
        objectNode.put("message", "The recommendations for user %s have been updated successfully."
                .formatted(user.getUsername()));

        return objectNode;
    }

    /**
     * Handles user subscription and unsubscription to an artist or host page.
     *
     * This method fetches the user, checks for the existence of the user, and then verifies if
     * the user is on the page of an artist or host. If the user is on an artist or host page,
     * it subscribes or unsubscribes the user accordingly, updates the observer list of the
     * artist, and provides a success message indicating the subscription or unsubscription status.
     * If the user is not on an artist or host page, a message is returned indicating the
     * requirement to be on the page of an artist or host to perform a subscription operation.
     *
     * @param commandInput The command input containing information about the subscription
     *                     operation, including the username, timestamp, and other details.
     * @return The JSON object indicating the result of the subscription operation,
     *         including a success message or a message indicating the requirement to be on the
     *         page of an artist or host.
     */
    public static ObjectNode subscribe(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", user.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            objectNode.put("message", "The username %s doesn't exist."
                    .formatted(user.getUsername()));
            return objectNode;
        }
        ArtistPage artistPage = (ArtistPage) user.getCurrentPage();
        if (artistPage == null) {
            objectNode.put("message", "To subscribe you need to be on the page "
                    + "of an artist or host.");
            return objectNode;
        }
        Artist artist = Admin.getInstance().getArtist(artistPage.getOwner());
        if (artist.getObservers().contains(user)) {
            artist.removeObserver(user);
            objectNode.put("message", "%s unsubscribed from %s successfully."
                    .formatted(user.getUsername(), artist.getUsername()));
            return objectNode;
        }
        artist.addObserver(user);
        objectNode.put("message", "%s subscribed to %s successfully."
                .formatted(user.getUsername(), artist.getUsername()));

        return objectNode;
    }

    /**
     * Retrieves notifications for a user and clears the notification list.
     *
     * This method fetches the user, checks for the existence of the user, retrieves notifications
     * from the user's notification list, and structures them into a JSON array. The resulting
     * JSON object includes a field called "notifications" containing a list of notifications,
     * where each notification has a "name" and a "description".
     * After retrieving the notifications, the method clears the user's notification list.
     *
     * @param commandInput The command input containing information about the request, including
     *                     the username, timestamp, and other details.
     * @return The JSON object containing the retrieved notifications structured as an array with
     *         "name" and "description" fields, and a message indicating a successful retrieval of
     *         notifications.
     */
    public static ObjectNode getNotifications(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", user.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            objectNode.put("message", "The username %s doesn't exist."
                    .formatted(user.getUsername()));
            return objectNode;
        }
        // put all the notifications into a filed called notifications from the list, where
        // "name": key   and "description": value
        ArrayNode notifications = objectMapper.createArrayNode();
        for (int i = 0; i < user.getMessages().size(); i++) {
            ObjectNode notification = objectMapper.createObjectNode();
            notification.put("name", user.getMessages().get(i));
            notification.put("description", user.getDescription().get(i));
            notifications.add(notification);
        }

        objectNode.put("notifications", notifications);
        user.clearNotifications();

        return objectNode;
    }

    /**
     * Navigates the user to the previous page in their browsing history.
     *
     * This method fetches the user, checks for the existence of the user, attempts to navigate
     * the user to the previous page in their browsing history, and provides a message indicating
     * the success or failure of the navigation.
     *
     * @param commandInput The command input containing information about the request, including
     *                     the username, timestamp, and other details.
     * @return The JSON object containing the command, user, timestamp, and a message indicating
     *         the success or failure of navigating the user to the previous page.
     */
    public static ObjectNode previousPage(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", user.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            objectNode.put("message", "The username %s doesn't exist."
                    .formatted(user.getUsername()));
            return objectNode;
        }
        // change the current page of the user to the previous one
        user.getHistory().goBack(user);
        // check if it worked
        if (user.getHistory().getCurrentIndex() == -1) {
            objectNode.put("message", "There are no pages left to go back.");
            return objectNode;
        }
        objectNode.put("message", "The user %s has navigated successfully to the previous page."
                .formatted(user.getUsername()));
        return objectNode;
    }

    /**
     * Navigates the user to the next page in their browsing history.
     *
     * This method fetches the user, checks for the existence of the user, attempts to navigate
     * the user to the next page in their browsing history, and provides a message indicating
     * the success or failure of the navigation.
     *
     * @param commandInput The command input containing information about the request, including
     *                     the username, timestamp, and other details.
     * @return The JSON object containing the command, user, timestamp, and a message indicating
     *         the success or failure of navigating the user to the next page.
     */
    public static ObjectNode nextPage(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", user.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            objectNode.put("message", "The username %s doesn't exist."
                    .formatted(user.getUsername()));
            return objectNode;
        }
        // change the current page of the user to the next one
        user.getHistory().goForward(user);
        // check if it worked
        if (user.getHistory().getCurrentIndex() == user.getHistory().getHistory().size() - 1) {
            objectNode.put("message", "There are no pages left to go forward.");
            return objectNode;
        }
        objectNode.put("message", "The user %s has navigated successfully to the next page."
                .formatted(user.getUsername()));
        return objectNode;
    }

    /**
     * Loads recommendations for a user based on their preferences and playback history.
     *
     * This method retrieves the user, checks for the existence of the user, validates the
     * availability of recommendations, and loads the recommendations into the user's playback
     * source. It also provides a message indicating the success or failure of loading the
     * recommendations.
     *
     * @param commandInput The command input containing information about the request, including
     *                     the username, timestamp, and other details.
     * @return The JSON object containing the command, user, timestamp, and a message indicating
     *         the success or failure of loading the recommendations.
     */
    public static ObjectNode loadRecommendations(final CommandInput commandInput) {
        User user = Admin.getInstance().getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "loadRecommendations");
        objectNode.put("user", user.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user == null) {
            objectNode.put("message", "The username %s doesn't exist."
                    .formatted(user.getUsername()));
            return objectNode;
        }

        if (user.getLastTypeOfRecommendations().equals("empty")) {
            objectNode.put("message", "No recommendations available.");
            return objectNode;
        }

        if (!user.isStatus()) {
            objectNode.put("message", "%s is offline.".formatted(user.getUsername()));
            return objectNode;
        }

        user.getPlayer().setSource(user.getLastRecommendation(),
                user.getLastTypeOfRecommendations());
        user.getSearchBar().clearSelection();
        user.getPlayer().pause();

        Admin.getInstance().updateWrappedIndividually(user);

        objectNode.put("message", "Playback loaded successfully.");

        return objectNode;
    }
}
