import SwiftUI
import SharedLogic

private enum DetailLayout {
    static let imageHeight: CGFloat = 200
    static let cardTopCornerRadius: CGFloat = 32
    static let cardOverlap: CGFloat = 24
}

struct PokemonDetailView: View {
    @Environment(\.colorScheme) private var systemColorScheme

    let pokemon: Pokemon
    let detail: PokemonDetail?
    let isDetailLoading: Bool
    let detailLoadFailed: Bool
    let useMetricUnits: Bool
    let themeMode: ThemeMode
    let onBack: () -> Void
    let onSettings: () -> Void

    private var isDark: Bool {
        ThemeAppearance.isDark(themeMode: themeMode, systemColorScheme: systemColorScheme)
    }

    private var primaryType: PokemonType {
        detail?.typesArray.first ?? .normal
    }

    private var typePalette: TypeColorPalette {
        PokemonTypeColors.palette(for: primaryType)
    }

    var body: some View {
        GeometryReader { geometry in
            ZStack(alignment: .top) {
                PokemonTypeColors.cardBackground(isDark: isDark)
                    .ignoresSafeArea()

                Color(argb: typePalette.headerBackground)
                    .frame(height: geometry.size.height * 0.42)
                    .frame(maxWidth: .infinity, alignment: .top)
                    .ignoresSafeArea(edges: .top)

                VStack(spacing: 0) {
                    headerSection
                        .frame(height: geometry.size.height * 0.42)

                    infoCardSection
                        .frame(maxHeight: .infinity)
                }
            }
            .ignoresSafeArea(edges: .top)
        }
    }

    private var headerSection: some View {
        ZStack(alignment: .bottom) {
            VStack {
                headerControls
                    .padding(.horizontal, 16)
                    .padding(.top, 8)
                Spacer()
            }

            AsyncImage(url: URL(string: PokemonImageURL.forId(pokemon.id))) { phase in
                switch phase {
                case .success(let image):
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                default:
                    Color.clear
                }
            }
            .frame(height: DetailLayout.imageHeight)
            .frame(maxWidth: .infinity)
            .padding(.bottom, DetailLayout.cardOverlap)
        }
    }

    private var headerControls: some View {
        HStack {
            detailCircleButton(action: onBack) {
                Image(systemName: "chevron.left")
                    .font(.system(size: 18, weight: .semibold))
                    .foregroundStyle(.white)
            }

            Spacer()

            detailCircleButton(action: onSettings) {
                Image(PokemonAssets.settings(isDark: true))
                    .renderingMode(.template)
                    .foregroundStyle(.white)
                    .frame(width: 20, height: 20)
            }
        }
        .padding(.top, safeAreaTopInset)
    }

    private var safeAreaTopInset: CGFloat {
        UIApplication.shared.connectedScenes
            .compactMap { ($0 as? UIWindowScene)?.keyWindow?.safeAreaInsets.top }
            .first ?? 0
    }

    private var infoCardSection: some View {
        ScrollView(showsIndicators: false) {
            if let detail {
                detailCardContent(detail: detail)
            } else if isDetailLoading {
                ProgressView()
                    .frame(maxWidth: .infinity, minHeight: 240)
            } else if detailLoadFailed {
                Text("Unable to load Pokémon details.\nCheck your connection and try again.")
                    .font(.system(size: 16, weight: .medium))
                    .multilineTextAlignment(.center)
                    .foregroundStyle(Color(red: 0.643, green: 0.643, blue: 0.643))
                    .frame(maxWidth: .infinity, minHeight: 240)
            }
        }
        .background(
            PokemonTypeColors.cardBackground(isDark: isDark)
                .clipShape(
                    RoundedRectangle(
                        cornerRadius: DetailLayout.cardTopCornerRadius,
                        style: .continuous
                    )
                )
                .shadow(color: .black.opacity(isDark ? 0.25 : 0.08), radius: 12, y: -4)
        )
        .padding(.top, -DetailLayout.cardOverlap)
    }

