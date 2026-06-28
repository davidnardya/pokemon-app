import SwiftUI
import SharedLogic

struct PokemonLoadingView<Content: View>: View {
    let isLoading: Bool
    let networkError: Bool
    let isCacheEmpty: Bool
    let isDark: Bool
    let onRefresh: (() async -> Void)?
    @ViewBuilder let content: () -> Content

    init(
        uiState: PokemonListState,
        isDark: Bool,
        onRefresh: (() async -> Void)? = nil,
        @ViewBuilder content: @escaping () -> Content
    ) {
        self.isLoading = uiState.isLoading
        self.networkError = uiState.networkError
        self.isCacheEmpty = uiState.isCacheEmpty
        self.isDark = isDark
        self.onRefresh = onRefresh
        self.content = content
    }

    init(
        isLoading: Bool,
        networkError: Bool,
        isCacheEmpty: Bool,
        isDark: Bool,
        onRefresh: (() async -> Void)? = nil,
        @ViewBuilder content: @escaping () -> Content
    ) {
        self.isLoading = isLoading
        self.networkError = networkError
        self.isCacheEmpty = isCacheEmpty
        self.isDark = isDark
        self.onRefresh = onRefresh
        self.content = content
    }

    var body: some View {
        if isLoading && isCacheEmpty {
            refreshableEmptyState {
                SpinningPokeballView()
            }
        } else if networkError && isCacheEmpty {
            refreshableEmptyState {
                Image(PokemonAssets.loadingList(isDark: isDark))
                    .resizable()
                    .scaledToFit()
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                    .padding(.horizontal, 16)
            }
        } else {
            content()
        }
    }

    @ViewBuilder
    private func refreshableEmptyState<EmptyContent: View>(
        @ViewBuilder _ emptyContent: @escaping () -> EmptyContent
    ) -> some View {
        if let onRefresh {
            GeometryReader { geometry in
                ScrollView {
                    emptyContent()
                        .frame(maxWidth: .infinity, minHeight: geometry.size.height)
                }
                .refreshable {
                    await onRefresh()
                }
            }
        } else {
            emptyContent()
                .frame(maxWidth: .infinity, maxHeight: .infinity)
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
