import SwiftUI

struct PokemonListColors {
    let screenBackground: Color
    let primaryText: Color
    let secondaryText: Color
    let settingsButtonBackground: Color
    let navigationBarBackground: Color
    let navigationIndicator: Color
    let navigationContent: Color
    let cardContainer: Color
    let cardImageSurface: Color
    let usesDistinctImageSurface: Bool

    static func current(for colorScheme: ColorScheme) -> PokemonListColors {
        if colorScheme == .dark {
            return PokemonListColors(
                screenBackground: Color(red: 0, green: 0, blue: 0),
                primaryText: .white,
                secondaryText: Color(red: 0.557, green: 0.557, blue: 0.576),
                settingsButtonBackground: Color(red: 0.173, green: 0.173, blue: 0.180),
                navigationBarBackground: Color(red: 0.11, green: 0.11, blue: 0.118),
                navigationIndicator: Color(red: 0.23, green: 0.23, blue: 0.235),
                navigationContent: .white,
                cardContainer: Color(red: 0.165, green: 0.165, blue: 0.165),
                cardImageSurface: Color(red: 0.165, green: 0.165, blue: 0.165),
                usesDistinctImageSurface: false
            )
        }

        return PokemonListColors(
            screenBackground: Color(red: 0.929, green: 0.929, blue: 0.929),
            primaryText: Color(red: 0.102, green: 0.102, blue: 0.102),
            secondaryText: Color(red: 0.557, green: 0.557, blue: 0.576),
            settingsButtonBackground: .white,
            navigationBarBackground: Color(red: 0.949, green: 0.949, blue: 0.969),
            navigationIndicator: Color(red: 0.890, green: 0.890, blue: 0.910),
            navigationContent: Color(red: 0.102, green: 0.102, blue: 0.102),
            cardContainer: .white,
            cardImageSurface: Color(red: 0.945, green: 0.945, blue: 0.945),
            usesDistinctImageSurface: true
        )
    }
}
