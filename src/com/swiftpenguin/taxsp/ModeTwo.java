/*package com.swiftpenguin.taxsp;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

public class ModeTwo implements Listener{

    private TaxSP plugin;

    public ModeTwo(TaxSP plugin) {
        this.plugin = plugin;
    }

    public static Economy econ = null;

    private boolean setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }


    @EventHandler
    public void taxCheck(PlayerJoinEvent e) {

        Player player = e.getPlayer();
        String playeruuid = player.getUniqueId().toString();

        if (plugin.getConfig().getConfigurationSection("Player.Data").getKeys(false).contains(playeruuid)) {
            return;
        }

        double bal = econ.getBalance(player);
        double b = 1;
        double percentage = bal * b / 100;
        int totalformat = (int) percentage;

        if (bal >= 25000000) {

            EconomyResponse r = econ.withdrawPlayer(player, percentage);

            if (r.transactionSuccess()) {

                player.sendMessage(String.format(ChatColor.GOLD + "You paid %s tax and now have %s", econ.format(r.amount), econ.format(r.balance)));

                int total = plugin.getConfig().getInt("Total");
                int totalnew = total + totalformat;

                plugin.getConfig().set("Player.Data." + playeruuid, totalformat);
                plugin.getConfig().options().copyDefaults(true);
                plugin.getConfig().set("Total", totalnew);
                plugin.saveConfig();

            }
        }
    }
}*/
