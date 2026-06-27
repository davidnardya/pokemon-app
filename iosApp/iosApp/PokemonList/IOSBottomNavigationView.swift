import SwiftUI
import SharedLogic

enum IOSBottomNavigationMetrics {
    /// Bottom scroll inset so list items are not obscured by the floating switch.
    static let contentBottomInset: CGFloat = 80

    static let containerWidth: CGFloat = 300
    static let containerHeight: CGFloat = 50
}

private let bottomNavigationSelectionID = "bottomNavigationSelection"

struct IOSBottomNavigationView: View {
    @Environment(\.colorScheme) private var colorScheme
    @Namespace private var selectionNamespace

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
        ZStack {
            Capsule()
                .fill(colors.navigationBarBackground)

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
        }
        .frame(maxWidth: IOSBottomNavigationMetrics.containerWidth)
        .frame(height: IOSBottomNavigationMetrics.containerHeight)
        .animation(.easeInOut(duration: 0.25), value: selectedTab)
        .clipShape(Capsule())
        .shadow(
            color: Color.black.opacity(isDark ? 0.35 : 0.12),
            radius: 16,
            y: 4
        )
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
            ZStack {
                if isSelected {
                    Capsule()
                        .fill(colors.navigationIndicator)
                        .matchedGeometryEffect(
                            id: bottomNavigationSelectionID,
                            in: selectionNamespace
                        )
                }

                VStack(spacing: 4) {
                    NamedAssetImage(name: iconName)
                        .frame(width: 24, height: 24)

                    Text(title)
                        .font(.system(size: 12, weight: isSelected ? .semibold : .regular))
                        .foregroundStyle(colors.navigationContent)
                        .lineLimit(1)
                        .minimumScaleFactor(0.8)
                }
                .padding(.vertical, 4)
                .padding(.horizontal, 8)
            }
            .frame(maxWidth: .infinity)
        }
        .buttonStyle(.plain)
    }
}
