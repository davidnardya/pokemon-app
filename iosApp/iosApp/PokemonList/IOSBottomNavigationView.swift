import SwiftUI
import SharedLogic

struct IOSBottomNavigationView: View {
    @Environment(\.colorScheme) private var colorScheme

    let selectedTab: NavigationTab
    let onPokemonTap: () -> Void
    let onTeamTap: () -> Void

    private var colors: PokemonListColors {
        PokemonListColors.current(for: colorScheme)
    }

    private var isDark: Bool {
        colorScheme == .dark
    }

    var body: some View {
        HStack(spacing: 0) {
            navigationItem(
                title: "Pokemon",
                iconName: PokemonAssets.pokemonTabIcon(
                    isDark: isDark,
                    isSelected: selectedTab == .pokemon
                ),
                isSelected: selectedTab == .pokemon,
                action: onPokemonTap
            )

            navigationItem(
                title: "Team",
                iconName: PokemonAssets.teamTabIcon(
                    isDark: isDark,
                    isSelected: selectedTab == .team
                ),
                isSelected: selectedTab == .team,
                action: onTeamTap
            )
        }
        .padding(6)
        .background(colors.navigationBarBackground)
        .clipShape(Capsule())
        .shadow(color: Color.black.opacity(isDark ? 0.35 : 0.12), radius: 16, y: 4)
        .padding(.horizontal, 48)
        .padding(.bottom, 8)
    }

    private func navigationItem(
        title: String,
        iconName: String,
        isSelected: Bool,
        action: @escaping () -> Void
    ) -> some View {
        Button(action: action) {
            VStack(spacing: 4) {
                NamedAssetImage(name: iconName)
                    .frame(width: 24, height: 24)

                Text(title)
                    .font(.system(size: 12, weight: isSelected ? .semibold : .regular))
                    .foregroundStyle(colors.navigationContent)
            }
            .frame(maxWidth: .infinity)
            .padding(.vertical, 8)
            .padding(.horizontal, 12)
            .background(
                Capsule()
                    .fill(isSelected ? colors.navigationIndicator : Color.clear)
            )
        }
        .buttonStyle(.plain)
    }
}