    private func detailCardContent(detail: PokemonDetail) -> some View {
        let accentColor = Color(argb: typePalette.accent)
        let tileBackground = PokemonTypeColors.tileBackground(isDark: isDark)
        let borderColor = isDark ? Color(red: 0.165, green: 0.165, blue: 0.165) : Color(red: 0.91, green: 0.91, blue: 0.91)
        let labelColor = Color(red: 0.643, green: 0.643, blue: 0.643)
        let primaryText = isDark ? Color.white : Color(red: 0.114, green: 0.114, blue: 0.114)
        let iconBackground = accentColor.opacity(isDark ? 0.35 : 0.18)
        let iconTint = isDark ? Color.white : accentColor

        return VStack(alignment: .leading, spacing: 16) {
            HStack(alignment: .center) {
                HStack(spacing: 8) {
                    ForEach(Array(detail.typesArray.enumerated()), id: \.offset) { _, type in
                        detailTypeBadge(type: type)
                    }
                }
                Spacer()
                Text(pokemon.formattedId)
                    .font(.system(size: 14, weight: .medium))
                    .foregroundStyle(labelColor)
            }

            Text(pokemon.displayName)
                .font(.system(size: 28, weight: .bold))
                .foregroundStyle(primaryText)

            HStack(spacing: 12) {
                PokemonDetailInfoTile(
                    label: "Weight",
                    value: PokemonDetailFormatter.shared.formatWeight(
                        weightHectograms: detail.weightHectograms,
                        useMetricUnits: useMetricUnits
                    ),
                    iconName: PokemonAssets.weight(isDark: isDark),
                    tileBackground: tileBackground,
                    borderColor: borderColor,
                    labelColor: labelColor,
                    valueColor: primaryText,
                    iconBackground: iconBackground,
                    iconTint: iconTint
                )

                PokemonDetailInfoTile(
                    label: "Height",
                    value: PokemonDetailFormatter.shared.formatHeight(
                        heightDecimeters: detail.heightDecimeters,
                        useMetricUnits: useMetricUnits
                    ),
                    iconName: PokemonAssets.height(isDark: isDark),
                    tileBackground: tileBackground,
                    borderColor: borderColor,
                    labelColor: labelColor,
                    valueColor: primaryText,
                    iconBackground: iconBackground,
                    iconTint: iconTint
                )
            }

            if !detail.abilitiesArray.isEmpty {
                PokemonDetailMovesTile(
                    label: "Moves",
                    moves: detail.abilitiesArray,
                    iconName: PokemonAssets.moves(isDark: isDark),
                    tileBackground: tileBackground,
                    borderColor: borderColor,
                    labelColor: labelColor,
                    valueColor: primaryText,
                    iconBackground: iconBackground,
                    iconTint: iconTint
                )
            }

            if !detail.statsArray.isEmpty {
                PokemonDetailStatsSection(
                    stats: detail.statsArray,
                    accentColor: accentColor,
                    tileBackground: tileBackground,
                    borderColor: borderColor,
                    labelColor: labelColor,
                    valueColor: primaryText,
                    trackColor: PokemonTypeColors.statTrack(isDark: isDark)
                )
            }
        }
        .padding(.horizontal, 20)
        .padding(.top, 28)
        .padding(.bottom, 24)
    }

    private func detailTypeBadge(type: PokemonType) -> some View {
        Text(type.displayName)
            .font(.system(size: 12, weight: .semibold))
            .foregroundStyle(PokemonTypeColors.badgeText(for: type, isDark: isDark))
            .padding(.horizontal, 10)
            .padding(.vertical, 4)
            .background(PokemonTypeColors.badgeBackground(for: type, isDark: isDark))
            .clipShape(RoundedRectangle(cornerRadius: 8, style: .continuous))
    }

    private func detailCircleButton<Content: View>(
        action: @escaping () -> Void,
        @ViewBuilder content: () -> Content
    ) -> some View {
        Button(action: action) {
            content()
                .frame(width: 40, height: 40)
                .background(Color.black.opacity(0.25))
                .clipShape(Circle())
        }
        .buttonStyle(.plain)
    }
}

private struct PokemonDetailInfoTile: View {
    let label: String
    let value: String
    let iconName: String
    let tileBackground: Color
    let borderColor: Color
    let labelColor: Color
    let valueColor: Color
    let iconBackground: Color
    let iconTint: Color

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            HStack {
                Text(label)
                    .font(.system(size: 13, weight: .medium))
                    .foregroundStyle(labelColor)
                Spacer()
                Image(iconName)
                    .renderingMode(.template)
                    .foregroundStyle(iconTint)
                    .frame(width: 16, height: 16)
                    .padding(6)
                    .background(iconBackground)
                    .clipShape(Circle())
            }

            Text(value)
                .font(.system(size: 16, weight: .bold))
                .foregroundStyle(valueColor)
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 14)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(tileBackground)
        .overlay(
            RoundedRectangle(cornerRadius: 16, style: .continuous)
                .stroke(borderColor, lineWidth: 1)
        )
        .clipShape(RoundedRectangle(cornerRadius: 16, style: .continuous))
    }
}

private struct PokemonDetailMovesTile: View {
    let label: String
    let moves: [String]
    let iconName: String
    let tileBackground: Color
    let borderColor: Color
    let labelColor: Color
    let valueColor: Color
    let iconBackground: Color
    let iconTint: Color

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            HStack {
                Text(label)
                    .font(.system(size: 13, weight: .medium))
                    .foregroundStyle(labelColor)
                Spacer()
                Image(iconName)
                    .renderingMode(.template)
                    .foregroundStyle(iconTint)
                    .frame(width: 16, height: 16)
                    .padding(6)
                    .background(iconBackground)
                    .clipShape(Circle())
            }

