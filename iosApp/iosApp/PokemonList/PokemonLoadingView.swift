import SwiftUI
import SharedLogic

struct PokemonLoadingView<Content: View>: View {
    let isLoading: Bool
    let networkError: Bool
    let isCacheEmpty: Bool
    let isDark: Bool
    @ViewBuilder let content: () -> Content

    init(
        uiState: PokemonListState,
        isDark: Bool,
        @ViewBuilder content: @escaping () -> Content
    ) {
        self.isLoading = uiState.isLoading
        self.networkError = uiState.networkError
        self.isCacheEmpty = uiState.isCacheEmpty
        self.isDark = isDark
        self.content = content
    }

    init(
        isLoading: Bool,
        networkError: Bool,
        isCacheEmpty: Bool,
        isDark: Bool,
        @ViewBuilder content: @escaping () -> Content
    ) {
        self.isLoading = isLoading
        self.networkError = networkError
        self.isCacheEmpty = isCacheEmpty
        self.isDark = isDark
        self.content = content
    }

    var body: some View {
        if isLoading && isCacheEmpty {
            SpinningPokeballView()
                .frame(maxWidth: .infinity, maxHeight: .infinity)
        } else if networkError && isCacheEmpty {
            Image(PokemonAssets.loadingList(isDark: isDark))
                .resizable()
                .scaledToFit()
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .padding(.horizontal, 16)
        } else {
            content()
        }
    }
}

struct SpinningPokeballView: View {
    @State private var isAnimating = false

    var body: some View {
        Image(PokemonAssets.loadingPokeball)
            .renderingMode(.original)
            .resizable()
            .scaledToFit()
            .frame(width: 64, height: 64)
            .rotationEffect(.degrees(isAnimating ? 360 : 0))
            .animation(
                .linear(duration: 1).repeatForever(autoreverses: false),
                value: isAnimating
            )
            .onAppear {
                isAnimating = true
            }
    }
}
