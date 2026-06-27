import SwiftUI
import SharedLogic

struct PokemonCardView: View {
    @Environment(\.colorScheme) private var colorScheme

    let pokemon: Pokemon
    let isInTeam: Bool
    let onPokemonTap: () -> Void
    let onPokeballTap: () -> Void

    private var colors: PokemonListColors {
        PokemonListColors.current(for: colorScheme)
    }

    private var isDark: Bool {
        colorScheme == .dark
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            ZStack(alignment: .topLeading) {
                imageArea
                    .aspectRatio(1, contentMode: .fit)
                    .contentShape(Rectangle())
                    .onTapGesture(perform: onPokemonTap)

                Button(action: onPokeballTap) {
                    pokeballIcon
                }
                .buttonStyle(.plain)
                .padding(12)
            }

            VStack(alignment: .leading, spacing: 0) {
                Text(pokemon.displayName)
                    .font(.system(size: 16, weight: .bold))
                    .foregroundStyle(colors.primaryText)

                Text(pokemon.formattedId)
                    .font(.system(size: 14, weight: .medium))
                    .foregroundStyle(colors.secondaryText)
            }
            .padding(.top, 10)
            .padding(.leading, colors.usesDistinctImageSurface ? 4 : 0)
            .padding(.bottom, colors.usesDistinctImageSurface ? 4 : 0)
            .contentShape(Rectangle())
            .onTapGesture(perform: onPokemonTap)
        }
        .padding(colors.usesDistinctImageSurface ? 8 : 12)
        .background(colors.cardContainer)
        .clipShape(RoundedRectangle(cornerRadius: 24, style: .continuous))
    }

    @ViewBuilder
    private var imageArea: some View {
        if colors.usesDistinctImageSurface {
            ZStack {
                RoundedRectangle(cornerRadius: 20, style: .continuous)
                    .fill(colors.cardImageSurface)

                AsyncImage(url: URL(string: PokemonImageURL.forId(pokemon.id))) { phase in
                    switch phase {
                    case .success(let image):
                        image
                            .resizable()
                            .scaledToFit()
                            .padding(.horizontal, 8)
                            .padding(.vertical, 12)
                    default:
                        Color.clear
                    }
                }
            }
        } else {
            AsyncImage(url: URL(string: PokemonImageURL.forId(pokemon.id))) { phase in
                switch phase {
                case .success(let image):
                    image
                        .resizable()
                        .scaledToFit()
                        .padding(.horizontal, 8)
                        .padding(.vertical, 12)
                default:
                    Color.clear
                }
            }
        }
    }

    private var pokeballIcon: some View {
        Image(pokeballAssetName)
            .renderingMode(.original)
            .resizable()
            .scaledToFit()
            .frame(width: 24, height: 24)
    }

    private var pokeballAssetName: String {
        if isInTeam {
            return PokemonAssets.loadingPokeball
        }
        return PokemonAssets.pokeball(isDark: isDark)
    }
}

enum PokemonImageURL {
    static func forId(_ id: Int32) -> String {
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/\(id).png"
    }
}
