package me.Visionexe.ZombieArena.Entity;

import me.Visionexe.ZombieArena.Log;
import me.Visionexe.ZombieArena.Storage.DatabaseConnection;
import me.Visionexe.ZombieArena.ZombieArena;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PlayerWrapper {
    // HashMap relating UUID to each PlayerWrapper
    private static final HashMap<UUID, PlayerWrapper> all = new HashMap<>();
    // Obviously to store players' UUIDs
    private UUID uuid;
    // Obviously to store players' statistics to track
    private HashMap<String, Integer> playerStats;

    /*
    Constructor to initialize a new player
    Initialize function also calls Load function if a player is found
     */
    private PlayerWrapper(UUID uuid) {
        this.uuid = uuid;
        this.playerStats = new HashMap<>();

        Log.debug("PlayerWrapper Constructor");
        Log.debug("PlayerWrapper size: " + all.size());
        all.put(this.uuid, this);

        init();
    }

    /*
    Initialises the Player Wrapper.
    Adds an entry to the database with default values if an entry doesn't exist yet.
     */
    private void init() {
        DatabaseConnection connection = new DatabaseConnection();

        // Statement used to select players from database with the corresponding UUID
        Optional<PreparedStatement> psOptional = connection.prepareStatement("SELECT COUNT(*) FROM `players` WHERE `uuid`=?;");

        if (!psOptional.isPresent()) {
            Log.debug("An error occurred during player initialisation.");
            return;
        }

        PreparedStatement ps = psOptional.get();
        try {
            // Use previously implemented statement to actually obtain the information associated to the UUID
            ps.setString(1, this.uuid.toString());
            ResultSet rs = connection.query(ps);

            rs.next();

            int count = rs.getInt(1);

            Log.debug("Count: " + count);

            // Run load and close database connections if a player exists
            if (count > 0) {
                Log.debug("Player already exists... Loading data...");
                load();

                ps.close();
                rs.close();
                connection.close();
                return;
            }
            ps.close();
            rs.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Start a new Database Connection in order to initialize a new player if not found
        connection = new DatabaseConnection();
        // Statement to prepare to insert new player into database
        psOptional = connection.prepareStatement("INSERT INTO `players` (" +
                "`uuid`, " +
                "`level`, " +
                "`experience`, " +
                "`prestige`, " +
                "`games_played`, " +
                "`games_won`, " +
                "`total_kills`, " +
                "`zombie_kills`, " +
                "`skeleton_kills`, " +
                "`spider_kills`, " +
                "`piglin_brute_kills`, " +
                "`zoglin_kills`, " +
                "`blaze_kills`, " +
                "`wither_skeleton_kills`, " +
                "`total_boss_kills`, " +
                "`wave_10_boss_kills`, " +
                "`wave_20_boss_kills`, " +
                "`wave_30_boss_kills`, " +
                "`wave_40_boss_kills`, " +
                "`wave_50_boss_kills`, " +
                "`total_boss_damage`, " +
                "`wave_10_boss_damage`, " +
                "`wave_20_boss_damage`, " +
                "`wave_30_boss_damage`, " +
                "`wave_40_boss_damage`, " +
                "`wave_50_boss_damage`" +
                ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"
        );

        if (!psOptional.isPresent()) {
            Log.debug("An error occurred during player initialisation.");
            return;
        }

        ps = psOptional.get();

        try {
            // Insert default information into database using previously declared statement
            ps.setString(1, this.uuid.toString());
            ps.setInt(2, 1); // Level
            ps.setInt(3, 0); // Exp
            ps.setInt(4, 0); // Prestige
            ps.setInt(5, 0); // Games Played
            ps.setInt(6, 0); // Games Won
            ps.setInt(7, 0); // Total Kills
            ps.setInt(8, 0); // Zombie Kills
            ps.setInt(9, 0); // Skeleton Kills
            ps.setInt(10, 0); // Spider Kills
            ps.setInt(11, 0); // Piglin Brute Kills
            ps.setInt(12, 0); // Zoglin Kills
            ps.setInt(13, 0); // Blaze Kills
            ps.setInt(14, 0); // Wither Skeleton Kills
            ps.setInt(15, 0); // Total Boss Kills
            ps.setInt(16, 0); // Wave 10 Boss Kills
            ps.setInt(17, 0); // Wave 20 Boss Kills
            ps.setInt(18, 0); // Wave 30 Boss Kills
            ps.setInt(19, 0); // Wave 40 Boss Kills
            ps.setInt(20, 0); // Wave 50 Boss Kills
            ps.setInt(21, 0); // Total Boss Damage
            ps.setInt(22, 0); // Wave 10 Boss Damage
            ps.setInt(23, 0); // Wave 20 Boss Damage
            ps.setInt(24, 0); // Wave 30 Boss Damage
            ps.setInt(25, 0); // Wave 40 Boss Damage
            ps.setInt(26, 0); // Wave 50 Boss Damage

            connection.update(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            connection.close();
        }
    }


    /*
    Loads the player data from the database.
    Called in init minimize the amount of places where this needs to be called
     */
    public void load() {
        // Set up the Player Stats HashMap to default values to replace when loading information from database
        this.playerStats.put("level", 1);
        this.playerStats.put("experience", 0);
        this.playerStats.put("prestige", 0);
        this.playerStats.put("games_played", 0);
        this.playerStats.put("games_won", 0);
        this.playerStats.put("total_kills", 0);
        this.playerStats.put("zombie_kills", 0);
        this.playerStats.put("skeleton_kills", 0);
        this.playerStats.put("spider_kills", 0);
        this.playerStats.put("piglin_brute_kills", 0);
        this.playerStats.put("zoglin_kills", 0);
        this.playerStats.put("blaze_kills", 0);
        this.playerStats.put("wither_skeleton_kills", 0);
        this.playerStats.put("total_boss_kills", 0);
        this.playerStats.put("wave_10_boss_kills", 0);
        this.playerStats.put("wave_20_boss_kills", 0);
        this.playerStats.put("wave_30_boss_kills", 0);
        this.playerStats.put("wave_40_boss_kills", 0);
        this.playerStats.put("wave_50_boss_kills", 0);
        this.playerStats.put("total_boss_damage", 0);
        this.playerStats.put("wave_10_boss_damage", 0);
        this.playerStats.put("wave_20_boss_damage", 0);
        this.playerStats.put("wave_30_boss_damage", 0);
        this.playerStats.put("wave_40_boss_damage", 0);
        this.playerStats.put("wave_50_boss_damage", 0);

        // Connect to database and get statement ready to select from given UUID
        DatabaseConnection connection = new DatabaseConnection();
        Optional<PreparedStatement> psOptional = connection.prepareStatement("SELECT * FROM `players` WHERE `uuid` = ?;");

        if (!psOptional.isPresent()) {
            Log.debug("Failed to load data for player with UUID '" + this.uuid.toString() + "'! This might lead to data loss!");
            return;
        }

        PreparedStatement ps = psOptional.get();

        try {
            ps.setString(1, this.uuid.toString());
            Log.debug("Loading UUID: " + this.uuid.toString());

            Log.debug("Query Before Result Set: " + ps);
            ResultSet rs = connection.query(ps);

            if (rs.next()) {
                this.playerStats.replace("level", rs.getInt("level"));
                this.playerStats.replace("experience", rs.getInt("experience"));
                this.playerStats.replace("prestige", rs.getInt("prestige"));
                this.playerStats.replace("games_played", rs.getInt("games_played"));
                this.playerStats.replace("games_won", rs.getInt("games_won"));
                this.playerStats.replace("total_kills", rs.getInt("total_kills"));
                this.playerStats.replace("zombie_kills", rs.getInt("zombie_kills"));
                this.playerStats.replace("skeleton_kills", rs.getInt("skeleton_kills"));
                this.playerStats.replace("spider_kills", rs.getInt("spider_kills"));
                this.playerStats.replace("piglin_brute_kills", rs.getInt("piglin_brute_kills"));
                this.playerStats.replace("zoglin_kills", rs.getInt("zoglin_kills"));
                this.playerStats.replace("blaze_kills", rs.getInt("blaze_kills"));
                this.playerStats.replace("wither_skeleton_kills", rs.getInt("wither_skeleton_kills"));
                this.playerStats.replace("total_boss_kills", rs.getInt("total_boss_kills"));
                this.playerStats.replace("wave_10_boss_kills", rs.getInt("wave_10_boss_kills"));
                this.playerStats.replace("wave_20_boss_kills", rs.getInt("wave_20_boss_kills"));
                this.playerStats.replace("wave_30_boss_kills", rs.getInt("wave_30_boss_kills"));
                this.playerStats.replace("wave_40_boss_kills", rs.getInt("wave_40_boss_kills"));
                this.playerStats.replace("wave_50_boss_kills", rs.getInt("wave_50_boss_kills"));
                this.playerStats.replace("total_boss_damage", rs.getInt("total_boss_damage"));
                this.playerStats.replace("wave_10_boss_damage", rs.getInt("wave_10_boss_damage"));
                this.playerStats.replace("wave_20_boss_damage", rs.getInt("wave_20_boss_damage"));
                this.playerStats.replace("wave_30_boss_damage", rs.getInt("wave_30_boss_damage"));
                this.playerStats.replace("wave_40_boss_damage", rs.getInt("wave_40_boss_damage"));
                this.playerStats.replace("wave_50_boss_damage", rs.getInt("wave_50_boss_damage"));
                Log.debug("Level: " + rs.getInt("level"));
                Log.debug("Experience: " + rs.getInt("experience"));
                Log.debug("Prestige: " + rs.getInt("prestige"));
                Log.debug("Games Played: " + rs.getInt("games_played"));
                Log.debug("Games Won: " + rs.getInt("games_won"));
                Log.debug("Total Kills: " + rs.getInt("total_kills"));
                Log.debug("RS FIRST = true");
            } else {
                Log.debug("RS FIRST = false");
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.debug("An error occurred while trying to load the data for player UUID '" + this.uuid.toString() + "'.");
        }

        try {
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
    }

    /*
    Saves the player data into the database.
     */
    public void save() {
        DatabaseConnection connection = new DatabaseConnection();
//		INSERT INTO visits (ip, hits)
//		VALUES ('127.0.0.1', 1)
//		ON CONFLICT(ip) DO UPDATE SET hits = hits + 1;
        Optional<PreparedStatement> psOptional = connection.prepareStatement("UPDATE `players` SET " +
                "`level` = ?, " +
                "`experience` = ?, " +
                "`prestige` = ?, " +
                "`games_played` = ?, " +
                "`games_won` = ?, " +
                "`total_kills` = ?, " +
                "`zombie_kills` = ?, " +
                "`skeleton_kills` = ?, " +
                "`spider_kills` = ?, " +
                "`piglin_brute_kills` = ?, " +
                "`zoglin_kills` = ?, " +
                "`blaze_kills` = ?, " +
                "`wither_skeleton_kills` = ?, " +
                "`total_boss_kills` = ?, " +
                "`wave_10_boss_kills` = ?, " +
                "`wave_20_boss_kills` = ?, " +
                "`wave_30_boss_kills` = ?, " +
                "`wave_40_boss_kills` = ?, " +
                "`wave_50_boss_kills` = ?, " +
                "`total_boss_damage` = ?, " +
                "`wave_10_boss_damage` = ?, " +
                "`wave_20_boss_damage` = ?, " +
                "`wave_30_boss_damage` = ?, " +
                "`wave_40_boss_damage` = ?, " +
                "`wave_50_boss_damage` = ? " +
                "WHERE `uuid` = ?;"
        );

        if (!psOptional.isPresent()) {
            Log.debug("Failed to save data of player with UUID '" + this.uuid.toString() + "'. Data loss might have occurred!");
            return;
        }

        PreparedStatement ps = psOptional.get();

        try {
            ps.setInt(1, this.playerStats.get("level"));
            ps.setInt(2, this.playerStats.get("experience"));
            ps.setInt(3, this.playerStats.get("prestige"));
            ps.setInt(4, this.playerStats.get("games_played"));
            ps.setInt(5, this.playerStats.get("games_won"));
            ps.setInt(6, this.playerStats.get("total_kills"));
            ps.setInt(7, this.playerStats.get("zombie_kills")); // Zombie Kills
            ps.setInt(8, this.playerStats.get("skeleton_kills")); // Skeleton Kills
            ps.setInt(9, this.playerStats.get("spider_kills")); // Spider Kills
            ps.setInt(10, this.playerStats.get("piglin_brute_kills")); // Piglin Brute Kills
            ps.setInt(11, this.playerStats.get("zoglin_kills")); // Zoglin Kills
            ps.setInt(12, this.playerStats.get("blaze_kills")); // Blaze Kills
            ps.setInt(13, this.playerStats.get("wither_skeleton_kills")); // Wither Skeleton Kills
            ps.setInt(14, this.playerStats.get("total_boss_kills"));
            ps.setInt(15, this.playerStats.get("wave_10_boss_kills")); // Wave 10 Boss Kills
            ps.setInt(16, this.playerStats.get("wave_20_boss_kills")); // Wave 20 Boss Kills
            ps.setInt(17, this.playerStats.get("wave_30_boss_kills")); // Wave 30 Boss Kills
            ps.setInt(18, this.playerStats.get("wave_40_boss_kills")); // Wave 40 Boss Kills
            ps.setInt(19, this.playerStats.get("wave_50_boss_kills")); // Wave 50 Boss Kills
            ps.setInt(20, this.playerStats.get("total_boss_damage")); // Total Boss Damage
            ps.setInt(21, this.playerStats.get("wave_10_boss_damage")); // Wave 10 Boss Damage
            ps.setInt(22, this.playerStats.get("wave_20_boss_damage")); // Wave 20 Boss Damage
            ps.setInt(23, this.playerStats.get("wave_30_boss_damage")); // Wave 30 Boss Damage
            ps.setInt(24, this.playerStats.get("wave_40_boss_damage")); // Wave 40 Boss Damage
            ps.setInt(25, this.playerStats.get("wave_50_boss_damage")); // Wave 50 Boss Damage
            ps.setString(26, this.uuid.toString());

            connection.update(ps);

            Log.debug("Saving data for player UUID '" + this.uuid.toString() + "'.");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.debug("An error occurred while trying to save the data for player UUID '" + this.uuid.toString() + "'.");
        }

        try {
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
    }

    /*
    Getters for all statistics
     */
    public HashMap<String, Integer> getPlayerStats() { return this.playerStats; }
    public int getLevel() { return this.playerStats.get("level"); }
    public int getExperience() { return playerStats.get("experience"); }
    public int getPrestige() { return this.playerStats.get("prestige"); }
    public int getGamesPlayed() { return this.playerStats.get("games_played"); }
    public int getGamesWon() { return this.playerStats.get("games_won"); }
    public int getTotalKills() { return this.playerStats.get("total_kills"); }
    public int getZombieKills() { return this.playerStats.get("zombie_kills"); }
    public int getSkeletonKills() { return this.playerStats.get("skeleton_kills"); }
    public int getSpiderKills() { return this.playerStats.get("spider_kills"); }
    public int getPiglinBruteKills() { return this.playerStats.get("piglin_brute_kills"); }
    public int getZoglinKills() { return this.playerStats.get("zoglin_kills"); }
    public int getBlazeKills() { return this.playerStats.get("blaze_kills"); }
    public int getWitherSkeletonKills() { return this.playerStats.get("wither_skeleton_kills"); }
    public int getTotalBossKills() { return this.playerStats.get("total_boss_kills"); }
    public int getWave10BossKills() { return this.playerStats.get("wave_10_boss_kills"); }
    public int getWave20BossKills() { return this.playerStats.get("wave_20_boss_kills"); }
    public int getWave30BossKills() { return this.playerStats.get("wave_30_boss_kills"); }
    public int getWave40BossKills() { return this.playerStats.get("wave_40_boss_kills"); }
    public int getWave50BossKills() { return this.playerStats.get("wave_50_boss_kills"); }
    public int getTotalBossDamage() { return this.playerStats.get("total_boss_damage"); }
    public int getWave10BossDamage() { return this.playerStats.get("wave_10_boss_damage"); }
    public int getWave20BossDamage() { return this.playerStats.get("wave_20_boss_damage"); }
    public int getWave30BossDamage() { return this.playerStats.get("wave_30_boss_damage"); }
    public int getWave40BossDamage() { return this.playerStats.get("wave_40_boss_damage"); }
    public int getWave50BossDamage() { return this.playerStats.get("wave_50_boss_damage"); }
    public int getGenericMobKills(String entityType) {
        Log.debug("Getting kills for: " + entityType.toLowerCase() + "_kills");
        return this.playerStats.get(entityType.toLowerCase() + "_kills");
    }

    /*
    Setters for all statistics
     */
    public boolean setLevel(int level) {
        // If level provided is not an applicable level, cancel
        if (level < 1) { return false; }
        // If the level provided does not exceed the max, set the level
        if (level <= this.getMaxLevel()) {
            this.playerStats.replace("level", level);
            this.playerStats.replace("experience", (int)(Math.pow(this.playerStats.get("level"), 3.5) - 1));
            return true;
        } else {
            return false;
        }
    }
    public boolean setExperience(int experience) {
        // If experience provided is not valid, cancel
        if (experience < 0) { return false; }
        // If experience provided will put the player over the max level, set player to max level
        if (experience > getExperienceForLevel(getMaxLevel())) {
            this.playerStats.replace("level", getMaxLevel());
            this.playerStats.replace("experience", getExperienceForLevel(getMaxLevel()));
            return true;
        }
        this.playerStats.replace("experience", experience);
        updateLevel();
        return true;
    }
    public boolean setPrestige(int prestige) {
        // This will also set player's level to 1 and experience to 0 as prestiges are intended

        // If prestige given is not valid, cancel
        if (prestige < 0) { return false; }
        // If prestige provided will exceed max prestige, put player to max
        if (prestige > getMaxPrestige()) {
            this.playerStats.replace("prestige", getMaxPrestige());
            this.playerStats.replace("level", 1);
            this.playerStats.replace("experience", 0);
            return true;
        }
        this.playerStats.replace("prestige", prestige);
        this.playerStats.replace("level", 1);
        this.playerStats.replace("experience", 0);

        return true;
    }
    public boolean setGamesPlayed(int gamesPlayed) {
        // If games played is not valid, cancel
        if (gamesPlayed < 0) { return false; }
        this.playerStats.replace("games_played", gamesPlayed);
        return true;
    }
    public boolean setGamesWon(int gamesWon) {
        // If games won is not valid, cancel
        if (gamesWon < 0) { return false; }
        this.playerStats.replace("games_won", gamesWon);
        return true;
    }
    public boolean setTotalKills(int totalKills) {
        // If total kills is not valid, cancel
        if (totalKills < 0) { return false; }
        this.playerStats.replace("total_kills", totalKills);
        return true;
    }
    public boolean setZombieKills(int zombieKills) {
        if (zombieKills < 0) { return false; }
        this.playerStats.replace("zombie_kills", zombieKills);
        return true;
    }
    public boolean setSkeletonKills(int skeletonKills) {
        if (skeletonKills < 0) { return false; }
        this.playerStats.replace("skeleton_kills", skeletonKills);
        return true;
    }
    public boolean setSpiderKills(int spiderKills) {
        if (spiderKills < 0) { return false; }
        this.playerStats.replace("spider_kills", spiderKills);
        return true;
    }
    public boolean setPiglinBruteKills(int piglinBruteKills) {
        if (piglinBruteKills < 0) { return false; }
        this.playerStats.replace("piglin_brute_kills", piglinBruteKills);
        return true;
    }
    public boolean setZoglinKills(int zoglinKills) {
        if (zoglinKills < 0) { return false; }
        this.playerStats.replace("zoglin_kills", zoglinKills);
        return true;
    }
    public boolean setBlazeKills(int blazeKills) {
        if (blazeKills < 0) { return false; }
        this.playerStats.replace("blaze_kills", blazeKills);
        return true;
    }
    public boolean setWitherSkeletonKills(int witherSkeletonKills) {
        if (witherSkeletonKills < 0) { return false; }
        this.playerStats.replace("wither_skeleton_kills", witherSkeletonKills);
        return true;
    }
    public boolean setTotalBossKills(int totalBossKills) {
        if (totalBossKills < 0) { return false; }
        this.playerStats.replace("total_boss_kills", totalBossKills);
        return true;
    }
    public boolean setWave10BossKills(int wave10BossKills) {
        if (wave10BossKills < 0) { return false; }
        this.playerStats.replace("wave_10_boss_kills", wave10BossKills);
        return true;
    }
    public boolean setWave20BossKills(int wave20BossKills) {
        if (wave20BossKills < 0) { return false; }
        this.playerStats.replace("wave_20_boss_kills", wave20BossKills);
        return true;
    }
    public boolean setWave30BossKills(int wave30BossKills) {
        if (wave30BossKills < 0) { return false; }
        this.playerStats.replace("wave_30_boss_kills", wave30BossKills);
        return true;
    }
    public boolean setWave40BossKills(int wave40BossKills) {
        if (wave40BossKills < 0) { return false; }
        this.playerStats.replace("wave_40_boss_kills", wave40BossKills);
        return true;
    }
    public boolean setWave50BossKills(int wave50BossKills) {
        if (wave50BossKills < 0) { return false; }
        this.playerStats.replace("wave_50_boss_kills", wave50BossKills);
        return true;
    }
    public boolean setTotalBossDamage(int totalBossDamage) {
        if (totalBossDamage < 0) { return false; }
        this.playerStats.replace("total_boss_damage", totalBossDamage);
        return true;
    }
    public boolean setWave10BossDamage(int wave10BossDamage) {
        if (wave10BossDamage < 0) { return false; }
        this.playerStats.replace("wave_10_boss_damage", wave10BossDamage);
        return true;
    }
    public boolean setWave20BossDamage(int wave20BossDamage) {
        if (wave20BossDamage < 0) { return false; }
        this.playerStats.replace("wave_20_boss_damage", wave20BossDamage);
        return true;
    }
    public boolean setWave30BossDamage(int wave30BossDamage) {
        if (wave30BossDamage < 0) { return false; }
        this.playerStats.replace("wave_30_boss_damage", wave30BossDamage);
        return true;
    }
    public boolean setWave40BossDamage(int wave40BossDamage) {
        if (wave40BossDamage < 0) { return false; }
        this.playerStats.replace("wave_40_boss_damage", wave40BossDamage);
        return true;
    }
    public boolean setWave50BossDamage(int wave50BossDamage) {
        if (wave50BossDamage < 0) { return false; }
        this.playerStats.replace("wave_50_boss_damage", wave50BossDamage);
        return true;
    }

    /*
    Adders for all statistics
     */
    public boolean addLevel(int amount) {
        if (this.playerStats.get("level") + amount <= this.getMaxLevel()) {
            this.playerStats.replace("level", this.playerStats.get("level") + amount);
            this.playerStats.replace("experience", getExperienceForLevel(this.playerStats.get("level")));
            return true;
        }

        return false;
    }
    public boolean addExperience(int amount) {
        if (amount < 0) {
            return false;
        }

        this.playerStats.replace("experience", this.playerStats.get("experience") + amount);
        Log.debug("Adding " + amount + " EXP to UUID '" + this.uuid + "'. New experience: " + this.playerStats.get("experience") + ".");
        updateLevel();

        return true;
    }
    public boolean addPrestige(int prestige) {
        this.playerStats.replace("prestige", this.playerStats.get("prestige") + prestige);
        return true;
    }
    public boolean addGamesPlayed(int gamesPlayed) {
        this.playerStats.replace("games_played", this.playerStats.get("games_played") + gamesPlayed);
        return true;
    }
    public boolean addGamesWon(int gamesWon) {
        this.playerStats.replace("games_won", this.playerStats.get("games_won") + gamesWon);
        return true;
    }
    public void addTotalKills(int totalKills) {
        this.playerStats.replace("total_kills", this.playerStats.get("total_kills") + totalKills);
    }
    public boolean addZombieKills(int zombieKills) {
        this.playerStats.replace("zombie_kills", this.playerStats.get("zombie_kills") + zombieKills);
        return true;
    }
    public boolean addSkeletonKills(int skeletonKills) {
        this.playerStats.replace("skeleton_kills", this.playerStats.get("skeleton_kills") + skeletonKills);
        return true;
    }
    public boolean addSpiderKills(int spiderKills) {
        this.playerStats.replace("spider_kills", this.playerStats.get("spider_kills") + spiderKills);
        return true;
    }
    public boolean addPiglinBruteKills(int piglinBruteKills) {
        this.playerStats.replace("piglin_brute_kills", this.playerStats.get("piglin_brute_kills") + piglinBruteKills);
        return true;
    }
    public boolean addZoglinKills(int zoglinKills) {
        this.playerStats.replace("zoglin_kills", this.playerStats.get("zoglin_kills") + zoglinKills);
        return true;
    }
    public boolean addBlazeKills(int blazeKills) {
        this.playerStats.replace("blaze_kills", this.playerStats.get("blaze_kills") + blazeKills);
        return true;
    }
    public boolean addWitherSkeletonKills(int witherSkeletonKills) {
        this.playerStats.replace("wither_skeleton_kills", this.playerStats.get("wither_skeleton_kills") + witherSkeletonKills);
        return true;
    }
    public void addTotalBossKills(int totalBossKills) {
        this.playerStats.replace("total_boss_kills", this.playerStats.get("total_boss_kills") + totalBossKills);
    }
    public void addWave10BossKills(int wave10BossKills) {
        this.playerStats.replace("wave_10_boss_kills", this.playerStats.get("wave_10_boss_kills") + wave10BossKills);
    }
    public void addWave20BossKills(int wave20BossKills) {
        this.playerStats.replace("wave_20_boss_kills", this.playerStats.get("wave_20_boss_kills") + wave20BossKills);
    }
    public void addWave30BossKills(int wave30BossKills) {
        this.playerStats.replace("wave_30_boss_kills", this.playerStats.get("wave_30_boss_kills") + wave30BossKills);
    }
    public void addWave40BossKills(int wave40BossKills) {
        this.playerStats.replace("wave_40_boss_kills", this.playerStats.get("wave_40_boss_kills") + wave40BossKills);
    }
    public void addWave50BossKills(int wave50BossKills) {
        this.playerStats.replace("wave_50_boss_kills", this.playerStats.get("wave_50_boss_kills") + wave50BossKills);
    }
    public void addTotalBossDamage(int totalBossDamage) {
        this.playerStats.replace("total_boss_damage", this.playerStats.get("total_boss_damage") + totalBossDamage);
    }
    public void addWave10BossDamage(int wave10BossDamage) {
        this.playerStats.replace("wave_10_boss_damage", this.playerStats.get("wave_10_boss_damage") + wave10BossDamage);
    }
    public void addWave20BossDamage(int wave20BossDamage) {
        this.playerStats.replace("wave_20_boss_damage", this.playerStats.get("wave_20_boss_damage") + wave20BossDamage);
    }
    public void addWave30BossDamage(int wave30BossDamage) {
        this.playerStats.replace("wave_30_boss_damage", this.playerStats.get("wave_30_boss_damage") + wave30BossDamage);
    }
    public void addWave40BossDamage(int wave40BossDamage) {
        this.playerStats.replace("wave_40_boss_damage", this.playerStats.get("wave_40_boss_damage") + wave40BossDamage);
    }
    public void addWave50BossDamage(int wave50BossDamage) {
        this.playerStats.replace("wave_50_boss_damage", this.playerStats.get("wave_50_boss_damage") + wave50BossDamage);
    }
    public void addGenericMobKills(String entityType, int mobKills) {
        this.playerStats.replace(entityType + "_kills", getGenericMobKills(entityType.toLowerCase()) + mobKills);
    }
    public void addGenericBossDamage(int damage, int wave) {
        this.playerStats.replace("wave_" + wave + "_boss_damage", this.playerStats.get("wave_" + wave + "_boss_damage") + damage);
    }

    /*
    Gets the max level and prestige from the config
    If no value is found, use 100 as default
     */
    public int getMaxLevel() {
        return ZombieArena.getInstance().getFileManager().get("config").get().getConfiguration().getInt("max-level", 100);
    }
    public int getMaxPrestige() {
        return ZombieArena.getInstance().getFileManager().get("config").get().getConfiguration().getInt("max-prestige", 100);
    }

    /*
    Updates the level according to the experience
     */
    private void updateLevel() {
        int maxLevel = getMaxLevel();

        for (int i = maxLevel; i > 0; i--) {
            long exp = (long) (Math.pow(i, 3.5) - 1);

            if (this.playerStats.get("experience") > exp) {
                if (this.playerStats.get("level") != i) {
                    this.playerStats.replace("level", i);
                }
                return;
            }
        }
    }

    /*
    Returns the amount of experience required for the next level
     */
    public int getExperienceForNextLevel() { return getExperienceForLevel(this.playerStats.get("level") + 1); }

    /*
    Returns the amount of experience required for the specified level
     */
    public int getExperienceForLevel(int level) { return (int) Math.pow(level, 3.5); }

    /*
    Gets the PlayerWrapper for a give Player object
     */
    public static PlayerWrapper get(Player player) { return get(player.getUniqueId()); }

    /*
    Gets the PlayerWrapper for the player's UUID
     */
    public static PlayerWrapper get(UUID uuid) {
        if (all.containsKey(uuid)) {
            return all.get(uuid);
        } else {
            return new PlayerWrapper(uuid);
        }
    }

    /*
    Removes the wrapper from the PlayerWrapper list given a Player object
     */
    public static void remove(Player player) {
        remove(player.getUniqueId());
    }

    /*
    Removes the wrapper from the PlayerWrapper list given a player's UUID
     */
    public static void remove(UUID uuid) {
        Log.debug("Removing player");
        if (all.containsKey(uuid)) {
            all.get(uuid).save();
            all.remove(uuid);
        }
    }

    /*
    Saves all player data.
     */
    public static void saveAll() {
        for (PlayerWrapper wrapper : all.values()) {
            wrapper.save();
        }
    }
}
