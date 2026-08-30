package com.younesb.securevault.core.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// ─────────────────────────────────────────────
//  SecureVault — Design Tokens
//  Theme: Vault Black × Signal Yellow
//  Philosophy: Every color earns its place.
//  Yellow = action / unlocked / alive.
//  Black = vault / sealed / secure.
// ─────────────────────────────────────────────

// ── Core Palette ─────────────────────────────

/** Primary accent — electric signal yellow */
val VaultYellow         = Color(0xFFFFD600)
val VaultYellowBright   = Color(0xFFFFE033)   // hover / pressed highlight
val VaultYellowMuted    = Color(0xFFCCAB00)   // secondary actions, borders
val VaultYellowDim      = Color(0xFF665700)   // yellow container in dark theme

/** Neutrals — the vault walls */
val VaultBlack          = Color(0xFF0A0A0A)   // true background
val VaultSurface        = Color(0xFF111111)   // card / bottom sheet surface
val VaultSurfaceVariant = Color(0xFF1C1C1C)   // elevated surfaces, drawers
val VaultOutline        = Color(0xFF2E2E2E)   // borders, dividers
val VaultOutlineFaint   = Color(0xFF1A1A1A)   // subtle separators

/** On-colors */
val OnVaultYellow       = Color(0xFF1A1400)   // text/icons on yellow bg — near-black
val OnVaultBlack        = Color(0xFFF0F0F0)   // primary text on dark bg
val OnVaultSurface      = Color(0xFFE4E4E4)   // body text on surface
val OnVaultSurfaceVar   = Color(0xFFADADAD)   // secondary / hint text

/** Secondary — cold steel gray */
val VaultSteel          = Color(0xFF8A8A8A)
val VaultSteelContainer = Color(0xFF1E1E1E)
val OnVaultSteel        = Color(0xFFE8E8E8)
val OnVaultSteelCont    = Color(0xFFB8B8B8)

/** Tertiary — cipher teal (encrypted-state accent) */
val CipherTeal          = Color(0xFF00C2A8)   // encryption confirmed indicator
val CipherTealContainer = Color(0xFF00382F)
val OnCipherTeal        = Color(0xFF000000)
val OnCipherTealCont    = Color(0xFF7AEBD9)

/** Error */
val VaultRed            = Color(0xFFFF4C4C)
val VaultRedContainer   = Color(0xFF3B0000)
val OnVaultRed          = Color(0xFF000000)
val OnVaultRedCont      = Color(0xFFFFB3B3)

/** Scrim / overlay */
val VaultScrim          = Color(0xCC000000)   // 80% black overlay


// ─────────────────────────────────────────────
//  Material 3 — Dark Color Scheme (primary)
//  SecureVault is dark-first by design.
// ─────────────────────────────────────────────

val SecureVaultDarkColorScheme = darkColorScheme(

    // ── Primary (Signal Yellow) ───────────────
    primary                 = VaultYellow,
    onPrimary               = OnVaultYellow,
    primaryContainer        = VaultYellowDim,
    onPrimaryContainer      = VaultYellowBright,

    // ── Secondary (Steel Gray) ────────────────
    secondary               = VaultSteel,
    onSecondary             = OnVaultSteel,
    secondaryContainer      = VaultSteelContainer,
    onSecondaryContainer    = OnVaultSteelCont,

    // ── Tertiary (Cipher Teal) ────────────────
    tertiary                = CipherTeal,
    onTertiary              = OnCipherTeal,
    tertiaryContainer       = CipherTealContainer,
    onTertiaryContainer     = OnCipherTealCont,

    // ── Error ─────────────────────────────────
    error                   = VaultRed,
    onError                 = OnVaultRed,
    errorContainer          = VaultRedContainer,
    onErrorContainer        = OnVaultRedCont,

    // ── Background & Surface ──────────────────
    background              = VaultBlack,
    onBackground            = OnVaultBlack,

    surface                 = VaultSurface,
    onSurface               = OnVaultSurface,
    surfaceVariant          = VaultSurfaceVariant,
    onSurfaceVariant        = OnVaultSurfaceVar,

    // ── Outline ───────────────────────────────
    outline                 = VaultOutline,
    outlineVariant          = VaultOutlineFaint,

    // ── Inverse (used in snackbars, tooltips) ─
    inverseSurface          = OnVaultBlack,
    inverseOnSurface        = VaultBlack,
    inversePrimary          = VaultYellowMuted,

    // ── Scrim ─────────────────────────────────
    scrim                   = VaultScrim,
)


// ─────────────────────────────────────────────
//  Material 3 — Light Color Scheme (optional)
//  Provided for completeness / system override.
//  Not the primary experience — keep dark-first.
// ─────────────────────────────────────────────

val SecureVaultLightColorScheme = lightColorScheme(

    // ── Primary ───────────────────────────────
    primary                 = VaultYellowMuted,
    onPrimary               = Color(0xFFFFFFFF),
    primaryContainer        = VaultYellow,
    onPrimaryContainer      = OnVaultYellow,

    // ── Secondary ─────────────────────────────
    secondary               = Color(0xFF5A5A5A),
    onSecondary             = Color(0xFFFFFFFF),
    secondaryContainer      = Color(0xFFE0E0E0),
    onSecondaryContainer    = Color(0xFF1A1A1A),

    // ── Tertiary ──────────────────────────────
    tertiary                = Color(0xFF007A6B),
    onTertiary              = Color(0xFFFFFFFF),
    tertiaryContainer       = Color(0xFFB2F5EC),
    onTertiaryContainer     = Color(0xFF00201B),

    // ── Error ─────────────────────────────────
    error                   = Color(0xFFB3001B),
    onError                 = Color(0xFFFFFFFF),
    errorContainer          = Color(0xFFFFDAD8),
    onErrorContainer        = Color(0xFF410003),

    // ── Background & Surface ──────────────────
    background              = Color(0xFFF8F8F5),
    onBackground            = Color(0xFF1A1A1A),

    surface                 = Color(0xFFFFFFFF),
    onSurface               = Color(0xFF1A1A1A),
    surfaceVariant          = Color(0xFFEEEEEE),
    onSurfaceVariant        = Color(0xFF4A4A4A),

    // ── Outline ───────────────────────────────
    outline                 = Color(0xFFBBBBBB),
    outlineVariant          = Color(0xFFDDDDDD),

    // ── Inverse ───────────────────────────────
    inverseSurface          = Color(0xFF1A1A1A),
    inverseOnSurface        = Color(0xFFF0F0F0),
    inversePrimary          = VaultYellow,

    // ── Scrim ─────────────────────────────────
    scrim                   = Color(0x80000000),
)
