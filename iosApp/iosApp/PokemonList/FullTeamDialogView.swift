import SharedLogic
import SwiftUI

struct FullTeamDialogView: View {
    @Environment(\.colorScheme) private var colorScheme
    let onClose: () -> Void

    private var colors: FullTeamDialogColors {
        FullTeamDialogColors.current(for: colorScheme)
    }

    var body: some View {
        ZStack {
            Color.black.opacity(0.45)
                .ignoresSafeArea()
                .onTapGesture(perform: onClose)

            VStack(spacing: 0) {
                Image("ic_full_team")
                    .renderingMode(.original)
                    .resizable()
                    .scaledToFit()
                    .frame(width: 186, height: 144)

                Spacer().frame(height: 24)

                Text("Team is full!")
                    .font(.system(size: 24, weight: .bold))
                    .foregroundStyle(colors.title)
                    .multilineTextAlignment(.center)

                Spacer().frame(height: 12)

                descriptionText
                    .font(.system(size: 16))
                    .foregroundStyle(colors.description)
                    .multilineTextAlignment(.center)
                    .lineSpacing(4)
                    .fixedSize(horizontal: false, vertical: true)

                Spacer().frame(height: 32)

                Button(action: onClose) {
                    Text("Close")
                        .font(.system(size: 17, weight: .bold))
                        .foregroundStyle(Color(argb: DialogButtonTheme.shared.closeButtonText()))
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 14)
                        .background(
                            LinearGradient(
                                colors: [
                                    Color(argb: DialogButtonTheme.shared.closeButtonGradientTop()),
                                    Color(argb: DialogButtonTheme.shared.closeButtonGradientBottom()),
                                ],
                                startPoint: .top,
                                endPoint: .bottom
                            )
                        )
                        .clipShape(Capsule())
                        .contentShape(Rectangle())
                }
                .buttonStyle(.plain)
            }
            .padding(.horizontal, 24)
            .padding(.top, 32)
            .padding(.bottom, 24)
            .frame(maxWidth: .infinity)
            .background(colors.background)
            .clipShape(RoundedRectangle(cornerRadius: 32, style: .continuous))
            .shadow(color: Color.black.opacity(colorScheme == .dark ? 0.35 : 0.12), radius: 24, y: 8)
            .padding(.horizontal, 24)
        }
    }

    private var descriptionText: Text {
        Text("Your team can only have ")
        + Text("6 Pokemons").fontWeight(.bold).foregroundStyle(colors.emphasisText)
        + Text(". Please, remove a member from your ")
        + Text("Team").fontWeight(.bold).foregroundStyle(colors.emphasisText)
        + Text(" before adding a new one.")
    }
}

private struct FullTeamDialogColors {
    let background: Color
    let title: Color
    let description: Color
    let emphasisText: Color

    static func current(for colorScheme: ColorScheme) -> FullTeamDialogColors {
        if colorScheme == .dark {
            return FullTeamDialogColors(
                background: Color(red: 0.11, green: 0.11, blue: 0.118),
                title: .white,
                description: Color(red: 0.557, green: 0.557, blue: 0.576),
                emphasisText: .white
            )
        }

        return FullTeamDialogColors(
            background: Color(red: 0.949, green: 0.949, blue: 0.949),
            title: Color(red: 0.133, green: 0.133, blue: 0.133),
            description: Color(red: 0.4, green: 0.4, blue: 0.4),
            emphasisText: Color(red: 0.267, green: 0.267, blue: 0.267)
        )
    }
}

#Preview("Light") {
    FullTeamDialogView(onClose: {})
        .preferredColorScheme(.light)
}

#Preview("Dark") {
    FullTeamDialogView(onClose: {})
        .preferredColorScheme(.dark)
}
