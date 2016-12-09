/*
 * Copyright 2016 Stocard GmbH.
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.stocard.keepactivitiestile;

import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import static de.stocard.keepactivitiestile.KeepActivitiesToggle.getIcon;
import static de.stocard.keepactivitiestile.KeepActivitiesToggle.getKeepActivities;
import static de.stocard.keepactivitiestile.KeepActivitiesToggle.getLabel;

/**
 * A {@link TileService quick settings tile} for toggling "Don't keep activities".
 */
public class ToggleKeepActivities extends TileService {

    public ToggleKeepActivities() { }

    @Override
    public void onStartListening() {
        super.onStartListening();
        updateTile();
    }

    @Override
    public void onClick() {
        final boolean current = getKeepActivities(getContentResolver());
        final boolean target = !current;
        KeepActivitiesToggle.setKeepActivities(this, target);
        updateTile();
    }

    private void updateTile() {
        final boolean keepActivities = getKeepActivities(getContentResolver());
        final Tile tile = getQsTile();
        tile.setIcon(Icon.createWithResource(getApplicationContext(), getIcon(keepActivities)));
        tile.setLabel(getString(getLabel(keepActivities)));
        tile.updateTile();
    }

}
