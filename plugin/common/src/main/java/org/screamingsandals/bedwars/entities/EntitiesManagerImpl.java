/*
 * Copyright (C) 2022 ScreamingSandals
 *
 * This file is part of Screaming BedWars.
 *
 * Screaming BedWars is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Screaming BedWars is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Screaming BedWars. If not, see <https://www.gnu.org/licenses/>.
 */

package org.screamingsandals.bedwars.entities;

import org.screamingsandals.bedwars.api.entities.EntitiesManager;
import org.screamingsandals.bedwars.api.entities.GameEntity;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.game.GameImpl;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.plugin.ServiceManager;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EntitiesManagerImpl implements EntitiesManager {
    private final List<GameEntityImpl> entities = new ArrayList<>();

    public static EntitiesManagerImpl getInstance() {
        return ServiceManager.get(EntitiesManagerImpl.class);
    }

    @Override
    public List<GameEntityImpl> getEntities(Game game) {
        return entities.stream().filter(gameEntity -> gameEntity.getGame() == game).collect(Collectors.toList());
    }

    @Override
    public Optional<GameImpl> getGameOfEntity(Object entity) {
        if (entity instanceof Entity) {
            return getGameOfEntity((Entity) entity);
        } else {
            return getGameOfEntity(Objects.requireNonNull(Entities.wrapEntity(entity)));
        }
    }

    public Optional<GameImpl> getGameOfEntity(Entity entityBasic) {
        return entities.stream().filter(gameEntity -> gameEntity.getEntity().equals(entityBasic)).findFirst().map(GameEntityImpl::getGame);
    }

    @Override
    public GameEntityImpl addEntityToGame(Object entity, Game game) {
        if (entity instanceof Entity) {
            return addEntityToGame((Entity) entity, game);
        } else {
            return addEntityToGame(Objects.requireNonNull(Entities.wrapEntity(entity)), game);
        }
    }

    public GameEntityImpl addEntityToGame(Entity entityBasic, Game game) {
        if (!(game instanceof GameImpl)) {
            throw new IllegalArgumentException("Provided instance of game is not created by BedWars plugin!");
        }

        var gameEntity = new GameEntityImpl((GameImpl) game, entityBasic);
        entities.add(gameEntity);
        return gameEntity;
    }

    @Override
    public void removeEntityFromGame(Object entity) {
        if (entity instanceof Entity) {
            removeEntityFromGame((Entity) entity);
        } else {
            removeEntityFromGame(Objects.requireNonNull(Entities.wrapEntity(entity)));
        }
    }

    public void removeEntityFromGame(Entity entityBasic) {
        entities.stream()
                .filter(gameEntity -> gameEntity.getEntity().equals(entityBasic))
                .findFirst()
                .ifPresent(this::removeEntityFromGame);
    }

    @Override
    public void removeEntityFromGame(GameEntity entityObject) {
        if (!(entityObject instanceof GameEntityImpl)) {
            throw new IllegalArgumentException("Provided instance of game entity is not created by BedWars plugin!");
        }

        entities.remove(entityObject);
    }
}
