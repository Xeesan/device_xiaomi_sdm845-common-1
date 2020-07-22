/*
 *  Poco Extras Settings Module
 *  Made by @shivatejapeddi 2019
 */

package com.lineageos.settings.pocopref;

import android.graphics.drawable.Icon;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import com.lineageos.settings.pocopref.XiaomiDisplay;
import com.lineageos.settings.pocopref.utils.Shell;
import com.lineageos.settings.pocopref.R;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

/*
 * Based on the original implementation of Spectrum Kernel Manager by frap129 <joe@frap129.org>
 *
 * Originally authored by Morogoku <morogoku@hotmail.com>
 *
 * Modified by sunilpaulmathew <sunil.kde@gmail.com>
 */

public class GameTile extends TileService {

    private int id = 0;
    private static final String TAG = Shell.class.getSimpleName();

    @Override
    public void onClick() {
        updateTile();
    }

    private void updateTile() {
        Tile tile = this.getQsTile();
        Icon newIcon;
	newIcon =  Icon.createWithResource(getApplicationContext(), R.drawable.ic_enhanced_display);
        String newLabel;
        int newState;

        if (id == 0) {
		newLabel = "Default";
		newState = Tile.STATE_INACTIVE;
		id +=1;
            runCommand("display ENHANCE 1");
            runCommand("display ADAPT 3");
            } else if (id == 1) {
		newLabel = "Bright";
		newState = Tile.STATE_ACTIVE;
		id +=1;
            runCommand("display GAME_HDR 2");
            } else if (id == 2) {
		newLabel = "Saturation";
		newState = Tile.STATE_ACTIVE;
		id +=1;
            runCommand("display GAME_HDR -38");
            } else {
		newLabel = "Bri+Sat";
		newState = Tile.STATE_ACTIVE;
		id = 0;
            runCommand("display GAME_HDR 41");
            }

        // Change the UI of the tile.
        tile.setLabel(newLabel);
        tile.setIcon(newIcon);
        tile.setState(newState);
        tile.updateTile();
    }

   public static void runCommand(String command) {
        Log.d(TAG, "setCurrentProfile() cmd=\'" + command + "\'");
        ArrayList<String> output = Shell.runWithShell(command);
        for (String line : output) {
            Log.d(TAG, "setCurrentProfile() output:" + line);
        }
   }

}