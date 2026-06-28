import SwiftUI
import SharedLogic

struct PokemonListView: View {
    @StateObject private var viewModel = PokemonListViewModelWrapper()
    @State private var isNearListEnd = false
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
        Group {
            if let selectedPokemon = viewModel.uiState.selectedPokemon {
                PokemonDetailView(
                    pokemon: selectedPokemon,
                    uiState: viewModel.uiState,
                    onBack: viewModel.closePokemonDetail
                )
            } else {
                mainList
            }
        }
        .overlay {
            if viewModel.uiState.showTeamFullDialog {
                FullTeamDialogView(onClose: viewModel.closeTeamFullDialog)
            }
        }
        .overlay {
            if viewModel.uiState.isAboutVisible {
                ZStack {
                    Color.black.opacity(0.45)
                        .ignoresSafeArea()
                        .onTapGesture(perform: viewModel.closeAbout)

                    SettingsAboutView(
                        isDark: isDark,
                        onClose: viewModel.closeAbout
                    )
                }
            }
        }
        .sheet(isPresented: settingsBinding) {
            SettingsDrawerView(
                viewModel: viewModel,
                systemColorScheme: systemColorScheme
            )
            .preferredColorScheme(ThemeAppearance.preferredColorScheme(for: themeMode))
            .presentationDetents([.medium])
            .presentationDragIndicator(.hidden)
        }
        .preferredColorScheme(ThemeAppearance.preferredColorScheme(for: themeMode))
        .onAppear {
            if viewModel.uiState.pokemonArray.isEmpty && !viewModel.uiState.isLoading {
                viewModel.loadInitialList()
            }
        }
    }

    private var settingsBinding: Binding<Bool> {
        Binding(
            get: { viewModel.uiState.isSettingsVisible },
            set: { isVisible in
                if !isVisible {
                    viewModel.closeSettings()
                }
            }
        )
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
