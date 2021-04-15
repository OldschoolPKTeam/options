package gg.rsmod.plugins.content.inter.options

import gg.rsmod.game.model.interf.DisplayMode

/**
 * The component ID for individual components on this interface
 */
val COMPONENT_ID = 18

/**
 * The slider component ID for this interface
 */
val SLIDER_ID = 20

/**
 * The dropdown component ID for this interface
 */
val DROPDOWN_CHILD_ID = 27

fun bind_all_setting(child: Int, plugin: Plugin.() -> Unit) {
    on_button(interfaceId = OptionsTab.ALL_SETTINGS_INTERFACE_ID, component = child) {
        plugin(this)
    }
}

bind_all_setting(Settings.SETTINGS_TAB_SELECTOR) {
    player.devMessage("Interacting tab = ${player.getInteractingSlot()}")
    player.setVarbit(9656, player.getInteractingSlot().run {
        when(this) {// for some reason rs has gameplay and hotkeys tab swapped
            5 -> 4
            4 -> 5
            else -> this
        }
    })
}

bind_all_setting(DROPDOWN_CHILD_ID) {
    player.devMessage("Interacting dropdown slot = ${player.getInteractingSlot()}")
    val selectedTab = player.selectedTab()

    val interactingComp = player.getLastInteractingSlot()
    val interactingSlot = player.getInteractingSlot()
    val choice = (interactingSlot - 2) / 3
    when (selectedTab) {
        0 -> { // General
            when(interactingComp) {
                1 -> {// Display mode
                    when (choice) {
                        1 -> player.toggleDisplayInterface(DisplayMode.RESIZABLE_CLASSIC)
                        2 -> player.toggleDisplayInterface(DisplayMode.RESIZABLE_MODERN)
                        else -> player.toggleDisplayInterface(DisplayMode.FIXED)
                    }
                }
                10 -> {// Scrollbar right or left
                    when (choice) {
                        0 -> player.setVarbit(6374, 0)
                        1 -> player.setVarbit(6374, 1)
                    }
                }
            }
        }
        3 -> { // Controls
            when (interactingComp) {
                1 -> player.setVarp(1107, choice) // Player attack options
                2 -> player.setVarp(1306, choice) // NPC attack options
            }
        }
        4 -> { // Keybinds
            val hotkeySlot = player.getLastInteractingSlot() - 1
            val hotkeyValue = choice
            Hotkeys.setHotkey(player, hotkeySlot, hotkeyValue)
        }
    }
}

bind_all_setting(SLIDER_ID) {
    player.devMessage("Interacting slider slot = ${player.getInteractingSlot()}")

    when(player.selectedTab()) {
        0 -> player.setVarp(OSRSGameframe.SCREEN_BRIGHTNESS_VARP, player.getInteractingSlot() + 1)
    }

}

