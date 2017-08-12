package eu.mctraps.shop;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
                sender.sendMessage("§7__-- §6Komendy pluginu: §7--__");
                sender.sendMessage("  §7- &9/voucher <list|add|remove>");

                return true;
            } else {
                return false;
            }
        } else if(cmd.getName().equalsIgnoreCase("voucher")) {
            if(args.length == 0) {
                return false;
            } else if(args.length == 1) {
                if(args[0].equalsIgnoreCase("list")) {
                    try {
                        ResultSet result = plugin.statement.executeQuery("SELECT * FROM " + plugin.config.getString("database.table"));
                        List<String> vouchers = new ArrayList<>();
                        while(result.next()) {
                            String code = result.getString("code");
                            int uses = result.getInt("uses");
                            int id = result.getInt("id");
                            String voucher = (uses == 0) ? "  §7" + code + " (#" + id + ")" : "  §2" + code + " (#" + id + ")";
                            vouchers.add(voucher);
                        }

                        sender.sendMessage("§7__-- §6Lista voucherów: §7--__");
                        for(int i = 0; i < vouchers.size(); i++) {
                            sender.sendMessage(vouchers.get(i));
                        }
                        sender.sendMessage("§7__-- §dAby dowiedzieć się więcej o danym voucherze, użyj /voucher info <id> §7--__");
                    } catch (SQLException e) {
                        e.printStackTrace();
                        sender.sendMessage("§cWystąpił błąd w trakcie łączenia z bazą danych");
                    }

                    return true;
                }
            }
        }

        return false;
    }
}
