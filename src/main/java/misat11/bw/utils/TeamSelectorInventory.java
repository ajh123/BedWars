package misat11.bw.utils;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import misat11.bw.Main;
import misat11.bw.game.Game;
import misat11.bw.game.Team;
import misat11.bw.game.TeamColor;

public class TeamSelectorInventory implements Listener {

	private Inventory inv;
	private Main plugin;
	private Game game;

	public TeamSelectorInventory(Main plugin, Game game) {
		this.plugin = plugin;
		this.game = game;
		int slotCount = 9;
		if (game.getTeams().size() > 9) {
			slotCount = 18;
		}
		if (game.getTeams().size() > 18) {
			slotCount = 54; // What??? There are more than 18 teams?
		}

		inv = Bukkit.getServer().createInventory(null, slotCount, "[BW] Teams: " + game.getName());

		for (Team team : game.getTeams()) {
			ItemStack teamStack = materializeColorToWool(team.color);
			ItemMeta im = teamStack.getItemMeta();
			im.setDisplayName(team.color.chatColor + team.name);
			im.setLore(Arrays.asList(I18n._("click_to_join_team", false)));
			teamStack.setItemMeta(im);
			inv.addItem(teamStack);
		}

		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public static ItemStack materializeColorToWool(TeamColor color) {
		return new ItemStack(color.material);
	}

	public void destroy() {
		HandlerList.unregisterAll(this);
		inv.clear();
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (!e.getInventory().equals(inv))
			return;
		e.setCancelled(true);
		if (!(e.getWhoClicked() instanceof Player)) {
			e.getWhoClicked().closeInventory();
			return; // How this happened?
		}
		Player player = (Player) e.getWhoClicked();
		if (!Main.isPlayerInGame(player)) {
			player.closeInventory();
			return;
		}
		game.selectTeam(Main.getPlayerGameProfile(player), e.getCurrentItem().getItemMeta().getDisplayName());
	}

	public void openForPlayer(Player player) {
		player.openInventory(inv);
	}

}
