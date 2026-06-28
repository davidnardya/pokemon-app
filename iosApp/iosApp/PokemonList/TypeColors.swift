import SwiftUI
import SharedLogic

extension Color {
    init(argb: Int64) {
        let alpha = Double((argb >> 24) & 0xFF) / 255.0
        let red = Double((argb >> 16) & 0xFF) / 255.0
        let green = Double((argb >> 8) & 0xFF) / 255.0
        let blue = Double(argb & 0xFF) / 255.0
        self.init(.sRGB, red: red, green: green, blue: blue, opacity: alpha)
    }
}

enum PokemonTypeColors {
    static func palette(for type: PokemonType) -> TypeColorPalette {
        TypeColors.shared.palette(type: type)
    }

    static func cardBackground(isDark: Bool) -> Color {
        Color(argb: TypeColors.shared.cardBackground(isDark: isDark))
    }

    static func tileBackground(isDark: Bool) -> Color {
        Color(argb: TypeColors.shared.tileBackground(isDark: isDark))
    }

    static func statTrack(isDark: Bool) -> Color {
        Color(argb: TypeColors.shared.statTrack(isDark: isDark))
    }

    static func badgeBackground(for type: PokemonType, isDark: Bool) -> Color {
        Color(argb: TypeColors.shared.badgeBackground(type: type, isDark: isDark))
    }

    static func badgeText(for type: PokemonType, isDark: Bool) -> Color {
        Color(argb: TypeColors.shared.badgeText(type: type, isDark: isDark))
    }
}
