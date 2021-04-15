package gg.rsmod.plugins.content.inter.options

import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.ext.getVarbit
import gg.rsmod.plugins.api.ext.setVarbit
import gg.rsmod.plugins.api.ext.toggleVarbit
import java.util.*

object Hotkeys {

    fun setHotkey(player: Player, hotkeySlot: Int, hotkeyValue: Int) {
        val hotkey = Hotkeys.Hotkey.map[hotkeySlot] ?: return@setHotkey
        val activeHotkeys = getActiveHotkeys(player)

        val existingKey = activeHotkeys.withIndex().find { it.value == hotkeyValue && it.index != hotkeySlot }
        existingKey?.let {
            val existingHotkey = Hotkey.map[it.index] ?: return@let
            player.setVarbit(existingHotkey.varbit, 0)
        }
        player.setVarbit(hotkey.varbit, hotkeyValue)
    }

    fun toggleEscapeToClose(player: Player) = player.toggleVarbit(4681)

    /**
     * Resets the [player]s keybinds to default
     */
    fun resetHotkeys(player: Player) =
        Hotkey.values().forEach { player.setVarbit(it.varbit, it.defaultKey) }

    private fun getActiveHotkeys(player: Player): IntArray {
        val hotkeys = IntArray(Hotkey.values().size)
        Hotkey.values().withIndex().forEach {
            hotkeys[it.index] = player.getVarbit(it.value.varbit)
        }
        return hotkeys
    }

    /**
     * Represents a Hotkey with it's associated varbit
     *
     * @author Kobra#5689
     */
    enum class Hotkey(val varbit: Int, val defaultKey: Int) {

        COMBAT(varbit = 4675, defaultKey = 1),
        PRAYER(varbit = 4680, defaultKey = 5),
        SETTINGS(varbit = 4686, defaultKey = 10),
        SKILLS(varbit = 4676, defaultKey = 2),
        MAGIC(varbit = 4682, defaultKey = 6),
        EMOTE(varbit = 4687, defaultKey = 11),
        QUEST(varbit = 4677, defaultKey = 3),
        FRIENDS(varbit = 4684, defaultKey = 8),
        CLAN(varbit = 4683, defaultKey = 7),
        INVENTORY(varbit = 4678, defaultKey = 13),
        MANAGEMENT(varbit = 6517, defaultKey = 9),
        MUSIC(4688, defaultKey = 12),
        EQUIPMENT(varbit = 4679, defaultKey = 4),
        LOGOUT(varbit = 4689, defaultKey = 0);

        companion object {
            /**
             * All the Hotkeys mapped by their index
             */
            val map = mutableMapOf<Int, Hotkey>().apply {
                Hotkey.values().withIndex().forEach { this[it.index] = it.value }
            }
        }

    }

}
