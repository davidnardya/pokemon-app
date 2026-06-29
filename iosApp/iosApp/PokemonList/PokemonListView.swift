import SwiftUI
import SharedLogic

struct PokemonListView: View {
    @StateObject private var listViewModel = PokemonListViewModelWrapper()
    @StateObject private var settingsViewModel = SettingsViewModelWrapper()
    @State private var isNearListEnd = false
    @State private var showAboutPopup = false
    @State private var pokemonScrollPosition = ScrollPosition(edge: .top)
    @Environment(\.colorScheme) private var systemColorScheme

    private var themeMode: ThemeMode {
        settingsViewModel.uiState.settings.themeMode
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

            if settingsViewModel.uiState.isVisible {
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
        .animation(.easeInOut(duration: 0.25), value: settingsViewModel.uiState.isVisible)
        .onAppear {
            if listViewModel.uiState.pokemonArray.isEmpty && !listViewModel.uiState.isLoading {
                listViewModel.loadInitialList()
            }
        }
    }

    private var appContent: some View {
        ZStack {
            mainList
                .opacity(listViewModel.uiState.selectedPokemonId == nil ? 1 : 0)
                .allowsHitTesting(listViewModel.uiState.selectedPokemonId == nil)

            if let selectedPokemonId = listViewModel.uiState.selectedPokemonId?.int32Value {
                if let selectedPokemon = listViewModel.uiState.selectedPokemonOrNull() {
                    PokemonDetailContainerView(
                        pokemon: selectedPokemon,
                        themeMode: themeMode,
                        onBack: listViewModel.closePokemonDetail,
                        onSettings: settingsViewModel.openSettings
                    )
                    .id(selectedPokemonId)
                } else {
                    PokemonDetailErrorView(
                        message: "This Pokémon is no longer available in the list.",
                        themeMode: themeMode,
                        onBack: listViewModel.closePokemonDetail
                    )
                }
            }
        }
        .overlay {
            if listViewModel.uiState.showTeamFullDialog {
                FullTeamDialogView(onClose: listViewModel.closeTeamFullDialog)
            }
        }
    }

    private var settingsDrawerLayer: some View {
        GeometryReader { geometry in
            ZStack(alignment: .bottom) {
                Color.black.opacity(0.4)
                    .ignoresSafeArea()
                    .onTapGesture { settingsViewModel.closeSettings() }

                SettingsDrawerView(
                    viewModel: settingsViewModel,
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
                selectedTab: listViewModel.uiState.selectedTab,
                onPokemonTap: listViewModel.selectPokemonTab,
                onTeamTap: listViewModel.selectTeamTab
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

            Button(action: settingsViewModel.openSettings) {
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
        if listViewModel.uiState.selectedTabIsTeam {
            return "Team \(listViewModel.uiState.teamCount)/\(PokemonListUiState.companion.MAX_TEAM_SIZE)"
        }
        return "Pokemon"
    }

    @ViewBuilder
    private var content: some View {
        PokemonLoadingView(
            uiState: listViewModel.uiState,
            isDark: isDark,
            onRefresh: listViewModel.uiState.selectedTabIsPokemon
                ? { await listViewModel.refreshPokemonListAsync() }
                : nil
        ) {
            ZStack {
                pokemonGrid
                    .opacity(listViewModel.uiState.selectedTabIsPokemon ? 1 : 0)
                    .allowsHitTesting(listViewModel.uiState.selectedTabIsPokemon)

                if listViewModel.uiState.selectedTabIsTeam {
                    teamList
                }
            }
        }
    }

    private var teamList: some View {
        TeamListView(
            teamItems: listViewModel.uiState.teamPokemonItemsArray,
            teamCount: Int(listViewModel.uiState.teamCount),
            onRemoveFromTeam: listViewModel.toggleTeamMembership(pokemonId:),
            onAddMoreTap: listViewModel.selectPokemonTab
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
                ForEach(Array(listViewModel.uiState.displayedPokemonArray.enumerated()), id: \.element.id) { index, pokemon in
                    PokemonCardView(
                        pokemon: pokemon,
                        isInTeam: listViewModel.isInTeam(pokemonId: pokemon.id),
                        onPokemonTap: { listViewModel.openPokemonDetail(pokemon: pokemon) },
                        onPokeballTap: { listViewModel.toggleTeamMembership(pokemonId: pokemon.id) }
                    )
                    .onAppear {
                        if listViewModel.uiState.selectedTabIsPokemon,
                           index >= listViewModel.uiState.displayedPokemonArray.count - 2 {
                            isNearListEnd = true
                            loadNextPageIfNeeded()
                        }
                    }
                }

                if listViewModel.uiState.hasMore && listViewModel.uiState.selectedTabIsPokemon {
                    paginationTrigger
                }
            }
            .padding(.horizontal, 16)
            .padding(.top, 8)
            .padding(.bottom, IOSBottomNavigationMetrics.contentBottomInset)
        }
        .scrollPosition($pokemonScrollPosition)
        .scrollDisabled(!listViewModel.uiState.selectedTabIsPokemon)
        .refreshable {
            await listViewModel.refreshPokemonListAsync()
        }
        .onChange(of: listViewModel.uiState.isLoading) { _, isLoading in
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
        let state = listViewModel.uiState
        guard state.selectedTabIsPokemon, !state.isLoading, !state.isRefreshing, state.hasMore else { return }
        listViewModel.loadNextPage()
    }
}

#Preview {
    PokemonListView()
}
