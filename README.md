# KISS – Keep Inventories Sorted, Simple

Lightweight inventory sorting mod, an opinionated fork of [Inventory Sorting](https://modrinth.com/mod/inventory-sorting).

## Screenshots

| Before sorting | After sorting |
|----------------|---------------|
| ![Messy inventory before sorting](https://cdn.modrinth.com/data/FbHSPTyF/images/08380e05b921fe4c09b4785341fc3b0545358e07.png) | ![Perfectly clean, sorted inventory](https://cdn.modrinth.com/data/FbHSPTyF/images/4e10f256933a0a36cc401ac90269fc19d5a5b149.png) |

## Configuration

Use [Mod Menu](https://modrinth.com/mod/modmenu) for GUI configuration, or the `/kiss` command otherwise.

## Usage

You can use the middle mouse button or `R` to sort the inventory you're looking at. These keybindings can be changed in
the configuration. That's pretty much it, there is not much more to it – check the rest of the configuration, but be
aware that anything beyond this core functionality might be removed in upcoming versions (see below).

While the mod is technically only required on the server, you should install it on server _and_ client. If you only
install it on the server, vanilla clients can use it via the `/kiss` commands only. Due to a [known issue with Modrinth](https://github.com/modrinth/knossos/issues/1612),
I've marked the mod as `Required` on the client.