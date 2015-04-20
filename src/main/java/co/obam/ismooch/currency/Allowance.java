package co.obam.ismooch.currency;


import co.obam.ismooch.obamapi.ObamAPI;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Allowance implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        ObamAPI.openConnection();
        try {
            PreparedStatement sql =
                    ObamAPI.connection.prepareStatement("SELECT * FROM playerInfo WHERE uuid = ?");
            sql.setString(1, uuid.toString());
            ResultSet rs = sql.executeQuery();
            if (rs.next()) {

                Date date = new Date();
                Timestamp now = new Timestamp(date.getTime());
                Timestamp check = rs.getTimestamp("LogCheck");
                if (check == null || computeDiff(check, now).get(TimeUnit.DAYS) >= 1) {
                    Double bonus = 715.0;
                    if (e.getPlayer().hasPermission("obam.supp1")) {

                        bonus = bonus + 358;

                    }
                    if (e.getPlayer().hasPermission("obam.plus2")) {

                        bonus = bonus + 358;

                    }
                    if (e.getPlayer().hasPermission("obam.ult4")) {

                        bonus = bonus + 358;

                    }
                    ObamAPI.addTickets(uuid, bonus, "OBAM Allowance", "Daily Login Bonus");
                    e.getPlayer().sendRawMessage(
                            ChatColor.GREEN + "You have received your daily log in bonus of " + ChatColor.YELLOW +
                                    bonus + " Tickets" + ChatColor.GREEN + "!");
                    e.getPlayer().sendRawMessage(ChatColor.GREEN + "Your current balance is " + ChatColor.YELLOW +
                            ObamAPI.getTickets(e.getPlayer().getUniqueId()) + " Tickets" + ChatColor.GREEN + "!");
                    e.getPlayer().sendRawMessage(ChatColor.GREEN + "You have a login streak of " + ChatColor.YELLOW +
                            (rs.getInt("DayCount") + 1) + " Days" + ChatColor.GREEN + " and counting!");
                    ObamAPI.openConnection();
                    if (check == null || computeDiff(check, now).get(TimeUnit.DAYS) < 2) {

                        PreparedStatement update =
                                ObamAPI.connection.prepareStatement("UPDATE playerInfo SET LogCheck = CURRENT_TIMESTAMP, DayCount = DayCount + 1 WHERE uuid = ?");
                        update.setString(1, uuid.toString());
                        update.executeUpdate();

                    } else {

                        PreparedStatement update =
                                ObamAPI.connection.prepareStatement("UPDATE playerinfo SET LogCheck = CURRENT_TIMESTAMP, DayCount = 0 WHERE uuid = ?");
                        update.executeUpdate();
                    }


                } else {

                    Map<TimeUnit, Long> comp = computeDiff(check, now);
                    e.getPlayer().sendRawMessage(ChatColor.GREEN + "Your next sign in bonus is in " + ChatColor.YELLOW +
                            (24 - comp.get(TimeUnit.HOURS)) + " Hours" + ChatColor.GREEN + " and " + ChatColor.YELLOW +
                            (60 - comp.get(TimeUnit.MINUTES)) + " Minutes" + ChatColor.GREEN + "!");

                }

            }
        } catch (SQLException el) {

            el.printStackTrace();
        } finally {

            ObamAPI.closeConnection();
        }


    }


    public static Map<TimeUnit, Long> computeDiff(Date date1, Date date2) {
        long diffInMillies = date2.getTime() - date1.getTime();
        List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);

        Map<TimeUnit, Long> result = new LinkedHashMap<TimeUnit, Long>();
        long milliesRest = diffInMillies;
        for (TimeUnit unit : units) {
            long diff = unit.convert(milliesRest, TimeUnit.MILLISECONDS);
            long diffInMilliesForUnit = unit.toMillis(diff);
            milliesRest = milliesRest - diffInMilliesForUnit;
            result.put(unit, diff);
        }
        return result;
    }


}
