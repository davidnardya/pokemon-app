import SwiftUI
import SharedLogic

struct PokemonDetailView: View {
    @Environment(\.colorScheme) private var colorScheme

    let pokemon: Pokemon
    let onBack: () -> Void

    private var colors: PokemonListColors {
        PokemonListColors.current(for: colorScheme)
    }

    private var isDark: Bool {
        colorScheme == .dark
    }

    var body: some View {
        NavigationView {
            VStack(spacing: 24) {
                AsyncImage(url: URL(string: PokemonImageURL.forId(pokemon.id))) { phase in
                    switch phase {
                    case .success(let image):
                        image
                            .resizable()
                            .scaledToFit()
                            .padding(16)
                    default:
                        ProgressView()
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
}

#Preview {
    PokemonDetailView(
        pokemon: Pokemon(id: 1, name: "bulbasaur"),
        onBack: {}
    )
}