            VStack(alignment: .leading, spacing: 4) {
                ForEach(Array(moves.enumerated()), id: \.offset) { _, move in
                    Text(move)
                        .font(.system(size: 16, weight: .bold))
                        .foregroundStyle(valueColor)
                }
            }
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 14)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(tileBackground)
        .overlay(
            RoundedRectangle(cornerRadius: 16, style: .continuous)
                .stroke(borderColor, lineWidth: 1)
        )
        .clipShape(RoundedRectangle(cornerRadius: 16, style: .continuous))
    }
}

private struct PokemonDetailStatsSection: View {
    let stats: [PokemonStatEntry]
    let accentColor: Color
    let tileBackground: Color
    let borderColor: Color
    let labelColor: Color
    let valueColor: Color
    let trackColor: Color

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Base Stats")
                .font(.system(size: 13, weight: .medium))
                .foregroundStyle(labelColor)

            VStack(spacing: 10) {
                ForEach(Array(stats.enumerated()), id: \.offset) { _, stat in
                    PokemonDetailStatBar(
                        label: stat.displayLabel,
                        value: Int(stat.displayValue),
                        maxValue: 150,
                        accentColor: accentColor,
                        trackColor: trackColor,
                        labelColor: valueColor,
                        valueTextColor: labelColor
                    )
                }
            }
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 14)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(tileBackground)
        .overlay(
            RoundedRectangle(cornerRadius: 16, style: .continuous)
                .stroke(borderColor, lineWidth: 1)
        )
        .clipShape(RoundedRectangle(cornerRadius: 16, style: .continuous))
    }
}

private struct PokemonDetailStatBar: View {
    let label: String
    let value: Int
    let maxValue: Int
    let accentColor: Color
    let trackColor: Color
    let labelColor: Color
    let valueTextColor: Color

    var body: some View {
        HStack(spacing: 8) {
            Text(label)
                .font(.system(size: 13, weight: .bold))
                .foregroundStyle(labelColor)
                .frame(width: 44, alignment: .leading)

            GeometryReader { geometry in
                ZStack(alignment: .leading) {
                    Capsule()
                        .fill(trackColor)
                    Capsule()
                        .fill(accentColor)
                        .frame(width: geometry.size.width * progress)
                }
            }
            .frame(height: 8)

            Text("\(value)")
                .font(.system(size: 13, weight: .medium))
                .foregroundStyle(valueTextColor)
                .frame(width: 36, alignment: .trailing)
        }
        .frame(height: 16)
    }

    private var progress: CGFloat {
        CGFloat(min(max(Double(value) / Double(maxValue), 0), 1))
    }
}

#Preview {
    PokemonDetailView(
        pokemon: Pokemon(id: 1, name: "bulbasaur"),
        detail: nil,
        isDetailLoading: true,
        detailLoadFailed: false,
        useMetricUnits: true,
        themeMode: .system,
        onBack: {},
        onSettings: {}
    )
}

struct PokemonDetailErrorView: View {
    @Environment(\.colorScheme) private var systemColorScheme

    let message: String
    let themeMode: ThemeMode
    let onBack: () -> Void

    private var colors: PokemonListColors {
        PokemonListColors.current(
            for: ThemeAppearance.effectiveColorScheme(
                themeMode: themeMode,
                systemColorScheme: systemColorScheme
            )
        )
    }

    var body: some View {
        VStack(spacing: 24) {
            HStack {
                Button(action: onBack) {
                    Image(systemName: "chevron.left")
                        .font(.system(size: 18, weight: .semibold))
                        .foregroundStyle(colors.primaryText)
                }
                Spacer()
            }

            Spacer()

            Text(message)
                .font(.system(size: 16, weight: .medium))
                .multilineTextAlignment(.center)
                .foregroundStyle(colors.secondaryText)
                .padding(.horizontal, 24)

            Spacer()
        }
        .padding(.horizontal, 16)
        .padding(.top, 8)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(colors.screenBackground.ignoresSafeArea())
    }
}

struct PokemonDetailContainerView: View {
    let pokemon: Pokemon
    let themeMode: ThemeMode
    let onBack: () -> Void
    let onSettings: () -> Void

    @StateObject private var detailViewModel: PokemonDetailViewModelWrapper

    init(
        pokemon: Pokemon,
        themeMode: ThemeMode,
        onBack: @escaping () -> Void,
        onSettings: @escaping () -> Void
    ) {
        self.pokemon = pokemon
        self.themeMode = themeMode
        self.onBack = onBack
        self.onSettings = onSettings
        _detailViewModel = StateObject(
            wrappedValue: PokemonDetailViewModelWrapper(pokemonId: pokemon.id)
        )
    }

    var body: some View {
        PokemonDetailView(
            pokemon: pokemon,
            detail: detailViewModel.uiState.detail,
            isDetailLoading: detailViewModel.uiState.isLoading,
            detailLoadFailed: detailViewModel.uiState.loadFailed,
            useMetricUnits: detailViewModel.uiState.useMetricUnits,
            themeMode: themeMode,
            onBack: onBack,
            onSettings: onSettings
        )
    }
}
