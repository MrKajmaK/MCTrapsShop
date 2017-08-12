package eu.mctraps.shop;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util.Platform;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MCTrapsShopCommandExecutor implements CommandExecutor {
    private final MCTrapsShop plugin;

    public MCTrapsShopCommandExecutor(MCTrapsShop plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("smsshop")) {
            if(args.length == 0) {
                sender.sendMessage("§c__-- §6Komendy pluginu: §c--__");
                sender.sendMessage("  §7- §9/voucher <list|add|remove>");

                return true;
            } else {
                return false;
            }
        } else if(cmd.getName().equalsIgnoreCase("voucher")) {
            if(!(sender instanceof Player) || sender.hasPermission("smsshop.voucher")) {
                String vTable = plugin.config.getString("tables.vouchers");
                String oTable = plugin.config.getString("tables.offers");
                String hTable = plugin.config.getString("tables.history");

                if (args.length == 0) {
                    return false;
                }
                if (args[0].equalsIgnoreCase("list")) {
                    if (args.length == 1) {
                        try {
                            ResultSet result = plugin.statement.executeQuery("SELECT * FROM " + vTable);
                            List<String> vouchers = new ArrayList<>();
                            while (result.next()) {
                                String code = result.getString("code");
                                int uses = result.getInt("uses");
                                int id = result.getInt("id");
                                String voucher = (uses == 0) ? "  §7- " + code + " (#" + id + ")" : "  §7- §2" + code + " (#" + id + ")";
                                vouchers.add(voucher);
                            }

                            sender.sendMessage("§c__-- §6Lista voucherów: §c--__");
                            for (int i = 0; i < vouchers.size(); i++) {
                                sender.sendMessage(vouchers.get(i));
                            }
                            sender.sendMessage("§c__-- §dAby dowiedzieć się więcej o danym voucherze, użyj /voucher info <id> §c--__");
                        } catch (SQLException e) {
                            e.printStackTrace();
                            sender.sendMessage("§cWystąpił błąd w trakcie łączenia z bazą danych");
                        }

                        return true;
                    } else {
                        sender.sendMessage("§cPoporawny sposób użycia: §6/voucher list");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("info")) {
                    if (args.length == 2) {
                        try {
                            ResultSet result = plugin.statement.executeQuery("SELECT * FROM " + vTable + " WHERE id = " + args[1] + " LIMIT 1");
                            int id = result.getInt("id");
                            String code = result.getString("code");
                            int uses = result.getInt("uses");
                            int offerid = result.getInt("offer");
                            int timed = result.getInt("timed");
                            String timedText = (timed == 1) ? "tak" : "nie";
                            String end = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(result.getTimestamp("endtime"));

                            ResultSet offerResult = plugin.statement.executeQuery("SELECT * FROM " + oTable + " WHERE id = " + offerid + " LIMIT 1");
                            String offername;
                            if (!offerResult.next()) {
                                offername = "Brak usługi o takim ID";
                            } else {
                                offername = offerResult.getString("name");
                            }

                            sender.sendMessage("§c__-- §6Informacje o voucherze #" + id + " §c--__");
                            sender.sendMessage("§6Kod: §2" + code);
                            sender.sendMessage("§6Pozostałe użycia: §2" + uses);
                            sender.sendMessage("§6Kod do usługi: §2" + offerid + " §c(§9" + offername + "§c)");
                            sender.sendMessage("§6Oferta ograniczona czasowo?: §2" + timedText);
                            if (timed == 1) {
                                sender.sendMessage("§6Oferta ważna do: §2" + end);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            sender.sendMessage("§cWystąpił błąd w trakcie łączenia z bazą danych");
                        }
                    }
                }
            } else {
                sender.sendMessage("§cNie masz uprawnień :(");
            }
        }

        return false;
    }
}