bind_all_setting(COMPONENT_ID) {
    player.devMessage("Interacting component slot = ${player.getInteractingSlot()}")
    when (player.selectedTab()) {
        0 -> { // General
            when (player.getInteractingSlot()) {
                8 -> { // Transparent chatbox
                    if (player.interfaces.displayMode != DisplayMode.FIXED) {
                        player.toggleVarbit(4608)
                    }
                }
                9 -> { // Click-through trans chatbox
                    if (player.interfaces.displayMode != DisplayMode.FIXED) {
                        player.toggleVarbit(2570)
                    }
                }
                11 -> { // Transparent Side panels
                    if (player.interfaces.displayMode != DisplayMode.FIXED) {
                        player.toggleVarbit(4609)
                    }
                }
                12 -> { // Close side-tab with hotkey
                    if (player.interfaces.displayMode == DisplayMode.RESIZABLE_MODERN) {
                        player.toggleVarbit(4611)
                    }
                }
                13 -> player.toggleVarbit(10236) // Show tinted hitsplats
                14 -> player.toggleVarbit(4084) // Data orbs
                16 -> player.toggleVarbit(10113) // Wiki lookup
                17 -> player.toggleVarbit(10345) // Health overlay
                18 -> player.toggleVarbit(4181) // Show remaining xp
                19 -> player.toggleVarbit(5711) // Show prayer tooltip
                20 -> player.toggleVarbit(5712) // Show spec tooltip
            }
        }
        1 -> { // Chat
            when (player.getInteractingSlot()) {
                1 -> player.toggleVarbit(10078) // Music unlock message
            }
        }
        2 -> { // Chat
            when (player.getInteractingSlot()) {
                1 -> player.toggleVarbit(1074) // Enable Profanity filter
                2 -> player.toggleVarbit(1627) // Friend login/logout message timeout
                3 -> player.toggleVarbit(171) // Chat effects
                4 -> player.toggleVarbit(287) // Split friends private chat
                5 -> player.toggleVarbit(4089) // Hide private chat when chat is hidden
                7, 8 -> lootDropNotification(player) // Drop notification and input
                9 -> player.toggleVarbit(5402) // Untradeable loot notification
                10 -> player.toggleVarbit(4930) // Boss kill-count
                11, 12 -> dropItemWarning(player) // Drop item warning and input
                13, 14 -> alchemyWarning(player) // Alchemy warning and input
            }
        }
        3 -> { // Controls
            when (player.getInteractingSlot()) {
                3 -> player.toggleVarp(170) // Single Mouse mode
                4 -> player.toggleVarbit(4134) // Middle mouse controls camera
                5 -> player.toggleVarbit(5542) // Shift click to drop
                6 -> player.toggleVarbit(5599) // Move follower options down
                8 -> player.message("This option is disabled. Use settings tab instead.")
            }
        }
        4 -> { // Keybinds
            when (player.getInteractingSlot()) {
                15 -> Hotkeys.resetHotkeys(player) // TRANS CHATBOX
                16 -> Hotkeys.toggleEscapeToClose(player) // RESIZABLE CHATBOX
            }
        }
        5 -> { // Gameplay
            val teleportVarbits = setOf(236, 6284, 6285, 6286, 6287)
            val tabVarbits = setOf(3930, 2325, 2324, 3931, 2322, 3932, 2323)
            when (player.getInteractingSlot()) {
                2 -> player.toggleVarbit(236) // Teleport to target
                3 -> player.toggleVarbit(6284) // Dareeyak
                4 -> player.toggleVarbit(6285) // Carrallangar
                5 -> player.toggleVarbit(6286) // Annakarl
                6 -> player.toggleVarbit(6287) // Ghorrock
                7 -> teleportVarbits.forEach { player.setVarbit(it, 1) } // Enable all teleport warnings
                8 -> teleportVarbits.forEach { player.setVarbit(it, 0) } // Disable all teleport warnings
                11 -> player.toggleVarbit(3930) // Dareeyak tab
                12 -> player.toggleVarbit(2325) // Carrallangar tab
                13 -> player.toggleVarbit(2324) // Annakarl tab
                14 -> player.toggleVarbit(3931) // Ghorrock tab
                15 -> player.toggleVarbit(2322) // Cemetary tab
                16 -> player.toggleVarbit(3932) // Wilderness crab tab
                17 -> player.toggleVarbit(2323) // Ice Plateau
                18 -> tabVarbits.forEach { player.setVarbit(it, 0) } // Enable all tab warnings
                19 -> tabVarbits.forEach { player.setVarbit(it, 1) } // Disable all tab warnings
            }
        }
    }
}

fun lootDropNotification(player: Player) = handleThresholdAndWarning(player, 5399, 5400)

fun dropItemWarning(player: Player) = handleThresholdAndWarning(player, 5411, 5412)

fun alchemyWarning(player: Player) = handleThresholdAndWarning(player, 9647, 6091)

fun handleThresholdAndWarning(player: Player, checkBoxVarbit: Int, valueVarbit: Int) {
    val inputComponents = setOf(8, 12, 14)
    val interactingButton = player.getInteractingSlot()

    fun inputPrompt() {
        player.queue {
            when (val input = this.inputInt(description = "Set threshold value")) {
                0 -> {
                    player.setVarbit(checkBoxVarbit, 0)
                    player.setVarbit(valueVarbit, 0)
                }
                else -> {
                    player.setVarbit(checkBoxVarbit, 1)
                    player.setVarbit(valueVarbit, input)
                }
            }
        }
    }

    when (player.getVarbit(checkBoxVarbit) == 1) {
        true -> if (interactingButton !in inputComponents) player.setVarbit(checkBoxVarbit, 0) else inputPrompt()
        else -> inputPrompt()
    }
}

fun Player.selectedTab(): Int = this.getVarbit(9656)