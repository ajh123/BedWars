package misat11.bw.commands;

import misat11.bw.Main;
import misat11.bw.statistics.PlayerStatistic;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static misat11.lib.lang.I18n.i18n;

public class StatsCommand extends BaseCommand {

    public StatsCommand() {
        super("stats", null, true);
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) {
        if (!Main.isPlayerStatisticsEnabled()) {
            sender.sendMessage(i18n("statistics_is_disabled"));
        } else {
            if (args.size() >= 1) {
                if (!sender.hasPermission(OTHER_STATS_PERMISSION) && !sender.hasPermission(ADMIN_PERMISSION)) {
                    sender.sendMessage(i18n("no_permissions"));
                } else {
                    String name = args.get(0);
                    OfflinePlayer off = Main.getInstance().getServer().getPlayerExact(name);

                    if (off == null) {
                        sender.sendMessage(i18n("statistics_player_is_not_exists"));
                    } else {
                        PlayerStatistic statistic = Main.getPlayerStatisticsManager().getStatistic(off);
                        if (statistic == null) {
                            sender.sendMessage(i18n("statistics_not_found"));
                        } else {
                            this.sendStats(sender, statistic);
                        }
                    }
                }
            } else {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    PlayerStatistic statistic = Main.getPlayerStatisticsManager().getStatistic(player);
                    if (statistic == null) {
                        player.sendMessage(i18n("statistics_not_found"));
                    } else {
                        this.sendStats(player, statistic);
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void completeTab(List<String> completion, CommandSender sender, List<String> args) {
        if (args.size() == 1 && Main.isPlayerStatisticsEnabled()
                && (sender.hasPermission(OTHER_STATS_PERMISSION) || sender.hasPermission(ADMIN_PERMISSION))) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                completion.add(p.getName());
            }
        }
    }

    private void sendStats(CommandSender player, PlayerStatistic statistic) {
        player.sendMessage(i18n("statistics_header").replace("%player%", statistic.getName()));

        player.sendMessage(i18n("statistics_kills", false).replace("%kills%",
                Integer.toString(statistic.getKills() + statistic.getCurrentKills())));
        player.sendMessage(i18n("statistics_deaths", false).replace("%deaths%",
                Integer.toString(statistic.getDeaths() + statistic.getCurrentDeaths())));
        player.sendMessage(i18n("statistics_kd", false).replace("%kd%",
                Double.toString(statistic.getKD() + statistic.getCurrentKD())));
        player.sendMessage(i18n("statistics_wins", false).replace("%wins%",
                Integer.toString(statistic.getWins() + statistic.getCurrentWins())));
        player.sendMessage(i18n("statistics_loses", false).replace("%loses%",
                Integer.toString(statistic.getLoses() + statistic.getCurrentLoses())));
        player.sendMessage(i18n("statistics_games", false).replace("%games%",
                Integer.toString(statistic.getGames() + statistic.getCurrentGames())));
        player.sendMessage(i18n("statistics_beds", false).replace("%beds%",
                Integer.toString(statistic.getDestroyedBeds() + statistic.getCurrentDestroyedBeds())));
        player.sendMessage(i18n("statistics_score", false).replace("%score%",
                Integer.toString(statistic.getScore() + statistic.getCurrentScore())));
    }

}
