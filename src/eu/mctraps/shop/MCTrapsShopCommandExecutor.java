/*     */ package eu.mctraps.shop;
/*     */ 
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandExecutor;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ 
/*     */ public class MCTrapsShopCommandExecutor implements CommandExecutor
/*     */ {
/*     */   private final MCTrapsShop plugin;
/*     */   
/*     */   public MCTrapsShopCommandExecutor(MCTrapsShop plugin)
/*     */   {
/*  20 */     this.plugin = plugin;
/*     */   }
/*     */   
/*     */   public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
/*     */   {
/*  25 */     if (cmd.getName().equalsIgnoreCase("smsshop")) {
/*  26 */       if (args.length == 0) {
/*  27 */         sender.sendMessage("§7Glowne komendy pluginu:");
/*  28 */         sender.sendMessage(" §7/voucher §6<list;add;remove>");
/*     */         
/*  30 */         return true;
/*     */       }
/*  32 */       return false;
/*     */     }
/*  34 */     if (cmd.getName().equalsIgnoreCase("voucher")) {
/*  35 */       if ((!(sender instanceof org.bukkit.entity.Player)) || (sender.hasPermission("tools.voucher"))) {
/*  36 */         String vTable = this.plugin.config.getString("tables.vouchers");
/*  37 */         String oTable = this.plugin.config.getString("tables.offers");
/*  38 */         String hTable = this.plugin.config.getString("tables.history");
/*     */         
/*  40 */         if (args.length == 0) {
/*  41 */           return false;
/*     */         }
/*  43 */         if (args[0].equalsIgnoreCase("list")) {
/*  44 */           if (args.length == 1) {
/*     */             try {
/*  46 */               ResultSet result = this.plugin.statement.executeQuery("SELECT * FROM " + vTable);
/*  47 */               List<String> vouchers = new ArrayList();
/*  48 */               while (result.next()) {
/*  49 */                 String code = result.getString("code");
/*  50 */                 int uses = result.getInt("uses");
/*  51 */                 int id = result.getInt("id");
/*  52 */                 String voucher = " §a" + code + " (#" + id + ")";
/*  53 */                 vouchers.add(voucher);
/*     */               }
/*     */               
/*  56 */               sender.sendMessage("§7Lista aktualnych voucherów:");
/*  57 */               for (int i = 0; i < vouchers.size(); i++) {
/*  58 */                 sender.sendMessage((String)vouchers.get(i));
/*     */               }
/*  60 */               sender.sendMessage("§9Aby dowiedziec sie wiecej o danym voucherze, uzyj komendy: §7/voucher info §6<id>");
/*     */             } catch (SQLException e) {
/*  62 */               e.printStackTrace();
/*  63 */               sender.sendMessage("§cWystapil blad podczas laczenia z baza danych");
/*     */             }
/*     */             
/*  66 */             return true;
/*     */           }
/*  68 */           sender.sendMessage("§cPoprawne uzycie: §7/voucher list");
/*  69 */           return true;
/*     */         }
/*  71 */         if ((args[0].equalsIgnoreCase("info")) && 
/*  72 */           (args.length == 2)) {
/*     */           try {
/*  74 */             ResultSet result = this.plugin.statement.executeQuery("SELECT * FROM " + vTable + " WHERE id = '" + args[1] + "' ORDER BY uses DESC LIMIT 1");
/*  75 */             int id = 0;int uses = 0;int offerid = 0;int timed = 0;
/*  76 */             String code = "";String timedText = "";String end = "";
/*  77 */             while (result.next()) {
/*  78 */               id = result.getInt("id");
/*  79 */               code = result.getString("code");
/*  80 */               uses = result.getInt("uses");
/*  81 */               offerid = result.getInt("offer");
/*  82 */               timed = result.getInt("timed");
/*  83 */               timedText = timed == 1 ? "tak" : "nie";
/*  84 */               end = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(result.getTimestamp("endtime"));
/*     */             }
/*     */             
/*  87 */             ResultSet offerResult = this.plugin.statement.executeQuery("SELECT * FROM " + oTable + " WHERE id = '" + offerid + "' LIMIT 1");
/*  88 */             String offername = "Brak usługi o takim ID";
/*  89 */             while (offerResult.next()) {
/*  90 */               offername = offerResult.getString("name");
/*     */             }
/*     */             
/*  93 */             sender.sendMessage("§7Informacje o voucherze §6#" + id);
/*  94 */             sender.sendMessage(" §9Kod: §7" + code);
/*  95 */             sender.sendMessage(" §9Pozostale uzycia: §7" + uses);
/*  96 */             sender.sendMessage(" §9Kod do uslugi: §7#" + offerid + " §7(§c" + offername + "§7)");
/*  97 */             sender.sendMessage(" §9Oferta ograniczona czasowo?: §7" + timedText);
/*  98 */             if (timed == 1) {
/*  99 */               sender.sendMessage(" §9Oferta wazna do: §7" + end);
/*     */             }
/*     */             
/* 102 */             return true;
/*     */           } catch (SQLException e) {
/* 104 */             e.printStackTrace();
/* 105 */             sender.sendMessage("§cWystapil blad w trakcie laczenia z baza danych");
/* 106 */             return true;
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 111 */         sender.sendMessage("§cNie masz dostepu! :(");
/*     */       }
/*     */     }
/*     */     
/* 115 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Miłosz\Desktop\plgs\MCTrapsShop.jar!\eu\mctraps\shop\MCTrapsShopCommandExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
