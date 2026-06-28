import SwiftUI
import SharedLogic

struct PokemonListView: View {
    @StateObject private var viewModel = PokemonListViewModelWrapper()
    @State private var isNearListEnd = false
    @State private var showAboutPopup = false
    @Environment(\.colorScheme) private var systemColorScheme

    private var themeMode: ThemeMode {
        viewModel.uiState.settings.themeMode
    }

    private var effectiveColorScheme: ColorScheme {
        ThemeAppearance.effectiveColorScheme(
            themeMode: themeMode,
            systemColorScheme: systemColorScheme
        )
    }

    private var colors: PokemonListColors {
        PokemonListColors.current(for: effectiveColorScheme)
    }

    private var isDark: Bool {
        ThemeAppearance.isDark(themeMode: themeMode, systemColorScheme: systemColorScheme)
    }

    var body: some View {
        ZStack {
            appContent

            if viewModel.uiState.isSettingsVisible {
                settingsDrawerLayer
            }

            if showAboutPopup {
                Color.black.opacity(0.4)
                    .ignoresSafeArea()
                    .onTapGesture { showAboutPopup = false }
            }

            if showAboutPopup {
                SettingsAboutView(
                    isDark: isDark,
                    onClose: { showAboutPopup = false }
                )
                .frame(width: 320)
                .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
            }
        }
        .preferredColorScheme(ThemeAppearance.preferredColorScheme(for: themeMode))
        .animation(.easeInOut(duration: 0.25), value: viewModel.uiState.isSettingsVisible)
        .onAppear {
            if viewModel.uiState.pokemonArray.isEmpty && !viewModel.uiState.isLoading {
                viewModel.loadInitialList()
            }
        }
    }

    private var appContent: some View {
        Group {
            if let selectedPokemonId = viewModel.uiState.selectedPokemonId?.int32Value {
                if let selectedPokemon = viewModel.uiState.selectedPokemonOrNull() {
                    PokemonDetailView(
                        pokemon: selectedPokemon,
                        detail: viewModel.uiState.detailForPokemon(pokemonId: selectedPokemonId),
                        isDetailLoading: viewModel.uiState.isDetailLoading,
                        detailLoadFailed: viewModel.uiState.detailLoadFailed,
                        useMetricUnits: viewModel.uiState.settings.useMetricUnits,
                        themeMode: themeMode,
                        onBack: viewModel.closePokemonDetail,
                        onSettings: viewModel.openSettings
                    )
                } else {
                    PokemonDetailErrorView(
                        message: "This Pokémon is no longer available in the list.",
                        themeMode: themeMode,
                        onBack: viewModel.closePokemonDetail
                    )
                }
            } else {
                mainList
            }
        }
        .overlay {
            if viewModel.uiState.showTeamFullDialog {
                FullTeamDialogView(onClose: viewModel.closeTeamFullDialog)
            }
        }
    }

    private var settingsDrawerLayer: some View {
        GeometryReader { geometry in
            ZStack(alignment: .bottom) {
                Color.black.opacity(0.4)
                    .ignoresSafeArea()
                    .onTapGesture { viewModel.closeSettings() }

                SettingsDrawerView(
                    viewModel: viewModel,
                    systemColorScheme: systemColorScheme,
                    onAboutTap: { showAboutPopup = true }
                )
                .preferredColorScheme(ThemeAppearance.preferredColorScheme(for: themeMode))
                .frame(maxWidth: .infinity)
                .frame(height: geometry.size.height * 0.5)
                .clipShape(
                    UnevenRoundedRectangle(
                        topLeadingRadius: 20,
                        bottomLeadingRadius: 0,
                        bottomTrailingRadius: 0,
                        topTrailingRadius: 20,
                        style: .continuous
                    )
                )
            }
        }
        .ignoresSafeArea()
    }

