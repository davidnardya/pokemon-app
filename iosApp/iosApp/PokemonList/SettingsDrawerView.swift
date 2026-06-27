import SwiftUI
import SharedLogic

struct SettingsDrawerView: View {
    @Environment(\.colorScheme) private var colorScheme

    let settings: AppSettings
    let onClose: () -> Void
    let onToggleMetricUnits: () -> Void
    let onToggleTimeFormat: () -> Void
    let onToggleTheme: () -> Void

    private var colors: PokemonListColors {
        PokemonListColors.current(for: colorScheme)
    }

    var body: some View {
        VStack(spacing: 0) {
            HStack {
                Text("Settings")
                    .font(.system(size: 28, weight: .bold))
                    .foregroundStyle(colors.primaryText)

                Spacer()

                Button(action: onClose) {
                    Image(systemName: "xmark")
                        .font(.system(size: 14, weight: .semibold))
                        .foregroundStyle(colors.primaryText)
                        .frame(width: 40, height: 40)
                        .background(colors.settingsButtonBackground)
                        .clipShape(Circle())
                }
                .buttonStyle(.plain)
            }
            .padding(.horizontal, 24)
            .padding(.top, 24)
            .padding(.bottom, 16)

            VStack(spacing: 16) {
                settingsRow(
                    icon: "ruler",
                    title: "Units",
                    subtitle: settings.useMetricUnits ? "Metric" : "Imperial",
                    isOn: settings.useMetricUnits,
                    action: onToggleMetricUnits
                )

                settingsRow(
                    icon: "clock",
                    title: "Time Format",
                    subtitle: settings.use24HourTime ? "24-hour" : "12-hour",
                    isOn: settings.use24HourTime,
                    action: onToggleTimeFormat
                )

                settingsRow(
                    icon: "moon.fill",
                    title: "Theme",
                    subtitle: settings.useDarkTheme ? "Dark" : "Light",
                    isOn: settings.useDarkTheme,
                    action: onToggleTheme
                )
            }
            .padding(.horizontal, 24)
            .padding(.bottom, 32)

            Spacer(minLength: 0)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .background(colorScheme == .dark ? Color(red: 0.11, green: 0.11, blue: 0.118) : Color.white)
    }

    private func settingsRow(
        icon: String,
        title: String,
        subtitle: String,
        isOn: Bool,
        action: @escaping () -> Void
    ) -> some View {
        HStack(spacing: 12) {
            Image(systemName: icon)
                .font(.system(size: 18))
                .foregroundStyle(Color.orange)
                .frame(width: 24)

            VStack(alignment: .leading, spacing: 2) {
                Text(title)
                    .font(.system(size: 16, weight: .semibold))
                    .foregroundStyle(colors.primaryText)
                Text(subtitle)
                    .font(.system(size: 14))
                    .foregroundStyle(colors.secondaryText)
            }

            Spacer()

            Toggle("", isOn: Binding(
                get: { isOn },
                set: { _ in action() }
            ))
            .labelsHidden()
            .tint(Color.orange)
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
        .background(colors.settingsButtonBackground)
        .clipShape(RoundedRectangle(cornerRadius: 16, style: .continuous))
    }
}

#Preview {
    SettingsDrawerView(
        settings: AppSettings(useMetricUnits: true, use24HourTime: false, useDarkTheme: false),
        onClose: {},
        onToggleMetricUnits: {},
        onToggleTimeFormat: {},
        onToggleTheme: {}
    )
}
