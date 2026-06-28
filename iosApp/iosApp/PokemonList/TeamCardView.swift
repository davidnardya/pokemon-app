import SwiftUI
import SharedLogic

struct TeamCardView: View {
    @Environment(\.colorScheme) private var colorScheme

    let item: TeamPokemonItem
    let onRemove: () -> Void

    private var colors: PokemonListColors {
        PokemonListColors.current(for: colorScheme)
    }

    private var isDark: Bool {
        colorScheme == .dark
    }

    private var cardBackground: Color {
        isDark ? Color(red: 0.165, green: 0.165, blue: 0.165) : .white
    }

    private var imageBackground: Color {
        isDark ? Color(red: 0.11, green: 0.11, blue: 0.118) : Color(red: 0.945, green: 0.945, blue: 0.945)
    }

    private var removeButtonBackground: Color {
        isDark ? .white : Color(red: 0.239, green: 0.239, blue: 0.239)
    }

    private var removeIconColor: Color {
        isDark ? .black : .white
    }

    var body: some View {
        HStack(spacing: 12) {
            ZStack {
                RoundedRectangle(cornerRadius: 16, style: .continuous)
                    .fill(imageBackground)

                AsyncImage(url: URL(string: PokemonImageURL.forId(item.pokemon.id))) { phase in
                    switch phase {
                    case .success(let image):
                        image
                            .resizable()
                            .scaledToFit()
                            .padding(4)
                    default:
                        Color.clear
                    }
                }
            }
            .frame(width: 80, height: 80)

            VStack(alignment: .leading, spacing: 6) {
                Text(item.pokemon.displayName)
                    .font(.system(size: 16, weight: .bold))
                    .foregroundStyle(colors.primaryText)

                if !item.types.isEmpty {
                    HStack(spacing: 6) {
                        ForEach(Array(item.types.enumerated()), id: \.offset) { _, type in
                            PokemonTypeBadgeView(type: type)
                        }
                    }
                }

                Text(item.addedDateLabel)
                    .font(.system(size: 13, weight: .regular))
                    .foregroundStyle(colors.secondaryText)
            }

            Spacer(minLength: 0)

            Button(action: onRemove) {
                Text("✕")
                    .font(.system(size: 14, weight: .bold))
                    .foregroundStyle(removeIconColor)
                    .frame(width: 32, height: 32)
                    .background(removeButtonBackground)
                    .clipShape(Circle())
            }
            .buttonStyle(.plain)
        }
        .padding(12)
        .background(cardBackground)
        .clipShape(RoundedRectangle(cornerRadius: 24, style: .continuous))
    }
}
