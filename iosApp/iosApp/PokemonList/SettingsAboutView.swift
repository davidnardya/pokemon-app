import SwiftUI

struct SettingsAboutView: View {
    let isDark: Bool
    let onClose: () -> Void

    private var colors: SettingsColors {
        SettingsColors.current(isDark: isDark)
    }

    var body: some View {
        ZStack(alignment: .topLeading) {
            VStack(spacing: 0) {
                Text("About")
                    .font(.system(size: 20, weight: .bold))
                    .foregroundStyle(colors.primaryText)
                    .frame(maxWidth: .infinity)
                    .padding(.top, 8)

                Spacer(minLength: 28)

                Image("ic_logo")
                    .renderingMode(.original)
                    .resizable()
                    .scaledToFit()
                    .frame(width: 80, height: 80)

                Spacer(minLength: 24)

                Text("Pokemon v1.0")
                    .font(.system(size: 16, weight: .semibold))
                    .foregroundStyle(colors.secondaryText)
                    .padding(.bottom, 8)
            }
            .frame(maxWidth: .infinity)
            .frame(height: 260)

            Button(action: onClose) {
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
            .padding(.leading, 8)
            .padding(.top, 4)
        }
        .padding(.horizontal, 24)
        .padding(.vertical, 20)
        .frame(maxWidth: 320)
        .background(colors.aboutBackground)
        .clipShape(RoundedRectangle(cornerRadius: 24, style: .continuous))
        .shadow(color: Color.black.opacity(0.18), radius: 24, y: 12)
    }
}

#Preview {
    ZStack {
        Color.black.opacity(0.4).ignoresSafeArea()
        SettingsAboutView(isDark: false, onClose: {})
    }
}