    private var mainList: some View {
        ZStack(alignment: .bottom) {
            colors.screenBackground
                .ignoresSafeArea()

            VStack(spacing: 0) {
                topBar
                content
            }

            IOSBottomNavigationView(
                selectedTab: viewModel.uiState.selectedTab,
                onPokemonTap: viewModel.selectPokemonTab,
                onTeamTap: viewModel.selectTeamTab
            )
            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .bottom)
        }
    }

    private var topBar: some View {
        HStack {
            Text(screenTitle)
                .font(.system(size: 34, weight: .bold))
                .foregroundStyle(colors.primaryText)

            Spacer()

            Button(action: viewModel.openSettings) {
                Image(PokemonAssets.settings(isDark: isDark))
                    .renderingMode(.original)
                    .resizable()
                    .scaledToFit()
                    .frame(width: 28, height: 28)
                    .padding(10)
                    .background(colors.settingsButtonBackground)
                    .clipShape(Circle())
            }
            .buttonStyle(.plain)
        }
        .padding(.horizontal, 20)
        .padding(.top, 8)
        .padding(.bottom, 12)
        .background(colors.screenBackground)
    }

    private var screenTitle: String {
        if viewModel.uiState.selectedTabIsTeam {
            return "My Team (\(viewModel.uiState.teamCount)/\(PokemonListState.companion.MAX_TEAM_SIZE))"
        }
        return "Pokemon"
    }

    @ViewBuilder
    private var content: some View {
        PokemonLoadingView(
            uiState: viewModel.uiState,
            isDark: isDark
        ) {
            if viewModel.uiState.selectedTabIsTeam {
                teamList
            } else {
                pokemonGrid
            }
        }
    }

    private var teamList: some View {
        TeamListView(
            teamItems: viewModel.uiState.teamPokemonItemsArray,
            teamCount: Int(viewModel.uiState.teamCount),
            onRemoveFromTeam: viewModel.toggleTeamMembership(pokemonId:),
            onAddMoreTap: viewModel.selectPokemonTab
        )
    }

    private var pokemonGrid: some View {
        ScrollView {
            LazyVGrid(
                columns: [
                    GridItem(.flexible(), spacing: 12),
                    GridItem(.flexible(), spacing: 12),
                ],
                spacing: 12
            ) {
                ForEach(Array(viewModel.uiState.displayedPokemonArray.enumerated()), id: \.element.id) { index, pokemon in
                    PokemonCardView(
                        pokemon: pokemon,
                        isInTeam: viewModel.isInTeam(pokemonId: pokemon.id),
                        onPokemonTap: { viewModel.openPokemonDetail(pokemon: pokemon) },
                        onPokeballTap: { viewModel.toggleTeamMembership(pokemonId: pokemon.id) }
                    )
                    .onAppear {
                        if viewModel.uiState.selectedTabIsPokemon,
                           index >= viewModel.uiState.displayedPokemonArray.count - 2 {
                            isNearListEnd = true
                            loadNextPageIfNeeded()
                        }
                    }
                }

                if viewModel.uiState.hasMore && viewModel.uiState.selectedTabIsPokemon {
                    paginationTrigger
                }
            }
            .padding(.horizontal, 16)
            .padding(.top, 8)
            .padding(.bottom, IOSBottomNavigationMetrics.contentBottomInset)
        }
        .onChange(of: viewModel.uiState.isLoading) { _, isLoading in
            if !isLoading && isNearListEnd {
                loadNextPageIfNeeded()
            }
        }
    }

    private var paginationTrigger: some View {
        Color.clear
            .frame(height: 1)
            .gridCellColumns(2)
            .onAppear {
                isNearListEnd = true
                loadNextPageIfNeeded()
            }
    }

    private func loadNextPageIfNeeded() {
        let state = viewModel.uiState
        guard state.selectedTabIsPokemon, !state.isLoading, state.hasMore else { return }
        viewModel.loadNextPage()
    }
}

#Preview {
    PokemonListView()
}
