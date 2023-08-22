package me.Visionexe.ZombieArena.Game;

import com.sk89q.worldedit.math.BlockVector3;
import me.Visionexe.ZombieArena.Storage.Flatfile.Config;
import me.Visionexe.ZombieArena.Storage.Flatfile.FlatfileAPI;
import me.Visionexe.ZombieArena.ZombieArena;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Arena {
    /*
        This will use WorldEdit selections to create the arena and log each arena in its own yml file
        Will need to store:
        1. Name
        2. Pos 1 and Pos 2 of arena
        3. Player Spawn point
        4. Mob spawn points

        Will need to have getters and setters for all the data

        Need to find how to pull WorldEdit information in so arena can be created
        Will also need to make commands to create arena
    */
    private FlatfileAPI flatfile;
    private String arenaName = "";
    private BlockVector3 pos1;
    private BlockVector3 pos2;
    private Location playerSpawnPoint;
    private List<Location> mobSpawnPoints = new ArrayList<>();

    public Arena(String arenaName, BlockVector3 pos1, BlockVector3 pos2) {
        this.arenaName = arenaName;
        this.pos1 = pos1;
        this.pos2 = pos2;

        this.flatfile = ZombieArena.getInstance().getFlatfile();
//        if (!(this.flatfile.get("arena." + arenaName).contains(arenaName))) {
            this.flatfile.add(new Config("arena." + arenaName, "", "genericarena.yml", "arenas/", arenaName + ".yml"));
            this.flatfile.get("arena." + arenaName).set("ArenaName", arenaName);

            this.flatfile.get("arena." + arenaName).set("Position1.x", pos1.getX());
            this.flatfile.get("arena." + arenaName).set("Position1.y", pos1.getY());
            this.flatfile.get("arena." + arenaName).set("Position1.z", pos1.getZ());

            this.flatfile.get("arena." + arenaName).set("Position2.x", pos2.getX());
            this.flatfile.get("arena." + arenaName).set("Position2.y", pos2.getY());
            this.flatfile.get("arena." + arenaName).set("Position2.z", pos2.getZ());

            this.flatfile.get("arena." + arenaName).set("PlayerSpawn.x", 0);
            this.flatfile.get("arena." + arenaName).set("PlayerSpawn.y", 0);
            this.flatfile.get("arena." + arenaName).set("PlayerSpawn.z", 0);

            this.flatfile.get("arena." + arenaName).set("MobSpawns", mobSpawnPoints);

            this.flatfile.save();
//        }
    }

    /*
    Getters for all Arena values
     */
    public String getArenaName() {
        return arenaName;
    }
    public BlockVector3 getPos1() {
        return pos1;
    }
    public BlockVector3 getPos2() {
        return pos2;
    }
    public Location getPlayerSpawnPoint() {
        return playerSpawnPoint;
    }
    public List<Location> getMobSpawnPoints() {
        return mobSpawnPoints;
    }

    /*
    Setters for all Arena values
     */
    public void setArenaName(String arenaName) {
        this.arenaName = arenaName;
    }
    public void setPos1(BlockVector3 pos1) {
        this.pos1 = pos1;
    }
    public void setPos2(BlockVector3 pos2) {
        this.pos2 = pos2;
    }
    public void setPlayerSpawnPoint(Location playerSpawnPoint) {
        this.playerSpawnPoint = playerSpawnPoint;
    }
    public void setMobSpawnPoints(List<Location> mobSpawnPoints) {
        this.mobSpawnPoints = mobSpawnPoints;
    }

    // Since mobSpawnPoints is a List of Locations, specifically have a function to add to the list
    public void addMobSpawnPoint(Location mobSpawnPoint) {
        this.mobSpawnPoints.add(mobSpawnPoint);
    }
}
