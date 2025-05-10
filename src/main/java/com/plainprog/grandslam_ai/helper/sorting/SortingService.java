package com.plainprog.grandslam_ai.helper.sorting;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SortingService {

    public static final long GAP = 1000;

    /**
     * Attempts to set the position of an item in a list based on its neighbors.
     * DB saving is not performed here. and should be handled by the caller.
     *
     * @param itemToMove The item to be moved.
     * @param prev The item that should be before the moved item, or null if moving to the start.
     * @param next The item that should be after the moved item, or null if moving to the end.
     * @return true if the item is successfully moved without needing a rebalance, false if rebalancing is required.
     */
    public boolean reorder(Sortable itemToMove, Sortable prev, Sortable next) {
        long newPosition;

        if (prev == null && next == null) {
            newPosition = GAP;
        } else if (prev == null) {
            newPosition = next.getPosition() - GAP;
        } else if (next == null) {
            newPosition = prev.getPosition() + GAP;
        } else {
            long gap = next.getPosition() - prev.getPosition();

            if (gap > 1) {
                newPosition = prev.getPosition() + gap / 2;
            } else {
                return false; // Indicate failure due to lack of space
            }
        }

        itemToMove.setPosition(newPosition);
        return true; // Successfully moved
    }

    /**
     * Re-balances the positions of all items in the list to maintain a uniform gap.
     *
     * @param items The list of items to rebalance.
     */
    public void rebalance(List<? extends Sortable> items) {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setPosition(i * GAP);
        }
    }
}