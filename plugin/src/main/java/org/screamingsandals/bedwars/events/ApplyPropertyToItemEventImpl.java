package org.screamingsandals.bedwars.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.bedwars.api.events.ApplyPropertyToItemEvent;
import org.screamingsandals.bedwars.game.Game;
import org.screamingsandals.bedwars.player.BedWarsPlayer;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.builder.ItemFactory;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ApplyPropertyToItemEventImpl extends AbstractEvent implements ApplyPropertyToItemEvent<Game, BedWarsPlayer, Item> {
    private final Game game;
    private final BedWarsPlayer player;
    private final String propertyName;
    private final Map<String, Object> properties;
    private Item stack;

    @Override
    public void setStack(Object stack) {
        if (stack instanceof Item) {
            this.stack = (Item) stack;
        } else {
            this.stack = ItemFactory.build(stack).orElseThrow();
        }
    }
}
