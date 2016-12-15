package fr.xgouchet.scparchive.network;

import java.util.Comparator;

import fr.xgouchet.scparchive.model.Drawer;

/**
 * @author Xavier Gouchet
 */
public class DrawerComparator implements Comparator<Drawer> {
    @Override public int compare(Drawer a, Drawer b) {
        final int aIndex = a == null ? 0 : a.getIndex();
        final int bIndex = b == null ? 0 : b.getIndex();
        return aIndex - bIndex;
    }
}
