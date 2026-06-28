import SwiftUI

enum PokemonAssets {
    static func settings(isDark: Bool) -> String {
        isDark ? "ic_darkmode_settings" : "ic_lightmode_settings"
    }

    static func pokemonTabIcon(isDark: Bool, isSelected: Bool) -> String {
        switch (isDark, isSelected) {
        case (true, true): return "ic_darkmode_switch_pokemon_full"
        case (true, false): return "ic_darkmode_switch_pokemon_empty"
        case (false, true): return "ic_lightmode_switch_pokemon_full"
        case (false, false): return "ic_lightmode_switch_pokemon_empty"
        }
    }

    static func teamTabIcon(isDark: Bool, isSelected: Bool) -> String {
        switch (isDark, isSelected) {
        case (true, true): return "ic_darkmode_switch_team_full"
        case (true, false): return "ic_darkmode_switch_team_empty"
        case (false, true): return "ic_lightmode_switch_team_full"
        case (false, false): return "ic_lightmode_switch_team_empty"
        }
    }

    static func pokeball(isDark: Bool) -> String {
        isDark ? "ic_pokeball_dark_grey" : "ic_pokeball_grey"
    }

    static let loadingPokeball = "ic_loading_pokeball_orange"

    static func loadingList(isDark: Bool) -> String {
        isDark ? "img_darkmode_loading_list" : "img_lightmode_loading_list"
    }

    static func addToTeam(isDark: Bool) -> String {
        isDark ? "img_darkmode_add_to_team" : "img_lightmode_add_to_team"
    }

    static func settingsClose(isDark: Bool) -> String {
        isDark ? "ic_darkmode_settings_close" : "ic_lightmode_settings_close"
    }

    static func settingsUnit(isDark: Bool) -> String {
        isDark ? "ic_darkmode_unit" : "ic_lightmode_unit"
    }

    static func settingsClock(isDark: Bool) -> String {
        isDark ? "ic_darkmode_clock" : "ic_lightmode_clock"
    }

    static func settingsMode(isDark: Bool) -> String {
        isDark ? "ic_darkmode_mode" : "ic_lightmode_mode"
    }

    static func settingsAbout(isDark: Bool) -> String {
        isDark ? "ic_darkmode_about" : "ic_lightmode_about"
    }

    static func settingsCheckmark(isDark: Bool) -> String {
        "ic_lightmode_checkmark"
    }

    static func settingsDownArrow(isDark: Bool) -> String {
        isDark ? "ic_darkmode_down_arrow" : "ic_lightmode_down_arrow"
    }

    static func settingsRightArrow(isDark: Bool) -> String {
        isDark ? "ic_darkmode_right_arrow" : "ic_lightmode_right_arrow"
    }
}

struct ThemedAssetImage: View {
    @Environment(\.colorScheme) private var colorScheme

    let lightAsset: String
    let darkAsset: String

    var body: some View {
        Image(colorScheme == .dark ? darkAsset : lightAsset)
            .renderingMode(.original)
    }
}

struct NamedAssetImage: View {
    let name: String

    var body: some View {
        Image(name)
            .renderingMode(.original)
    }
}
