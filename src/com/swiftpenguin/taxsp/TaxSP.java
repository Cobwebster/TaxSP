package com.swiftpenguin.taxsp;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class TaxSP extends JavaPlugin implements Listener {

    public static Economy econ = null;

    @Override
    public void onEnable() {

        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        registerConfig();
        //getServer().getPluginManager().registerEvents(new ModeTwo(this), this);

        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    public void registerConfig() {
        saveDefaultConfig();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("Tax") && sender instanceof Player) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.GOLD + "§m---------------------------------------------");
                sender.sendMessage(ChatColor.GOLD + "§lTax Info");
                sender.sendMessage(ChatColor.GOLD + "");
                sender.sendMessage(ChatColor.YELLOW + "ECO > " + ChatColor.GREEN + "$" + getConfig().getInt("Total") + ChatColor.RED + " has been paid in taxes globally.");
                sender.sendMessage(ChatColor.YELLOW + "Cost > " + ChatColor.RED + "10k per 1m");
                sender.sendMessage(ChatColor.YELLOW + "Requirement > " + ChatColor.RED + "Bank balance of 1m or more");
                sender.sendMessage(ChatColor.YELLOW + "Tax Dates > " + ChatColor.RED + "Once a month");
                sender.sendMessage(ChatColor.GOLD + "§m---------------------------------------------");

            } else if (args[0].length() > 0 && args[0].equalsIgnoreCase(("reset")) && sender.hasPermission("taxsp.reset")) {

                sender.sendMessage(ChatColor.GREEN + "Tax Data reset... People who meet bankRequirement will now get taxed...");
                getConfig().set("Player.Data", null);
                getConfig().set("Total", 0);
                getConfig().addDefault("Player.Data.43a61dc7-7b99-4381-a06a-2bc16ff69d5d", 0);
                getConfig().options().copyDefaults(true);
                saveConfig();
            }
        }
        return true;
    }

    @EventHandler
    public void taxCheck(PlayerJoinEvent e) {

        Player player = e.getPlayer();
        String playeruuid = player.getUniqueId().toString();

        if (getConfig().getConfigurationSection("Player.Data").getKeys(false).contains(playeruuid)) {
            return;
        }

        double balance = econ.getBalance(player);
        double calc = balance / 1000000;
        double tax = Math.ceil(calc) * 10000;
        int bankRequirement = getConfig().getInt("Settings.bankRequirement");

        if (balance >= bankRequirement) {

                EconomyResponse r = econ.withdrawPlayer(player, tax);
                int totalformat = (int) tax;

                if (r.transactionSuccess()) {

                    player.sendMessage(String.format(ChatColor.GOLD + "You paid %s tax and now have %s", econ.format(r.amount), econ.format(r.balance)));
                    player.sendMessage(ChatColor.GOLD + "We charge 10k per every 1m once a month.");

                    int total = getConfig().getInt("Total");
                    int totalnew = total + totalformat;

                    getConfig().set("Player.Data." + playeruuid, totalformat);
                    getConfig().options().copyDefaults(true);
                    getConfig().set("Total", totalnew);
                    saveConfig();
                }
            }
        }
}

    /*
//1% format

       @EventHandler
    public void taxCheck(PlayerJoinEvent e) {

        Player player = e.getPlayer();
        String playeruuid = player.getUniqueId().toString();

        if (getConfig().getConfigurationSection("Player.Data").getKeys(false).contains(playeruuid)) {
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

                    int total = getConfig().getInt("Total");
                    int totalnew = total + totalformat;

                    getConfig().set("Player.Data." + playeruuid, totalformat);
                    getConfig().options().copyDefaults(true);
                    getConfig().set("Total", totalnew);
                    saveConfig();

                }
            }
        }
    }

     */