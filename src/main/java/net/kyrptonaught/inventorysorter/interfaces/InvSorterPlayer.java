package net.kyrptonaught.inventorysorter.interfaces;

import net.kyrptonaught.inventorysorter.SortCases;

public interface InvSorterPlayer {
    SortCases.SortType getSortType();

    void setSortType(SortCases.SortType sortType);
}
