import Foundation
import SharedLogic

@MainActor
final class PokemonListViewModelWrapper: ObservableObject {
    @Published private(set) var uiState: PokemonListState
    @Published var showTeamFullError = false

    private let viewModel: PokemonViewModel
    private var cancelObservation: (() -> Void)?
    private var cancelSideEffects: (() -> Void)?

    init() {
        KoinHelper.shared.start()
        let viewModel = KoinHelper.shared.pokemonViewModel()
        self.viewModel = viewModel
        self.uiState = viewModel.currentUiState()

        cancelObservation = viewModel.watchUiState { [weak self] state in
            Task { @MainActor in
                self?.uiState = state
            }
        }

        cancelSideEffects = viewModel.watchTeamFullError { [weak self] in
            Task { @MainActor in
                self?.showTeamFullError = true
            }
        }
    }

    deinit {
        cancelObservation?()
        cancelSideEffects?()
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

    func toggleTheme() {
        viewModel.toggleTheme()
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

    var teamIds: [Int32] {
        teamPokemonIds.map { $0.int32Value }
    }
}
