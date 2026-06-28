import SwiftUI
import SharedLogic

struct ModePickerView: View {
    let selectedMode: ThemeMode
    let isDark: Bool
    let onSelectMode: (ThemeMode) -> Void

    private var colors: SettingsColors {
        SettingsColors.current(isDark: isDark)
    }

    private let modes: [ThemeMode] = [.system, .light, .dark]
    private let cornerRadius: CGFloat = 16

    var body: some View {
        VStack(spacing: 0) {
            ForEach(Array(modes.enumerated()), id: \.offset) { index, mode in
                if index > 0 {
                    Rectangle()
                        .fill(colors.divider)
                        .frame(height: 1)
                        .padding(.horizontal, 16)
                }

                Button {
                    onSelectMode(mode)
                } label: {
                    HStack(spacing: 12) {
                        if selectedMode == mode {
                            Image(PokemonAssets.settingsCheckmark(isDark: isDark))
                                .renderingMode(.original)
                                .resizable()
                                .scaledToFit()
                                .frame(width: 16, height: 16)
                        } else {
                            Color.clear
                                .frame(width: 16, height: 16)
                        }

                        Text(mode.displayLabel())
                            .font(.system(size: 16, weight: .medium))
                            .foregroundStyle(colors.primaryText)

                        Spacer(minLength: 0)
                    }
                    .padding(.horizontal, 16)
                    .padding(.vertical, 14)
                }
                .buttonStyle(.plain)
            }
        }
        .frame(maxWidth: 240)
        .background(pickerBackground)
        .clipShape(RoundedRectangle(cornerRadius: cornerRadius, style: .continuous))
        .shadow(
            color: Color.black.opacity(isDark ? 0.35 : 0.12),
            radius: 12,
            y: 8
        )
    }

    @ViewBuilder
    private var pickerBackground: some View {
        if isDark {
            RoundedRectangle(cornerRadius: cornerRadius, style: .continuous)
                .fill(.ultraThinMaterial)
                .environment(\.colorScheme, .dark)
        } else {
            RoundedRectangle(cornerRadius: cornerRadius, style: .continuous)
                .fill(colors.pickerBackground)
        }
    }
}

#Preview {
    ModePickerView(
        selectedMode: .system,
        isDark: false,
        onSelectMode: { _ in }
    )
    .padding()
}
