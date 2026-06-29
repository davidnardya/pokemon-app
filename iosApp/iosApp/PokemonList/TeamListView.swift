import SwiftUI
import SharedLogic

struct TeamListView: View {
    @Environment(\.colorScheme) private var colorScheme

    let teamItems: [TeamPokemonItem]
    let teamCount: Int
    let onRemoveFromTeam: (Int32) -> Void
    let onAddMoreTap: () -> Void

    private var colors: PokemonListColors {
        PokemonListColors.current(for: colorScheme)
    }

    private var isDark: Bool {
        colorScheme == .dark
    }

    var body: some View {
        ScrollView {
            VStack(spacing: 12) {
                ForEach(Array(teamItems.enumerated()), id: \.element.pokemon.id) { _, item in
                    TeamCardView(
                        item: item,
                        onRemove: { onRemoveFromTeam(item.pokemon.id) }
                    )
                }

                if teamCount < Int(PokemonListUiState.companion.MAX_TEAM_SIZE) {
                    addMoreButton
                }

                Spacer(minLength: 8)
            }
            .padding(.horizontal, 16)
            .padding(.top, 8)
            .padding(.bottom, IOSBottomNavigationMetrics.contentBottomInset)
        }
    }

    private var addMoreButton: some View {
        Button(action: onAddMoreTap) {
            VStack(spacing: 16) {
                Image(PokemonAssets.addToTeam(isDark: isDark))
                    .renderingMode(.original)
                    .resizable()
                    .scaledToFit()
                    .frame(width: 176, height: 176)

                Text("Add more pokemons\nto your team")
                    .font(.system(size: 18, weight: .bold))
                    .multilineTextAlignment(.center)
                    .foregroundStyle(isDark ? colors.secondaryText : colors.primaryText)
                    .lineSpacing(4)
            }
            .frame(maxWidth: .infinity)
            .padding(.vertical, 24)
        }
        .buttonStyle(.plain)
    }
}
