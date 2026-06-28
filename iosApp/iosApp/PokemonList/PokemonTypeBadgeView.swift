import SwiftUI
import SharedLogic

struct PokemonTypeBadgeView: View {
    @Environment(\.colorScheme) private var colorScheme

    let type: PokemonType

    private var isDark: Bool {
        colorScheme == .dark
    }

    var body: some View {
        let colors = PokemonTypeBadgeColors.forType(type, isDark: isDark)
        Text(type.displayName)
            .font(.system(size: 12, weight: .semibold))
            .foregroundStyle(colors.text)
            .padding(.horizontal, 10)
            .padding(.vertical, 4)
            .background(colors.background)
            .clipShape(RoundedRectangle(cornerRadius: 8, style: .continuous))
    }
}

private struct PokemonTypeBadgeColors {
    let background: Color
    let text: Color

    static func forType(_ type: PokemonType, isDark: Bool) -> PokemonTypeBadgeColors {
        if isDark {
            return PokemonTypeBadgeColors(
                background: darkBackground(for: type),
                text: .white
            )
        }
        return PokemonTypeBadgeColors(
            background: lightBackground(for: type),
            text: lightText(for: type)
        )
    }

    private static func lightBackground(for type: PokemonType) -> Color {
        switch type {
        case .grass: return Color(red: 0.761, green: 0.882, blue: 0.725)
        case .poison: return Color(red: 0.898, green: 0.800, blue: 0.933)
        case .fire: return Color(red: 0.973, green: 0.835, blue: 0.710)
        case .water: return Color(red: 0.722, green: 0.831, blue: 0.941)
        case .electric: return Color(red: 0.973, green: 0.910, blue: 0.627)
        case .fairy: return Color(red: 0.961, green: 0.761, blue: 0.847)
        case .steel: return Color(red: 0.784, green: 0.831, blue: 0.878)
        case .dragon: return Color(red: 0.831, green: 0.761, blue: 0.941)
        case .ice: return Color(red: 0.722, green: 0.910, blue: 0.941)
        case .fighting: return Color(red: 0.941, green: 0.761, blue: 0.722)
        case .ground: return Color(red: 0.910, green: 0.831, blue: 0.722)
        case .flying: return Color(red: 0.831, green: 0.878, blue: 0.941)
        case .psychic: return Color(red: 0.941, green: 0.761, blue: 0.847)
        case .bug: return Color(red: 0.831, green: 0.910, blue: 0.722)
        case .rock: return Color(red: 0.878, green: 0.831, blue: 0.784)
        case .ghost: return Color(red: 0.831, green: 0.784, blue: 0.910)
        case .dark: return Color(red: 0.784, green: 0.784, blue: 0.831)
        case .normal: return Color(red: 0.878, green: 0.878, blue: 0.878)
        default: return Color(red: 0.878, green: 0.878, blue: 0.878)
        }
    }

    private static func lightText(for type: PokemonType) -> Color {
        switch type {
        case .grass: return Color(red: 0.306, green: 0.510, blue: 0.204)
        case .poison: return Color(red: 0.624, green: 0.357, blue: 0.729)
        case .fire: return Color(red: 0.722, green: 0.361, blue: 0.102)
        case .water: return Color(red: 0.227, green: 0.435, blue: 0.627)
        case .electric: return Color(red: 0.604, green: 0.541, blue: 0.102)
        case .fairy: return Color(red: 0.722, green: 0.361, blue: 0.541)
        case .steel: return Color(red: 0.353, green: 0.416, blue: 0.478)
        case .dragon: return Color(red: 0.416, green: 0.290, blue: 0.604)
        case .ice: return Color(red: 0.227, green: 0.541, blue: 0.604)
        case .fighting: return Color(red: 0.604, green: 0.290, blue: 0.227)
        case .ground: return Color(red: 0.541, green: 0.416, blue: 0.227)
        case .flying: return Color(red: 0.353, green: 0.478, blue: 0.604)
        case .psychic: return Color(red: 0.722, green: 0.361, blue: 0.541)
        case .bug: return Color(red: 0.416, green: 0.541, blue: 0.227)
        case .rock: return Color(red: 0.478, green: 0.416, blue: 0.353)
        case .ghost: return Color(red: 0.416, green: 0.353, blue: 0.541)
        case .dark: return Color(red: 0.353, green: 0.353, blue: 0.416)
        case .normal: return Color(red: 0.416, green: 0.416, blue: 0.416)
        default: return Color(red: 0.416, green: 0.416, blue: 0.416)
        }
    }

    private static func darkBackground(for type: PokemonType) -> Color {
        switch type {
        case .grass: return Color(red: 0.298, green: 0.686, blue: 0.314)
        case .poison: return Color(red: 0.612, green: 0.153, blue: 0.690)
        case .fire: return Color(red: 0.902, green: 0.318, blue: 0.0)
        case .water: return Color(red: 0.098, green: 0.463, blue: 0.824)
        case .electric: return Color(red: 0.976, green: 0.659, blue: 0.145)
        case .fairy: return Color(red: 0.914, green: 0.118, blue: 0.388)
        case .steel: return Color(red: 0.376, green: 0.490, blue: 0.545)
        case .dragon: return Color(red: 0.482, green: 0.122, blue: 0.635)
        case .ice: return Color(red: 0.0, green: 0.675, blue: 0.757)
        case .fighting: return Color(red: 0.847, green: 0.263, blue: 0.082)
        case .ground: return Color(red: 0.553, green: 0.431, blue: 0.388)
        case .flying: return Color(red: 0.361, green: 0.420, blue: 0.753)
        case .psychic: return Color(red: 0.671, green: 0.278, blue: 0.737)
        case .bug: return Color(red: 0.408, green: 0.624, blue: 0.220)
        case .rock: return Color(red: 0.475, green: 0.333, blue: 0.282)
        case .ghost: return Color(red: 0.369, green: 0.208, blue: 0.694)
        case .dark: return Color(red: 0.259, green: 0.259, blue: 0.259)
        case .normal: return Color(red: 0.459, green: 0.459, blue: 0.459)
        default: return Color(red: 0.459, green: 0.459, blue: 0.459)
        }
    }
}
