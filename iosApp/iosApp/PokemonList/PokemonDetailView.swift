import SwiftUI
import SharedLogic

struct PokemonDetailView: View {
    @Environment(\.colorScheme) private var systemColorScheme

    let pokemon: Pokemon
    let uiState: PokemonListState
    let onBack: () -> Void

    private var effectiveColorScheme: ColorScheme {
        ThemeAppearance.effectiveColorScheme(
            themeMode: uiState.settings.themeMode,
            systemColorScheme: systemColorScheme
        )
    }

    private var colors: PokemonListColors {
        PokemonListColors.current(for: effectiveColorScheme)
    }

    private var isDark: Bool {
        ThemeAppearance.isDark(
            themeMode: uiState.settings.themeMode,
            systemColorScheme: systemColorScheme
        )
    }

    var body: some View {
        NavigationView {
            PokemonLoadingView(
                uiState: uiState,
                isDark: isDark
            ) {
                detailContent
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .background(colors.screenBackground.ignoresSafeArea())
            .navigationTitle(pokemon.displayName)
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button(action: onBack) {
                        HStack(spacing: 4) {
                            Image(systemName: "chevron.left")
                            Text("Back")
                        }
                        .font(.system(size: 17, weight: .medium))
                        .foregroundStyle(colors.primaryText)
                    }
                }
            }
            .toolbarBackground(colors.screenBackground, for: .navigationBar)
            .toolbarBackground(.visible, for: .navigationBar)
        }
        .navigationViewStyle(.stack)
    }

    private var detailContent: some View {
        VStack(spacing: 24) {
            AsyncImage(url: URL(string: PokemonImageURL.forId(pokemon.id))) { phase in
                switch phase {
                case .success(let image):
                    image
                        .resizable()
                        .scaledToFit()
                        .padding(16)
                default:
                    Color.clear
                }
            }
            .frame(maxWidth: .infinity)
            .aspectRatio(1, contentMode: .fit)
            .background(colors.cardContainer)
            .clipShape(RoundedRectangle(cornerRadius: 24, style: .continuous))
            .padding(.horizontal, 24)

            VStack(spacing: 4) {
                Text(pokemon.displayName)
                    .font(.system(size: 28, weight: .bold))
                    .foregroundStyle(colors.primaryText)

                Text(pokemon.formattedId)
                    .font(.system(size: 18, weight: .medium))
                    .foregroundStyle(colors.secondaryText)
            }

            Spacer()
        }
        .padding(.top, 16)
    }
}

#Preview {
    PokemonDetailView(
        pokemon: Pokemon(id: 1, name: "bulbasaur"),
        uiState: PokemonListState(
            isLoading: false,
            pokemon: [],
            offset: 0,
            hasMore: true,
            error: nil,
            networkError: false,
            selectedTab: .pokemon,
            isSettingsVisible: false,
            isModePickerVisible: false,
            isAboutVisible: false,
            teamMembers: [],
            pokemonTypes: [:],
            selectedPokemon: nil,
            settings: AppSettings(useMetricUnits: true, use24HourTime: false, themeMode: .system),
            showTeamFullDialog: false
        ),
        onBack: {}
    )
}
