package co.obam.ismooch.currency;

import co.obam.ismooch.obamapi.ObamAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class TAM implements Listener {

    public static Map<Player, Double> sessionTam = new HashMap<Player, Double>();


    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {

        if (sessionTam.containsKey(e.getPlayer())) {

            sessionTam.remove(e.getPlayer());

        }

    }

    public static void startTam(Plugin plugin) {

        System.out.println("[OBAM Currency] TAM Initialized");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {

                System.out.println("[OBAM Currency] TAM Execution");
                double add = (double) getTam();
                for (Player player : Bukkit.getOnlinePlayers()) {

                    if (player.hasPermission("obam.ult4")) {

                        add = add + 1;

                    } else if (player.hasPermission("obam.plus2")) {

                        add = add + 0.5;

                    } else if (player.hasPermission("obam.supp1")) {

                        add = add + 0.25;

                    }

                }

                for (Player player : Bukkit.getOnlinePlayers()) {

                    if (getTamTotal(player) == -1) {

                        newTamPlayer(player);

                    }
                    ObamAPI.addTickets(player.getUniqueId(), add, "TAM", "TAM Deposit");
                    addTamTotal(player, add);
                    if (sessionTam.containsKey(player)) {

                        sessionTam.put(player, sessionTam.get(player) + add);

                    } else {

                        sessionTam.put(player, add);
                    }

                }


            }
        }, 0L, 1200L);

    }

    public static int getTam() {

        ObamAPI.openConnection();
        try {
            PreparedStatement sql = ObamAPI.connection.prepareStatement("SELECT * FROM MiscVar WHERE Name = 'CurTam'");
            ResultSet rs = sql.executeQuery();
            if (rs.next()) {

                return rs.getInt("NumValue");

            }
        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            ObamAPI.closeConnection();
        }

        return 1;

    }

    public static void setTam(int set) {

        ObamAPI.openConnection();
        try {

            PreparedStatement sql =
                    ObamAPI.connection.prepareStatement("UPDATE MiscVar SET NumValue = ? WHERE Name = 'CurTam'");
            sql.setInt(1, set);
            sql.executeUpdate();


        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            ObamAPI.closeConnection();

        }

    }

    public static void newTamPlayer(Player player) {

        ObamAPI.openConnection();
        try {

            PreparedStatement sql =
                    ObamAPI.connection.prepareStatement("INSERT INTO TAM_Stats (UUID, TotalEarned) Values (?, 0)");
            sql.setString(1, player.getUniqueId().toString());
            sql.executeUpdate();

        } catch (SQLException e) {

            e.printStackTrace();
        } finally {

            ObamAPI.closeConnection();

        }

    }

    public static void addTamTotal(Player player, double add) {

        ObamAPI.openConnection();
        try {

            PreparedStatement sql =
                    ObamAPI.connection.prepareStatement("UPDATE TAM_Stats SET TotalEarned = TotalEarned + ? WHERE UUID = ?");
            sql.setDouble(1, add);
            sql.setString(2, player.getUniqueId().toString());
            sql.executeUpdate();

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            ObamAPI.closeConnection();

        }

    }

    public static double getTamTotal(Player player) {

        ObamAPI.openConnection();
        try {

            PreparedStatement sql = ObamAPI.connection.prepareStatement("SELECT * FROM TAM_Stats WHERE UUID = ?");
            sql.setString(1, player.getUniqueId().toString());
            ResultSet rs = sql.executeQuery();
            if (rs.next()) {

                return rs.getDouble("TotalEarned");

            }
            return -1;

        } catch (SQLException e) {

            e.printStackTrace();
            return -1;

        } finally {

            ObamAPI.closeConnection();

        }

    }

    public static double getBoost() {

        double add = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {


            if (player.hasPermission("obam.ult4")) {

                add = add + 1;

            } else if (player.hasPermission("obam.plus2")) {

                add = add + 0.5;

            } else if (player.hasPermission("obam.supp1")) {

                add = add + 0.25;

            }

        }
        return add;

    }
}
