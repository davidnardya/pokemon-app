import SwiftUI
import SharedLogic

struct SettingsDrawerView: View {
    @ObservedObject var viewModel: PokemonListViewModelWrapper
    let systemColorScheme: ColorScheme
    let onAboutTap: () -> Void

    private var settings: AppSettings {
        viewModel.uiState.settings
    }

    private var themeMode: ThemeMode {
        settings.themeMode
    }

    private var isDark: Bool {
        ThemeAppearance.isDark(
            themeMode: themeMode,
            systemColorScheme: systemColorScheme
        )
    }

    private var colors: SettingsColors {
        SettingsColors.current(isDark: isDark)
    }

    var body: some View {
        ZStack(alignment: .top) {
            VStack(spacing: 0) {
                Capsule()
                    .fill(colors.dragHandle)
                    .frame(width: 36, height: 5)
                    .padding(.top, 8)
                    .padding(.bottom, 16)

                ZStack {
                    Button(action: viewModel.closeSettings) {
                        Image(PokemonAssets.settingsClose(isDark: isDark))
                            .renderingMode(.original)
                            .resizable()
                            .scaledToFit()
                            .frame(width: 20, height: 20)
                            .padding(10)
                            .background(colors.closeButtonBackground)
                            .clipShape(Circle())
                    }
                    .buttonStyle(.plain)
                    .frame(maxWidth: .infinity, alignment: .leading)

                    Text("Settings")
                        .font(.system(size: 20, weight: .bold))
                        .foregroundStyle(colors.primaryText)
                }
                .padding(.bottom, 20)

                VStack(spacing: 0) {
                    unitRow
                    settingsDivider
                    timeRow
                    settingsDivider
                    modeRow
                    settingsDivider
                    aboutRow
                }
                .background(colors.cardBackground)
                .clipShape(RoundedRectangle(cornerRadius: 20, style: .continuous))

                Spacer(minLength: 0)
            }
            .padding(.horizontal, 20)
            .padding(.bottom, 24)
            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
            .background(colors.sheetBackground)
        }
        .overlay {
            if viewModel.uiState.isModePickerVisible {
                ZStack {
                    Color.clear
                        .contentShape(Rectangle())
                        .onTapGesture(perform: viewModel.closeModePicker)

                    ModePickerView(
                        selectedMode: settings.themeMode,
                        isDark: isDark,
                        onSelectMode: viewModel.selectThemeMode(_:)
                    )
                    .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .bottomTrailing)
                    .offset(x: -36, y: -168)
                }
            }
        }
    }

    private var unitRow: some View {
        HStack(spacing: 12) {
            Image(PokemonAssets.settingsUnit(isDark: isDark))
                .renderingMode(.original)
                .resizable()
                .scaledToFit()
                .frame(width: 24, height: 24)

            Text("Unit")
                .font(.system(size: 16, weight: .semibold))
                .foregroundStyle(colors.primaryText)
                .lineLimit(1)
                .fixedSize(horizontal: true, vertical: false)
                .layoutPriority(1)

            Spacer(minLength: 8)

            UnitSegmentedControl(
                useMetricUnits: settings.useMetricUnits,
                isDark: isDark,
                colors: colors,
                onSetMetricUnits: viewModel.setMetricUnits(useMetric:)
            )
            .layoutPriority(0)
            .fixedSize(horizontal: true, vertical: false)
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 14)
        .frame(maxWidth: .infinity, alignment: .leading)
        .accessibilityElement(children: .contain)
        .accessibilityLabel("Unit")
        .accessibilityValue(settings.useMetricUnits ? "Metric" : "Imperial")
    }

    private var timeRow: some View {
        Button {
            viewModel.setUse24HourTime(use24HourTime: !settings.use24HourTime)
        } label: {
            HStack(spacing: 12) {
                Image(PokemonAssets.settingsClock(isDark: isDark))
                    .renderingMode(.original)
                    .resizable()
                    .scaledToFit()
                    .frame(width: 24, height: 24)

                Text("24-Hour Time")
                    .font(.system(size: 16, weight: .semibold))
                    .foregroundStyle(colors.primaryText)

                Spacer()

                Toggle(
                    "",
                    isOn: Binding(
                        get: { settings.use24HourTime },
                        set: viewModel.setUse24HourTime(use24HourTime:)
                    )
                )
                .labelsHidden()
                .tint(isDark ? Color(red: 0.224, green: 0.224, blue: 0.235) : Color(red: 0.204, green: 0.780, blue: 0.349))
                .allowsHitTesting(false)
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 10)
            .frame(maxWidth: .infinity, alignment: .leading)
            .contentShape(Rectangle())
        }
        .buttonStyle(.plain)
        .accessibilityLabel("24-Hour Time")
        .accessibilityValue(settings.use24HourTime ? "On" : "Off")
        .accessibilityHint("Toggles 24-hour time format")
    }

    private var modeRow: some View {
        Button(action: viewModel.openModePicker) {
            HStack(spacing: 12) {
                Image(PokemonAssets.settingsMode(isDark: isDark))
                    .renderingMode(.original)
                    .resizable()
                    .scaledToFit()
                    .frame(width: 24, height: 24)

                Text("Mode")
                    .font(.system(size: 16, weight: .semibold))
                    .foregroundStyle(colors.primaryText)

                Spacer()

                HStack(spacing: 4) {
                    Text(settings.themeMode.displayLabel())
                        .font(.system(size: 16, weight: .medium))
                        .foregroundStyle(colors.secondaryText)

                    Image(PokemonAssets.settingsDownArrow(isDark: isDark))
                        .renderingMode(.original)
                        .resizable()
                        .scaledToFit()
                        .frame(width: 24, height: 24)
                }
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 14)
            .frame(maxWidth: .infinity, alignment: .leading)
            .contentShape(Rectangle())
        }
        .buttonStyle(.plain)
        .accessibilityLabel("Mode")
        .accessibilityValue(settings.themeMode.displayLabel())
        .accessibilityHint("Opens theme mode picker")
    }

    private var aboutRow: some View {
        Button(action: onAboutTap) {
            HStack(spacing: 12) {
                Image(PokemonAssets.settingsAbout(isDark: isDark))
                    .renderingMode(.original)
                    .resizable()
                    .scaledToFit()
                    .frame(width: 24, height: 24)

                Text("About")
                    .font(.system(size: 16, weight: .semibold))
                    .foregroundStyle(colors.primaryText)

                Spacer()

                Image(PokemonAssets.settingsRightArrow(isDark: isDark))
                    .renderingMode(.original)
                    .resizable()
                    .scaledToFit()
                    .frame(width: 24, height: 24)
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 14)
            .frame(maxWidth: .infinity, alignment: .leading)
            .contentShape(Rectangle())
        }
        .buttonStyle(.plain)
        .accessibilityLabel("About")
        .accessibilityHint("Opens about information")
    }

    private var settingsDivider: some View {
        Rectangle()
            .fill(colors.divider)
            .frame(height: 1)
            .padding(.horizontal, 16)
    }
}

