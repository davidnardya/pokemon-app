import Foundation
import SharedLogic

@MainActor
final class PokemonListViewModelWrapper: ObservableObject {
    @Published private(set) var uiState: PokemonListUiState

    private let storeOwner: IosViewModelStoreOwner
    private let viewModel: PokemonListViewModel
    private var cancelObservation: (() -> Void)?

    init() {
        KoinHelper.shared.start()
        let owner = IosViewModelStoreOwner()
        self.storeOwner = owner
        let viewModel = owner.pokemonListViewModel()
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
        storeOwner.clear()
    }

    func loadInitialList() {
        viewModel.loadInitialList()
    }

    func loadNextPage() {
        viewModel.loadNextPage()
    }

    func refreshPokemonList() {
        viewModel.refreshPokemonList()
    }

    func refreshPokemonListAsync() async {
        refreshPokemonList()
        while uiState.isRefreshing {
            try? await Task.sleep(nanoseconds: 50_000_000)
        }
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
        viewModel.openPokemonDetail(pokemonId: pokemon.id)
    }

    func closePokemonDetail() {
        viewModel.closePokemonDetail()
    }

    func closeTeamFullDialog() {
        viewModel.closeTeamFullDialog()
    }

    func isInTeam(pokemonId: Int32) -> Bool {
        uiState.isPokemonInTeam(pokemonId: pokemonId)
    }
}

@MainActor
final class SettingsViewModelWrapper: ObservableObject {
    @Published private(set) var uiState: SettingsUiState

    private let storeOwner: IosViewModelStoreOwner
    private let viewModel: SettingsViewModel
    private var cancelObservation: (() -> Void)?

    init() {
        KoinHelper.shared.start()
        let owner = IosViewModelStoreOwner()
        self.storeOwner = owner
        let viewModel = owner.settingsViewModel()
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
        storeOwner.clear()
    }

    func openSettings() {
        viewModel.openSettings()
    }

    func closeSettings() {
        viewModel.closeSettings()
    }

    func setMetricUnits(useMetric: Bool) {
        viewModel.setMetricUnits(useMetric: useMetric)
    }

    func setUse24HourTime(use24HourTime: Bool) {
        viewModel.setUse24HourTime(use24HourTime: use24HourTime)
    }

    func openModePicker() {
        viewModel.openModePicker()
    }

    func closeModePicker() {
        viewModel.closeModePicker()
    }

    func selectThemeMode(_ themeMode: ThemeMode) {
        viewModel.selectThemeMode(themeMode: themeMode)
        objectWillChange.send()
    }

    func openAbout() {
        viewModel.openAbout()
    }

    func closeAbout() {
        viewModel.closeAbout()
    }
}

@MainActor
final class PokemonDetailViewModelWrapper: ObservableObject {
    @Published private(set) var uiState: PokemonDetailUiState

    private let storeOwner: IosViewModelStoreOwner
    private var cancelObservation: (() -> Void)?

    init(pokemonId: Int32) {
        KoinHelper.shared.start()
        let owner = IosViewModelStoreOwner()
        self.storeOwner = owner
        let viewModel = owner.pokemonDetailViewModel(pokemonId: pokemonId)
        self.uiState = viewModel.currentUiState()

        cancelObservation = viewModel.watchUiState { [weak self] state in
            DispatchQueue.main.async {
                self?.uiState = state
            }
        }
    }

    deinit {
        cancelObservation?()
        storeOwner.clear()
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

extension PokemonListUiState {
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

extension PokemonDetail {
    var typesArray: [PokemonType] {
        Array(toTypesArray())
    }

    var statsArray: [PokemonStatEntry] {
        Array(toStatsArray())
    }

    var abilitiesArray: [String] {
        Array(toAbilitiesArray())
    }
}

extension PokemonStatEntry {
    var displayValue: Int32 {
        statValue()
    }

    var displayLabel: String {
        statLabel()
    }
}
