package fr.xgouchet.scparchive.model;

import android.support.annotation.NonNull;

/**
 * A Drawer is a visual representation for a series
 * eg : Drawer containing files SCP-300 to SCP-399
 *
 * @author Xavier Gouchet
 */
public class Drawer {


    @NonNull private final String name;
    private final int index;

    public Drawer(@NonNull String name, int index) {
        this.name = name;
        this.index = index;
    }

    @NonNull public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    @Override public String toString() {
        return "Drawer <\"" + name + "\">";
    }
}
