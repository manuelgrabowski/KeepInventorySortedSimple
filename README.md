# KISS – Keep Inventories Sorted, Simple.

Lightweight inventory sorting mod, a very opinionated 1.21 update and fork of [Inventory Sorting](https://modrinth.com/mod/inventory-sorting).



## Screenshots

tbd


## Configuration

Use [Mod Menu](https://modrinth.com/mod/modmenu) for GUI configuration, or the `/kiss` command otherwise.  

_Note: Configuration might be a bit messy. The original configuration seems to have had a few issues.
I removed a few options, but I'm not sure if that resolved all issues._

## Usage

You can use the middle mouse button to sort the inventory you're looking at. That keybinding can be changed in the
configuration. That's pretty much it, there is not much more to it – check the rest of the configuration, but be aware
that anything beyond this core functionality might be removed in upcoming versions (see below).

While the mod is technically only required on the server, you should install it on server _and_ client. If you only
install it on the server, vanilla clients can use it via the `/kiss` commands only.

## Changes from original mod

- Shulker boxes are now sorted based on their content, heavily inspired by [PR #82](https://github.com/kyrptonaught/Inventory-Sorter/pull/82)
- Removed download function for the ignore list
  - Can still be curated manually in the config; but I'm considering to remove the feature entirely as I don't play
    modded and have no need for it
- Removed sorting by double click or middle click on empty slots
  - This was buggy and didn't work on servers; instead, the middle mouse button is now the default keybinding to trigger
    a sort, which works fine on servers as well
- Took the `sortKeyType` fix from [PR #115](https://github.com/kyrptonaught/Inventory-Sorter/pull/115), 
  which _might_ fix issues [#90](https://github.com/kyrptonaught/Inventory-Sorter/issues/90) and [#101](https://github.com/kyrptonaught/Inventory-Sorter/issues/101)

More things might get kicked out in future version – I never want to sort the player inventory, so I'm not keen on
keeping that code around, for example. That includes the "sort hovered inventory" setting as well. I'm also not too fond
of the button and might remove it entirely.  
As the [name suggests](https://en.wikipedia.org/wiki/KISS_principle), I intend to keep this mod as simple as possible to
fill my needs.  

A functioning 1.21 build of the original mod [is available](https://github.com/kyrptonaught/Inventory-Sorter/pull/123#issuecomment-2185101760), if that's what you're looking for.