private struct UnitSegmentedControl: View {
    let useMetricUnits: Bool
    let isDark: Bool
    let colors: SettingsColors
    let onSetMetricUnits: (Bool) -> Void

    var body: some View {
        HStack(spacing: 0) {
            segment(label: "Metric", selected: useMetricUnits) {
                onSetMetricUnits(true)
            }
            segment(label: "Imperial", selected: !useMetricUnits) {
                onSetMetricUnits(false)
            }
        }
        .padding(3)
        .frame(maxWidth: 188)
        .frame(height: 36)
        .background(colors.segmentBackground)
        .clipShape(Capsule())
    }

    private func segment(label: String, selected: Bool, action: @escaping () -> Void) -> some View {
        Button(action: action) {
            Text(label)
                .font(.system(size: 14, weight: selected ? .semibold : .medium))
                .foregroundStyle(selected ? colors.primaryText : colors.secondaryText)
                .lineLimit(1)
                .padding(.horizontal, 12)
                .padding(.vertical, 7)
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(
                    Group {
                        if selected {
                            Capsule()
                                .fill(colors.segmentSelectedBackground)
                                .shadow(
                                    color: isDark ? .clear : Color.black.opacity(0.08),
                                    radius: 2,
                                    y: 1
                                )
                        }
                    }
                )
                .contentShape(Rectangle())
        }
        .buttonStyle(.plain)
        .accessibilityLabel(label)
        .accessibilityAddTraits(selected ? [.isButton, .isSelected] : .isButton)
    }
}

struct SettingsColors {
    let sheetBackground: Color
    let primaryText: Color
    let secondaryText: Color
    let cardBackground: Color
    let closeButtonBackground: Color
    let dragHandle: Color
    let divider: Color
    let segmentBackground: Color
    let segmentSelectedBackground: Color
    let pickerBackground: Color
    let aboutBackground: Color

    static func current(isDark: Bool) -> SettingsColors {
        if isDark {
            return SettingsColors(
                sheetBackground: Color(red: 0.11, green: 0.11, blue: 0.118),
                primaryText: .white,
                secondaryText: Color(red: 0.557, green: 0.557, blue: 0.576),
                cardBackground: Color(red: 0.173, green: 0.173, blue: 0.180),
                closeButtonBackground: Color(red: 0.227, green: 0.227, blue: 0.235),
                dragHandle: Color(red: 0.353, green: 0.353, blue: 0.369),
                divider: Color(red: 0.227, green: 0.227, blue: 0.235),
                segmentBackground: Color(red: 0.227, green: 0.227, blue: 0.235),
                segmentSelectedBackground: Color(red: 0.353, green: 0.353, blue: 0.369),
                pickerBackground: Color(red: 0.173, green: 0.173, blue: 0.180),
                aboutBackground: Color(red: 0.173, green: 0.173, blue: 0.180)
            )
        }

        return SettingsColors(
            sheetBackground: Color(red: 0.961, green: 0.961, blue: 0.969),
            primaryText: Color(red: 0.102, green: 0.102, blue: 0.102),
            secondaryText: Color(red: 0.557, green: 0.557, blue: 0.576),
            cardBackground: .white,
            closeButtonBackground: .white,
            dragHandle: Color(red: 0.820, green: 0.820, blue: 0.839),
            divider: Color(red: 0.898, green: 0.898, blue: 0.918),
            segmentBackground: Color(red: 0.898, green: 0.898, blue: 0.918),
            segmentSelectedBackground: .white,
            pickerBackground: .white,
            aboutBackground: Color(red: 0.949, green: 0.949, blue: 0.969)
        )
    }

    static func current(for colorScheme: ColorScheme) -> SettingsColors {
        current(isDark: colorScheme == .dark)
    }
}

extension ThemeMode {
    func displayLabel() -> String {
        switch self {
        case .system: return "System"
        case .light: return "Light"
        case .dark: return "Dark"
        default: return "System"
        }
    }
}

#Preview {
    SettingsDrawerView(
        viewModel: PokemonListViewModelWrapper(),
        systemColorScheme: .light,
        onAboutTap: {}
    )
}
