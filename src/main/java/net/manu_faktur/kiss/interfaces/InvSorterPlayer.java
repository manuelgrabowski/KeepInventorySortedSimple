package net.manu_faktur.kiss.interfaces;

import net.manu_faktur.kiss.SortCases;

public interface InvSorterPlayer {
    SortCases.SortType getSortType();

    void setSortType(SortCases.SortType sortType);
}
