import Foundation
import SharedLogic

@MainActor
final class PokemonListViewModelWrapper: ObservableObject {
    @Published private(set) var uiState: PokemonListState

    private let viewModel: PokemonViewModel
    private var cancelObservation: (() -> Void)?

    init() {
        KoinHelper.shared.start()
        let viewModel = KoinHelper.shared.pokemonViewModel()
        self.viewModel = viewModel
        self.uiState = viewModel.currentUiState()

        cancelObservation = viewModel.watchUiState { [weak self] state in
            DispatchQueue.main.async {
                self?.uiState = state
            }
        }
    }

    deinit {
        cancelObservation?()
    }

    func loadInitialList() {
        viewModel.loadInitialList()
    }

    func loadNextPage() {
        viewModel.requestNextPage()
    }

    func requestNextPage() {
        viewModel.requestNextPage()
    }

    func openSettings() {
        viewModel.openSettings()
    }

    func closeSettings() {
        viewModel.closeSettings()
    }

    func selectPokemonTab() {
        viewModel.selectTab(tab: .pokemon)
    }

    func selectTeamTab() {
        viewModel.selectTab(tab: .team)
    }

    func toggleTeamMembership(pokemonId: Int32) {
        viewModel.toggleTeamMembership(pokemonId: pokemonId)
    }

    func openPokemonDetail(pokemon: Pokemon) {
        viewModel.openPokemonDetail(pokemon: pokemon)
    }

    func closePokemonDetail() {
        viewModel.closePokemonDetail()
    }

    func toggleMetricUnits() {
        viewModel.toggleMetricUnits()
    }

    func toggleTimeFormat() {
        viewModel.toggleTimeFormat()
    }

    func setMetricUnits(useMetric: Bool) {
        viewModel.setMetricUnits(useMetric: useMetric)
        uiState = viewModel.currentUiState()
    }

    func setUse24HourTime(use24HourTime: Bool) {
        viewModel.setUse24HourTime(use24HourTime: use24HourTime)
        uiState = viewModel.currentUiState()
    }

    func openModePicker() {
        viewModel.openModePicker()
        uiState = viewModel.currentUiState()
    }

    func closeModePicker() {
        viewModel.closeModePicker()
        uiState = viewModel.currentUiState()
    }

    func selectThemeMode(_ themeMode: ThemeMode) {
        viewModel.selectThemeMode(themeMode: themeMode)
        uiState = viewModel.currentUiState()
        objectWillChange.send()
    }

    func openAbout() {
        viewModel.openAbout()
        uiState = viewModel.currentUiState()
    }

    func closeAbout() {
        viewModel.closeAbout()
        uiState = viewModel.currentUiState()
    }

    func closeTeamFullDialog() {
        viewModel.closeTeamFullDialog()
    }

    func isInTeam(pokemonId: Int32) -> Bool {
        uiState.isPokemonInTeam(pokemonId: pokemonId)
    }
}

extension Pokemon {
    var displayName: String {
        name.prefix(1).uppercased() + name.dropFirst()
    }

    var formattedId: String {
        String(format: "#%03d", id)
    }
}

extension PokemonListState {
    var selectedTabIsPokemon: Bool {
        selectedTab == .pokemon
    }

    var selectedTabIsTeam: Bool {
        selectedTab == .team
    }

    var pokemonArray: [Pokemon] {
        Array(toPokemonArray())
    }

    var displayedPokemonArray: [Pokemon] {
        Array(displayedPokemon())
    }

    var teamPokemonItemsArray: [TeamPokemonItem] {
        Array(toTeamPokemonItemsArray())
    }

    var teamIds: [Int32] {
        teamPokemonIds.map { $0.int32Value }
    }
}
